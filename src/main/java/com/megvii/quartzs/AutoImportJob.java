package com.megvii.quartzs;

import com.megvii.configuration.SystemConfig;
import com.megvii.service.PhotoService;
import com.megvii.thread.DownloadThreadPool;
import com.megvii.utlis.FileIoUtils;
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



    @Autowired
    DownloadThreadPool downloadThreadPool;


    private FileIoUtils fileIoUtils = new FileIoUtils();

    @Scheduled(cron = "${improt.quartzs.job.time}")
    public void importTask(){

        synchronized(this) {
            try {
                log.info("定时器开始执行了...");
                if (!systemConfig.getTimerOnOff()) {
                    log.info("执行被拒绝，入库开关未打开！");
                    return;
                }
                if (!systemConfig.isJobExecutionStatus()) {
                    log.info("上次定时任务还未执行完毕，无法启动下次任务！");
                    return;
                }
                systemConfig.setJobExecutionStatus(false);
                //执行下载任务
                int downloadNumber = photoService.photoToLoca(systemConfig.getQueryMaxSize());
                if (downloadNumber == 0) {
                    log.info("没有新数据不进行入库操作!");
                    return;
                }
                while (true) {
                    if (downloadThreadPool.downloadQueue.size() > 0) {
                        log.info("下载队列中还存在数据不进行下一步操作：" + downloadThreadPool.downloadQueue.size());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    break;
                }
                //执行清除入库工具记录任务
                photoService.shellImprotPhoto(systemConfig.getClearShellName(), systemConfig.getShellPath(), systemConfig.getShellConfigPath());
                //执行调用入库工具任务
                photoService.shellImprotPhoto(systemConfig.getImprotShellName(), systemConfig.getShellPath(), systemConfig.getShellConfigPath());

                if (systemConfig.isJobPhotoDelete()) {
                    //执行删除列表下图片任务
                    String result = fileIoUtils.deleteFile(systemConfig.getFilePath());
                    log.info(result);
                }
            } finally {
                systemConfig.setJobExecutionStatus(true);
            }

        }
    }


}
