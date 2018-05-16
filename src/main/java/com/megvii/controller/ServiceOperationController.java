package com.megvii.controller;

import com.megvii.configuration.SystemConfig;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
import com.megvii.thread.DownloadHandlerThread;
import com.megvii.thread.DownloadThreadPool;
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

    //入库状态防止程序执行中，多次点击全量入库
    private  Boolean improtState= false;

    private TextUtils textUtils = new TextUtils();


    @PostMapping("/allImprotPhoto")
    @ApiOperation("执行全量入库操作接口(执行全量会关闭，定时任务)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stage", value = "执行阶段: 0.全阶段 1.图片落地阶段 2.入库阶段", paramType = "query",defaultValue = "0"),
    })
    public String allImportPhoto(String stage){
        systemConfig.setShellConfigPath("");
        systemConfig.setTimerOnOff(false);
        //全量存储文件地址
        systemConfig.setFilePath(filePath);
        if(improtState){
            return "目前全量入库执行尚未结束，请稍后再尝试！";
        }
        Thread thread =new Thread(new Runnable() {
            String thisStage =stage;
            @Override
            public void run() {
                //---------------------------一阶段 下载阶段-------------------------------------
                if("1".equals(thisStage) || "0".equals(thisStage)) {
                    improtState = true;
                    photoService.photoToLoca(systemConfig.getQueryMaxSize());
                }


                //等待队列中数据下载完毕
                while (true){
                    if(downloadThreadPool.downloadQueue.size()>0){
                        log.info("下载队列中还存在数据不进行下一步操作："+downloadThreadPool.downloadQueue.size());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    break;
                }

                //---------------------------二阶段 入库阶段-------------------------------------
                if("1".equals(thisStage)){
                    improtState= false;
                    return;
                }
                photoService.shellImprotPhoto(systemConfig.getClearShellName(),systemConfig.getShellPath(),systemConfig.getShellConfigPath());

                photoService.shellImprotPhoto(systemConfig.getImprotShellName(),systemConfig.getShellPath(),systemConfig.getShellConfigPath());
                improtState= false;
            }
        });
        thread.start();
        return "启动成功，请通过日志查询执行结果!";
    }

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

    @PostMapping("/test")
    @ApiOperation("测试用接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginNumber", value = "新增数量", paramType = "query", required = true),
            @ApiImplicitParam(name = "EndNumber", value = "=结束数量", paramType = "query", required = true),
    })
    public void test(Integer beginNumber,Integer  EndNumber) throws IOException {
        File file = new File("d://ysl.jpg");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = null;
        fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
        while ((fileChannel.read(byteBuffer)) > 0) {
            // do nothing
            // System.out.println("reading");
        }
        byte [] bytes = byteBuffer.array();
        for (int i =beginNumber; i < EndNumber; i++) {
            String sj= String.valueOf(i);
            for (int j = 0; j < (6-String.valueOf(i).length()); j++) {
                sj+="0";
            }
            photoService.testInsert(String.valueOf(i),bytes,"372928199011"+sj);

        }
        fileInputStream.close();
        return ;
    }

    @GetMapping("/getContinuinglyNode")
    @ApiOperation("获取续传或增量的节点信息(上次执行程序最后一条成功的数据的身份证号及时间)")
    public String red() throws IOException {
        return textUtils.readerOneRowText(systemConfig.getTextFilePaht());
    }

    @GetMapping("/clearContinuinglyNode")
    @ApiOperation("清除续传节点信息(会重新执行全量入库)")
    public String clearContinuinglyText() {
        downloadThreadPool.setTime("");
        Boolean b = textUtils.writerText(systemConfig.getTextFilePaht(),"",false);
        if(b){
            return "清除成功！";
        }
        return "清楚失败";
    }

    @PostMapping("/isDeletePhotoImg")
    @ApiOperation("增量执行时，执行完毕人像是否删除(默认删除，防止重复入库)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noORYes", value = "删除参数:1.删除 0.不删除", paramType = "query", required = true),
    })
    public String isDeletePhotoImg(String noORYes){
        if("1".equals(noORYes)){
            systemConfig.setJobPhotoDelete(true);
            return "设置成功";
        }else if("0".equals(noORYes)){
            systemConfig.setJobPhotoDelete(false);
            return "设置成功";
        }
        return "抱歉无法分辨您的传入！";

    }
}
