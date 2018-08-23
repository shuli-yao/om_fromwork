package com.megvii.controller;

import com.megvii.configuration.SystemConfig;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
import com.megvii.thread.DownloadHandlerThread;
import com.megvii.thread.DownloadThreadPool;
import com.megvii.utlis.FileIoUtils;
import com.megvii.utlis.ShellUtil;
import com.megvii.utlis.TextUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;

/**
 * 业务服务操作控制器
 * @author shuli.yao
 */
@RestController
@RequestMapping("/service")
@Api(description = "自动入库工具业务操作接口控制器")
@Slf4j
public class ServiceOperationController {

    @Autowired
    PhotoService photoService;

    @Autowired
    SystemConfig systemConfig;


    @Value("${photo.download.file.path}")
    String filePath;

    @Value("${photo.job.download.file.path}")
    String jobPhotoFilePath;

    @Autowired
    DownloadThreadPool downloadThreadPool;

    private FileIoUtils fileIoUtils = new FileIoUtils();

    //入库状态防止程序执行中，多次点击全量入库
    private  Boolean improtState= false;

    private TextUtils textUtils = new TextUtils();


    @PostMapping("/OnOrOffTimeImprot")
    @ApiOperation("打开或者关闭定时入库开关接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "onIsOff", value = "定时器开关参数:1.打开 0.关闭", paramType = "query", required = true),
    })
    public String openTimeImprot(String onIsOff){
        if("1".equals(onIsOff)){
            //增量文件地址
            systemConfig.setJobDownloadFilePath(jobPhotoFilePath);
            if(systemConfig.getShellConfigPath()==null || systemConfig.getShellConfigPath().equals("")){
                textUtils.copyChangeTextContext(systemConfig.getShellPath()+"config"+File.separator+"config.toml",23,"PHOTO_PATH = \""+systemConfig.getFilePath()+"\"");
                systemConfig.setShellConfigPath("."+File.separator+"config"+File.separator+"config_job.toml");
            }
            systemConfig.setTimerOnOff(true);
            return "打开成功，程序每天会进行自动查询入库操作！";
        }else if("0".equals(onIsOff)){
            systemConfig.setShellConfigPath("");
            systemConfig.setTimerOnOff(false);
            return "关闭成功，后续程序不会再执行自动入库操作！";
        }
        return "抱歉无法分辨您的传入！";

    }

    @GetMapping("/queryInfoLog")
    @ApiOperation("查询程序执行日志信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "onIsOff", value = "定时器开关参数:1.打开 0.关闭", paramType = "query", required = true),
    })
    public String queryInfoLog(String onIsOff){
        return "功能暂未实现";
    }

    @GetMapping("/clearContinuinglyNode")
    @ApiOperation("清除续传节点信息(会重新执行全量入库)")
    public String clearContinuinglyText() {
        downloadThreadPool.setTime("");
        Boolean b = textUtils.writerText(systemConfig.getTextFilePath(),"",false);
       textUtils.writerText(systemConfig.getQueryPageContextPath(),"",false);
        if(b){
            return "清除成功！";
        }
        return "清楚失败";
    }






    @PostMapping("/allXDBPhoto")
    @ApiOperation("执行全量入库操作接口(分批操作)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "begin", value = "开始条数", paramType = "query",defaultValue = "0"),
            @ApiImplicitParam(name = "end", value = "结束条数", paramType = "query",defaultValue = "0"),
    })
    public String allXDBPhoto(Integer begin,Integer end){

        //---------------执行前初始化，执行脚本信息---------------
        systemConfig.setShellConfigPath("");
        systemConfig.setTimerOnOff(false);
        //全量存储文件地址
        systemConfig.setFilePath(filePath);
        if(improtState){
            return "目前全量入库执行尚未结束，请稍后再尝试！";
        }

        //--------------------------为防止上次遗留数据，每次执行前会先执行一次清除操作--------------------------
        String result = fileIoUtils.deleteFile(systemConfig.getFilePath());
        log.info(result);
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {

                improtState = true;
                //--------------定义局部变量

                Integer queryMaxNumber= systemConfig.getQueryMaxSize();
                Integer beginNumber = 0;
                Integer topBeginNumber=0;
                Integer endNumber=beginNumber+queryMaxNumber;
                Integer performNumber=0;

                //---------------获取上次执行到位置--------------------
                String beginNumberStr= textUtils.readerOneRowText(systemConfig.getQueryPageContextPath());
                if(beginNumberStr!=null && !"".equals(beginNumberStr)){
                    String [] beginNumbers= beginNumberStr.split(",");
                    if(beginNumbers.length>0){
                        beginNumber=Integer.parseInt(beginNumbers[0]);
                        if(begin.intValue() > beginNumber.intValue()){
                            beginNumber = begin;
                        }
                        endNumber =beginNumber+queryMaxNumber;
                        topBeginNumber=Integer.parseInt(beginNumbers[1]);
                    }
                }

                //----------------执行分批入库位置---------------------
                if(endNumber.intValue() > end.intValue()){
                    endNumber = end;
                }
                while (true){
                    log.info("开始条数"+beginNumber+",落地条数:"+endNumber+",落地脚本正在执行请稍等...");
                    Date beginDate = new Date();
                    if(beginNumber>endNumber){
                        log.info("开始："+beginNumber+",结束:"+endNumber+"开始条数大于了结束条数end");
                        break;
                    }
                    Integer it = photoService.XDBPhotoToLoac(beginNumber,endNumber,"2010/01/01 00:00:00",topBeginNumber);

                    //等待队列中数据下载完毕
                    while (true){
                        if(downloadThreadPool.downloadQueue.size()>0){
                            log.info("下载队列中还存在数据不进行下一步操作："+downloadThreadPool.downloadQueue.size());
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }
                        break;
                    }
                    Date endDate = new Date();
                    log.info("落地完毕:"+it+"落地消耗时间："+(endDate.getTime()-beginDate.getTime())+"ms，当前已执行总数:"+performNumber);

                    photoService.shellImprotPhoto(systemConfig.getClearShellName(),systemConfig.getShellPath(),systemConfig.getShellConfigPath());

                    photoService.shellImprotPhoto(systemConfig.getImprotShellName(),systemConfig.getShellPath(),systemConfig.getShellConfigPath());

                    //执行删除图片文件
                    String result = fileIoUtils.deleteFile(systemConfig.getFilePath());

                    log.info(result);
                    if(endNumber.intValue() >=  end.intValue()){
                        System.out.println("结束"+it);
                        break;
                    }
                    //转换页码至下一页
                    topBeginNumber = beginNumber;
                    beginNumber = endNumber;
                    endNumber = endNumber + queryMaxNumber;
                    if(endNumber.intValue() > end.intValue()){
                        endNumber =end;
                    }
                    performNumber = performNumber + it;
                    //记录上次执行位置
                    textUtils.writerText(systemConfig.getQueryPageContextPath(), beginNumber + "," + beginNumber, false);
                    //清空记录的上次入库的信息
                    textUtils.writerText(systemConfig.getTextFilePath(), "", false);
                }

                improtState= false;
            }
        });
        thread.start();
        return "启动成功，请通过日志查询执行结果!";
    }



}
