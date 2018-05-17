package com.megvii.thread;

import com.megvii.configuration.SystemConfig;
import com.megvii.po.DownloadFileConfig;
import com.megvii.po.Photo;
import com.megvii.utlis.DateUtils;
import com.megvii.utlis.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 *  下载线程池
 * @author shuli.yao
 */
@Component
@Slf4j
public class DownloadThreadPool {

    @Value("${photo.download.queue.size}")
    public  int downloadQueueSize;    //定义下载队列长度

    @Value("${photo.download.thread.pool.size}")
    public  int poolSize;        //定义下载线程池大小

    TextUtils textUtils = new TextUtils();

    private  static String time ="";

    public void setTime(String timeStr){
        time =timeStr;
    }


    public  ExecutorService cachedThreadPool ;

    public  LinkedBlockingQueue<Photo> downloadQueue;

    public  LinkedBlockingQueue<Photo> textQueue;

    private DownloadFileConfig fileConfig = new DownloadFileConfig();

    @Autowired
    private SystemConfig systemConfig;


    /**
     * 通过初始化方法创建线程池及下载队列对象
     */
    @PostConstruct
    public void init(){

        this.downloadQueue = new LinkedBlockingQueue<Photo>(downloadQueueSize); //创建阻塞队列对象

        this.cachedThreadPool = Executors.newFixedThreadPool(poolSize);  //定义线程池

        this.textQueue = new LinkedBlockingQueue<Photo>(downloadQueueSize);

        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                writeText();
            }
        });

        thread.start();
    }


    /**
     * 获取阻塞队列方法
     * @return
     */
    public  LinkedBlockingQueue  getArrayBlockingQueue(){
        return downloadQueue;
    }

    /**
     * 执行线程
     */
    private  void runThread(){
            //向线程池中添加一个执行任务
            cachedThreadPool.execute(new DownloadHandlerThread(downloadQueue,systemConfig,textQueue));
    }

    /**
     * 向队列中添加数据
     */
    public  boolean putImgUrl(Photo value){
        try {
            //1.向下载队列中添加数据
            downloadQueue.put(value);
            //2.检查当前唤醒线程数，是否大于当前总线程数，若不大于则唤醒一个线程。
            if(((ThreadPoolExecutor)cachedThreadPool).getActiveCount() < poolSize){
                runThread();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  false;
        }
        return true;
    }

    public void writeText(){
        while (true){
            try {
                if(textQueue.size()>0){
                    Photo photo= textQueue.take();
                    if(photo ==null){
                        continue;
                    }

                    String changeTime = DateUtils.TIMEFORMAT.format(photo.getChangeTime());
                    String text = photo.getCardId()+","+changeTime;
                    textUtils.writerText(systemConfig.getTextFilePath(), text, true);
//                    if (time == null || "".equals(time) || "big".equals(stringDateCompare(changeTime, time))){
//                        textUtils.writerText(systemConfig.getTextFilePath(), text, false);
//                        time =changeTime;
//                    }else  if (time == null  || "".equals(time) || "etc".equals(stringDateCompare(changeTime, time))){
//                        textUtils.writerText(systemConfig.getTextFilePath(), text, true);
//                        time =changeTime;
//                    }
                    continue;
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.info("写入text失败");
            }
        }
    }

    public String stringDateCompare(String oneDateStr,String twoDateStr){

        Date oneDate = null;
        Date twoDate = null;
        try {
            oneDate = DateUtils.TIMEFORMAT.parse(oneDateStr);
            twoDate = DateUtils.TIMEFORMAT.parse(twoDateStr);
            if (oneDate.getTime() > twoDate.getTime()) {
                return "big";
            }else if(oneDate.getTime()==twoDate.getTime()){
                return "etc";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "less";
    }


}
