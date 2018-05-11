package com.megvii.thread;

import com.megvii.po.DownloadFileConfig;
import com.megvii.po.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *  下载线程池
 * @author shuli.yao
 */
@Component
public class DownloadThreadPool {

    @Value("${photo.download.queue.size}")
    public  int downloadQueueSize;    //定义下载队列长度

    @Value("${photo.download.thread.pool.size}")
    public  int poolSize;        //定义下载线程池大小

    @Value("${photo.download.file.path}")
    String filePath;

    @Value("${photo.download.file.suffix}")
    String fileSuffix;

    public  ExecutorService cachedThreadPool ;

    public  ArrayBlockingQueue<Photo> downloadQueue;

    private DownloadFileConfig fileConfig = new DownloadFileConfig();


    /**
     * 通过初始化方法创建线程池及下载队列对象
     */
    @PostConstruct
    public void init(){

        this.downloadQueue = new ArrayBlockingQueue<Photo>(downloadQueueSize); //创建阻塞队列对象

        this.cachedThreadPool = Executors.newFixedThreadPool(poolSize);  //定义线程池

        fileConfig.setFilePath(filePath);

        fileConfig.setSuffix(fileSuffix);
    }


    /**
     * 获取阻塞队列方法
     * @return
     */
    public  ArrayBlockingQueue  getArrayBlockingQueue(){
        return downloadQueue;
    }

    /**
     * 执行线程
     */
    private  void runThread(){
            //向线程池中添加一个执行任务
            cachedThreadPool.execute(new DownloadHandlerThread(downloadQueue,fileConfig));
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



}
