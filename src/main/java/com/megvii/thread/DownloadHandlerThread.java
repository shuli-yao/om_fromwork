package com.megvii.thread;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import com.megvii.configuration.SystemConfig;
import com.megvii.po.DownloadFileConfig;
import com.megvii.po.Photo;
import com.megvii.utlis.FileDownload;
import com.megvii.utlis.TextUtils;
import org.apache.http.client.utils.DateUtils;

/**
 * 执行下载程序线程对象
 * @author shuli.yao
 */
public class DownloadHandlerThread implements Runnable {

    private FileDownload fileDownload = new FileDownload(); //定义文件下载对象

    private ArrayBlockingQueue<Photo> downloadQueue;   //定义下载队列

    private ArrayBlockingQueue<Photo> textQueue;   //定义写入text队列

    private SystemConfig systemConfig;



    /**
     * 根据构造方法注入入库线程池对象及下载队列
     * @param downloadQueue
     */
    DownloadHandlerThread(ArrayBlockingQueue<Photo> downloadQueue,SystemConfig systemConfig,ArrayBlockingQueue<Photo> textQueue){
        this.downloadQueue = downloadQueue;   //获取到下载队列
        this.systemConfig = systemConfig;
        this.textQueue = textQueue;
    }

    @Override
    public void run() {

        //1.获取队列中的数据，直到队列为空时
        while(downloadQueue.size() > 0){
            Photo photo = null;
            try {
                //2.获取下载对象
                photo = downloadQueue.take();
                //姓名
                String fileName = (photo.getName()!=null?photo.getName():"none")+"_";
                //身份证号
                fileName += (photo.getCardId()!=null?photo.getCardId():"none")+"_";
                //性别
                fileName += (photo.getSex()!=null?photo.getSex():"none")+"_";
                //民族
                fileName += (photo.getEthnic()!=null?photo.getEthnic():"none")+"_";
                //户籍
                fileName += (photo.getHJ()!=null?photo.getHJ():"none")+"_";
                //数据来源
                fileName += (photo.getDataSource()!=null?photo.getDataSource().substring(0,19):"none");
                //文件后缀
                fileName += "."+systemConfig.getFileSuffix();
                boolean outputResult = fileDownload.outputFile(photo.getPhotoFileData(),
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
