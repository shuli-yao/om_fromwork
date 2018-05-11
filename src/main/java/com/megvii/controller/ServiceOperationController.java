package com.megvii.controller;

import com.megvii.configuration.SystemConfig;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
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

    //入库状态防止程序执行中，多次点击全量入库
    private  Boolean improtState= false;

    private TextUtils textUtils = new TextUtils();


    @PostMapping("/allImprotPhoto")
    @ApiOperation("执行全量入库操作接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stage", value = "执行阶段: 0.全阶段 1.图片落地阶段 2.入库阶段", paramType = "query",defaultValue = "0"),
    })
    public String allImportPhoto(String stage){
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

                //---------------------------二阶段 入库阶段-------------------------------------
                if("1".equals(thisStage)){
                    improtState= false;
                    return;
                }
                photoService.shellImprotPhoto(systemConfig.getFileName(),systemConfig.getShellPath(),systemConfig.getShellConfigPath());
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
            systemConfig.setTimerOnOff(true);
            return "打开成功，程序每天会进行自动查询入库操作！";
        }else if("0".equals(onIsOff)){
            systemConfig.setTimerOnOff(false);
            return "关闭成功，后续程序不会再执行自动入库操作！";
        }
        return "抱歉无法分辨您的传入！";

    }

    @PostMapping("/queryInfoLog")
    @ApiOperation("查询程序执行日志信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "onIsOff", value = "定时器开关参数:1.打开 0.关闭", paramType = "query", required = true),
    })
    public String queryInfoLog(String onIsOff){
        if("1".equals(onIsOff)){
            systemConfig.setTimerOnOff(true);
            return "打开成功，程序每天会进行自动查询入库操作！";
        }else if("0".equals(onIsOff)){
            systemConfig.setTimerOnOff(false);
            return "关闭成功，后续程序不会再执行自动入库操作！";
        }
        return "抱歉无法分辨您的传入！";

    }

    @PostMapping("/test")
    public void test(@ApiParam Integer  number) throws IOException {
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
        number = 1000;
        for (int i =2; i < number; i++) {
            String sj= String.valueOf(i);
            for (int j = 0; j < (6-String.valueOf(i).length()); j++) {
                sj+="0";
            }
            photoService.testInsert(String.valueOf(i),bytes,"372928199011"+sj);

        }
        fileInputStream.close();
        return ;
    }

    @GetMapping("/red")
    public String red() throws IOException {
        return textUtils.readerText(systemConfig.getTextPaht());
    }
}
