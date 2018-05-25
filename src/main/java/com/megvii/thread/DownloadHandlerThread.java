package com.megvii.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.megvii.configuration.SystemConfig;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
import com.megvii.utlis.FileDownload;

/**
 * 执行下载程序线程对象
 * @author shuli.yao
 */
public class DownloadHandlerThread implements Runnable {

    private FileDownload fileDownload = new FileDownload(); //定义文件下载对象

    private LinkedBlockingQueue<Photo> downloadQueue;   //定义下载队列

    private LinkedBlockingQueue<Photo> textQueue;   //定义写入text队列

    private SystemConfig systemConfig;

    private PhotoService photoService;


    private final Lock queueLock=new ReentrantLock();

    /**
     * 根据构造方法注入入库线程池对象及下载队列
     * @param downloadQueue
     */
    DownloadHandlerThread(LinkedBlockingQueue<Photo> downloadQueue,SystemConfig systemConfig,LinkedBlockingQueue<Photo> textQueue,PhotoService photoService){
        this.downloadQueue = downloadQueue;   //获取到下载队列
        this.systemConfig = systemConfig;
        this.textQueue = textQueue;
        this.photoService = photoService;
    }

    @Override
    public void run() {

        //1.获取队列中的数据，直到队列为空时
        while(downloadQueue.size() > 0){
            Photo photo = null;
            try {
                //2.获取下载对象
                synchronized (downloadQueue){
                    photo = downloadQueue.take();
                    downloadQueue.remove(photo);
                }
                byte [] dataSource = photoService.findDataByCardId(photo.getCardId());
                if(dataSource ==null){
                    System.out.println("身份证号："+photo.getCardId()+"未查出数据不进行下载！");
                    return;
                }
                //姓名
                String fileName = (photo.getName()!=null?photo.getName():"none")+"_";
                //身份证号
                fileName += (photo.getCardId()!=null?photo.getCardId():"none")+"_";
                //性别
                if(photo.getSex()==null || "".equals(photo.getSex()) || !"2".equals(photo.getSex()) || !"1".equals(photo.getSex())){
                    fileName +="none"+"_";
                }else if("2".equals(photo.getSex())){
                    fileName += "female"+"_";
                }else if("1".equals(photo.getSex())){
                    fileName += "male"+"_";
                }
                //民族
                fileName +="none_";
                //户籍
                fileName += (photo.getHJ()!=null?photo.getHJ():"none")+"_";
                //数据来源
                fileName += (photo.getDataSource()!=null?photo.getDataSource().substring(0,19):"none");
                //文件后缀
                fileName += "."+systemConfig.getFileSuffix();
                boolean outputResult = fileDownload.outputFile(dataSource,
                        fileName,systemConfig.getFilePath());
                textQueue.put(photo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





}
