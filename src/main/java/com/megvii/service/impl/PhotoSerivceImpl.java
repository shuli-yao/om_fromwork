package com.megvii.service.impl;

import com.megvii.configuration.SystemConfig;
import com.megvii.dbsource.oracle.mapper.PhotoMapper;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
import com.megvii.thread.DownloadThreadPool;
import com.megvii.utlis.DateUtils;
import com.megvii.utlis.ShellUtil;
import com.megvii.utlis.TextUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Service
@Slf4j

public class PhotoSerivceImpl implements PhotoService{

    //执行shell脚本工具对象
    private ShellUtil shellUtil =new ShellUtil();

    //获取或写入txt脚本工具信息
    private TextUtils textUtils = new TextUtils();

    //续传节点开关，用来判断从节点以后的数据才进行落地（时间相同的话，可能重复查出上次落地数据）
    private Boolean ContinuinglyNodeSwitch =true;

    @Autowired
    SystemConfig systemConfig;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    DownloadThreadPool downloadThreadPool;



    @Override
    public List<Photo> findByPhotos(int begin,int end,String date) {
        return photoMapper.selectPhotoByPage(begin,end,date,systemConfig.getInputDbTalbeName());
    }

    public List<Photo> findByPhotosNoOrder(int begin,int end,String date) {
        return photoMapper.selectPhotoByPage(begin,end,date,systemConfig.getInputDbTalbeName());
    }

    @Override
    public int selectPhotoCount(String date) {
        return photoMapper.selectPhotoCount(date,systemConfig.getInputDbTalbeName());
    }

    @Override
    public Integer photoToLoca(Integer queryMaxSize) {
        log.info("一、开始执行入库工具，落地图片功能！");

        //--------------------读取txt判断上次上传节点----------------
        String result= textUtils.readerOneRowText(systemConfig.getTextFilePath());
        String queryDate = "2010-01-01 00:00:00";
        String cardId = "";
        if(result!=null && !result.equals("")){
            String [] results = result.split(",");
            queryDate = results[1];
            if(queryDate.length()== 10){
                queryDate=queryDate+" 00:00:00";
            }
        }
        //--------------------循环执行数据下载功能----------------
        int begin = 0;
        int end = queryMaxSize;

        int  countNumbe = 0;
        int  addNumber =0;
        int  repeatNumber =0;

        int i = 0;
        while (true){
            i++;
            log.info(queryDate);
            Date beginDate = new Date();
            List<Photo> photos = findByPhotosNoOrder(begin,end,queryDate);
            for (Photo photo : photos) {
                countNumbe++;
                if(queryDate.equals(photo.getChangeTime())){
                    if(compareCardAndTime(photo.getCardId(),photo.getChangeTime())){
                        downloadThreadPool.putImgUrl(photo);
                        continue;
                    }
                    repeatNumber++;
                }else{
                    addNumber++;
                    downloadThreadPool.putImgUrl(photo);
                }
            }
            Date endDate = new Date();
            log.info("四、执行第" + (i + 1) + "批数据，查询到数据数量：" + countNumbe + ",已存在数量:"+repeatNumber+",新增数量:"+addNumber+" ,耗时" + (endDate.getTime() - beginDate.getTime()));
            begin = end;
            end = end + queryMaxSize;
            if(photos.size()< queryMaxSize){
                break;
            }
        }
        System.out.println("五、入库数量:"+addNumber);
        return addNumber;
    }

    /**
     * 分批入库方法
     * @param
     * @return
     */
    @Override
    public Integer photoPartialToLoca(Integer beginNumber,Integer sizeNumber,String date,Integer topBeginNumber) {
        Date queryDate = new Date();
        List<Photo> photos = findByPhotosNoOrder(beginNumber,sizeNumber,date);
        Date endDate = new Date();
        log.info("查询时间为："+(endDate.getTime()-queryDate.getTime())+"ms");
        for (Photo photo : photos) {
            downloadThreadPool.putImgUrl(photo);
            //判断如果两次一致，为防止重复进入判断状态
            if(beginNumber == topBeginNumber){
                if(compareCardAndTime(photo.getCardId(),photo.getChangeTime())){
                    downloadThreadPool.putImgUrl(photo);
                    continue;
                }
                continue;
            }
            downloadThreadPool.putImgUrl(photo);
        }
        return photos.size();
    }

    @Override
    public void shellImprotPhoto(String fileName,String shellPath,String shellConfigPath) {
        log.info("开始执行图片及信息入库功能！");

        String command = "bash "+ fileName;
        if(shellConfigPath!=null|| !shellConfigPath.equals("")){
            command +=" "+shellConfigPath;
        }
        log.info("执行命令："+command);
        try {
            String result =shellUtil.execCmd(command, new File(shellPath));
            log.info("执行结果:\t"+result);
        } catch (Exception e) {
            log.error("调用入库工具发生异常，请联系开发人员,异常："+e.getMessage());
        }
    }

    @Override
    public void testInsert(String id,byte [] bytes,String cradId) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("bytes",bytes);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        map.put("changeTiem",simpleDateFormat.format(new Date()));
        map.put("tableName",systemConfig.getInputDbTalbeName());
        map.put("cradId",cradId);
        photoMapper.testInsert(map);
    }

    @Override
    public String checkPhotoImport(Integer end) {
        String queryDate = "2010/01/01 00:00:00";
        List<Photo> photos = findByPhotos(0, end,queryDate);
        for (Photo photo : photos) {
            downloadThreadPool.putImgUrl(photo);
        }
        return "";
    }


        private boolean compareCardAndTime(String Card, Date Time){

        List<String> readList = textUtils.readerText(systemConfig.getTextFilePath());
        for (String s : readList) {
            if(s.equals(Card+","+DateUtils.TIMEFORMAT.format(Time))) {
                return false;
            }
        }
       return true;
    }
}
