package com.megvii.thread;

import java.util.concurrent.ArrayBlockingQueue;

import com.megvii.configuration.SystemConfig;
import com.megvii.po.DownloadFileConfig;
import com.megvii.po.Photo;
import com.megvii.utlis.FileDownload;
import com.megvii.utlis.TextUtils;

/**
 * 执行下载程序线程对象
 * @author shuli.yao
 */
public class DownloadHandlerThread implements Runnable {

    private FileDownload fileDownload = new FileDownload(); //定义文件下载对象

    private ArrayBlockingQueue<Photo> downloadQueue;   //定义下载队列

    private DownloadFileConfig fileConfig;

    private SystemConfig systemConfig;

    TextUtils textUtils = new TextUtils();

    /**
     * 根据构造方法注入入库线程池对象及下载队列
     * @param downloadQueue
     */
    DownloadHandlerThread(ArrayBlockingQueue<Photo> downloadQueue,DownloadFileConfig fileConfig,SystemConfig systemConfig){
        this.downloadQueue = downloadQueue;   //获取到下载队列
        this.fileConfig = fileConfig;
        this.systemConfig = systemConfig;
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
                fileName += (photo.getHJ()!=null?photo.getHJ():"none");
                //数据来源
                fileName += (photo.getDataSource()!=null?photo.getDataSource().substring(0,19):"none")+"_";
                //文件后缀
                fileName += "."+fileConfig.getSuffix();
                boolean outputResult = fileDownload.outputFile(photo.getPhotoFileData(),
                        fileName,fileConfig.getFilePath());

                String text = photo.getCardId()+","+photo.getChangeTime();
                textUtils.writerText(systemConfig.getTextPaht(),text,false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
