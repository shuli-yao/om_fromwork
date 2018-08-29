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

    //获取文件下载对象
    private FileDownload fileDownload = new FileDownload();

    //定义下载队列
    private LinkedBlockingQueue<Photo> downloadQueue;

    //定义写入text队列
    private LinkedBlockingQueue<Photo> textQueue;

    //定义系统环境变量对象
    private SystemConfig systemConfig;

    //定义数据库操作接口对象
    private PhotoService photoService;

    /**
     * 根据构造方法注入,入库线程池对象及下载队列
     * @param downloadQueue
     */
    DownloadHandlerThread(LinkedBlockingQueue<Photo> downloadQueue,SystemConfig systemConfig,LinkedBlockingQueue<Photo> textQueue,PhotoService photoService){
        this.downloadQueue = downloadQueue;   //获取到下载队列
        this.systemConfig = systemConfig;     //获取系统变量对象
        this.textQueue = textQueue;           //获取txt写入队列
        this.photoService = photoService;     //获取数据库操作对象
    }

    /**
     * 流程:
     *  1.从队列中读取人像数据
     *  2.根据身份证号获取人像照片信息
     *  3.组装图片名称将图片下载到本地
     *  4.将落地图片信息写入txt文本 (用于增量去重)
     */
    @Override
    public void run() {

        //文件命名拼接符
        String splitChar = "_";

        //获取队列中的数据，直到队列为空时
        while(downloadQueue.size() > 0){
            Photo photo = null;
            try {
                //--------------------1.通过队列获取落地人像信息--------------------
                synchronized (downloadQueue){
                    photo = downloadQueue.take();
                    downloadQueue.remove(photo);
                }

                //--------------------2.通过身份证号获取人像照片信息--------------------
                byte [] dataSource = photoService.findDataByCardId(photo.getCardId());
                if(dataSource ==null){
                    System.out.println("身份证号："+photo.getCardId()+"未查出数据不进行下载！");
                    return;
                }

                //--------------------3.组装图片名称并进行图片落地操作--------------------
                //姓名
                String fileName = (photo.getName()!=null?photo.getName():"none")+splitChar;
                //身份证号
                fileName += (photo.getCardId()!=null?photo.getCardId():"none")+splitChar;
                //性别
                if(photo.getSex()==null || "".equals(photo.getSex()) || !"2".equals(photo.getSex()) || !"1".equals(photo.getSex())){
                    fileName +=getSex(photo.getCardId())+splitChar;
                }else if("2".equals(photo.getSex())){
                    fileName += "female"+splitChar;
                }else if("1".equals(photo.getSex())){
                    fileName += "male"+splitChar;
                }
                //民族
                fileName +=getEthnic(photo.getEthnic())+splitChar;
                //户籍
                fileName += (photo.getHJ()!=null?photo.getHJ():"none")+splitChar;
                //数据来源
                fileName += (photo.getDataSource()!=null?photo.getDataSource().substring(0,19):"none");
                //文件后缀
                fileName += "."+systemConfig.getFileSuffix();

                //调用图片落地程序
                boolean outputResult = fileDownload.outputFile(dataSource,
                        fileName,systemConfig.getFilePath());

                //--------------------4.将落地人像信息放入txt写入队列--------------------
                textQueue.put(photo);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据数据库中民族代码获得民族信息
     * @param ethnicNumber
     * @return
     */
    public String getEthnic(String ethnicNumber){
        if("05".equals(ethnicNumber)){
            return "维吾尔族";
        }else if("01".equals(ethnicNumber)){
            return "汉族";
        }else{
            return "none";
        }
    }

    /**
     * 根据身份证号计算性别
     * @param idcard
     * @return
     */
    public String getSex(String idcard){
        Integer sexNumber;
        if(idcard.length()==18){
            sexNumber = Integer.parseInt(idcard.substring(16,17));
        }else if(idcard.length()==15){
            sexNumber = Integer.parseInt(idcard.substring(14,15));
        }else{
            return "none";
        }
        if (sexNumber % 2 != 0) {
            return "male";//男
        } else {
            return "female";//女
        }
    }



}
