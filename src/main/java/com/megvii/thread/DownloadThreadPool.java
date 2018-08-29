package com.megvii.thread;

import com.megvii.configuration.SystemConfig;
import com.megvii.po.DownloadFileConfig;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
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

    //获取下载队列长度
    @Value("${photo.download.queue.size}")
    public  int downloadQueueSize;

    //获取下载线程池大小
    @Value("${photo.download.thread.pool.size}")
    public  int poolSize;

    //获取系统变量对象
    @Autowired
    private SystemConfig systemConfig;

    //获取数据库操作对象
    @Autowired
    private PhotoService photoService;

    //获取txt操作对象
    TextUtils textUtils = new TextUtils();

    //定义线程池对象
    public  ExecutorService cachedThreadPool ;

    //定义下载队列
    public  LinkedBlockingQueue<Photo> downloadQueue;

    //定义txt写入队列
    public  LinkedBlockingQueue<Photo> textQueue;

    //定义时间戳变量
    private  static String time ="";
    public void setTime(String timeStr){
        time =timeStr;
    }



    /**
     * 通过初始化方法创建线程池及下载队列对象
     */
    @PostConstruct
    public void init(){

        this.downloadQueue = new LinkedBlockingQueue<Photo>(downloadQueueSize); //创建下载队列

        this.cachedThreadPool = Executors.newFixedThreadPool(poolSize);  //创建线程池

        this.textQueue = new LinkedBlockingQueue<Photo>(downloadQueueSize); //创建txt写入队列

        //启动txt写入方法
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                writeText();
            }
        });
        thread.start();
    }



    /**
     * 执行线程
     */
    private  void runThread(){
            //向线程池中添加一个执行任务
            cachedThreadPool.execute(new DownloadHandlerThread(downloadQueue,systemConfig,textQueue,photoService));
    }

    /**
     * 向下载队列中添加数据
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

    /**
     * 获取阻塞队列方法
     * @return
     */
    public  LinkedBlockingQueue  getArrayBlockingQueue(){
        return downloadQueue;
    }


     /**
     *

     */
    /**
     * txt写入程序
     * 流程
     *  1.从文本写入队列中获取需要写入人像对象
     *  2.组装写入内容
     *  3.检查写入规则所有| 工作
     *  4.根据不同规则写入txt，全量会一直追加txt，增量会根据时间清空txt
     */
    public void writeText(){
        while (true){
            try {
                if(textQueue.size()>0){

                    //1.获取txt写入队列中数据
                    Photo photo= textQueue.take();
                    if(photo ==null){
                        continue;
                    }

                    //2.格式化组装写入文本
                    String changeTime = DateUtils.TIMEFORMAT.format(photo.getChangeTime());
                    String text = photo.getCardId()+","+changeTime;

                    //3.检查写入规则
                    if("all".equals(systemConfig.getImprotType())){

                        //4.全量持续写入
                        textUtils.writerText(systemConfig.getTextFilePath(), text, true);

                    } if("job".equals(systemConfig.getImprotType())){

                        //4.增量根据时间判断，会清除txt脚本
                        if (time == null || "".equals(time) || "big".equals(stringDateCompare(changeTime, time))){
                            textUtils.writerText(systemConfig.getTextFilePath(), text, false);
                            time =changeTime;
                        }else  if (time == null  || "".equals(time) || "etc".equals(stringDateCompare(changeTime, time))){
                            textUtils.writerText(systemConfig.getTextFilePath(), text, true);
                            time =changeTime;
                        }
                    }

                    continue;
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.info("写入text失败");
            }
        }
    }

    /**
     * 比较时间大小
     * @param oneDateStr
     * @param twoDateStr
     * @return
     */
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
