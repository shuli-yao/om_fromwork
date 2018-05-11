package com.megvii.quartzs;

import com.megvii.configuration.SystemConfig;
import com.megvii.service.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AutoImportJob {

    @Autowired
    SystemConfig systemConfig;

    @Autowired
    PhotoService photoService;


    @Scheduled(cron = "${improt.quartzs.job.time}")
    public void importTask(){
        log.info("定时器开始执行了...");
        if(!systemConfig.getTimerOnOff()){
            log.info("执行被拒绝，入库开关未打开！");
            return;
        }
        photoService.photoToLoca(systemConfig.getQueryMaxSize());

        photoService.shellImprotPhoto(systemConfig.getFileName(),systemConfig.getShellPath(),systemConfig.getShellConfigPath());
    }


}
