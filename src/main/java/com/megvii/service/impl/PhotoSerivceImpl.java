package com.megvii.service.impl;

import com.megvii.configuration.SystemConfig;
import com.megvii.dbsource.oracle.mapper.PhotoMapper;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
import com.megvii.thread.DownloadThreadPool;
import com.megvii.utlis.ShellUtil;
import com.megvii.utlis.TextUtils;
import com.sun.javafx.collections.MappingChange;
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
        return photoMapper.selectPhotoByPage(begin,end,date);
    }

    @Override
    public int selectPhotoCount(String date) {
        return photoMapper.selectPhotoCount(date);
    }



    @Override
    public void photoToLoca(Integer queryMaxSize) {
        log.info("开始执行入库工具，落地图片功能！");

        //--------------------读取txt判断上次上传节点----------------
        String result= textUtils.readerOneRowText(systemConfig.getTextPaht());
        String queryDate = "2010-01-01 00:00:00";
        String cardId = "";
        if(result!=null && !result.equals("")){
            String [] results = result.split(",");
            queryDate = results[1];
            cardId = results[0];
            ContinuinglyNodeSwitch = false;
            if(queryDate.length()== 10){
                queryDate=queryDate+" 00:00:00";
            }
        }

        //--------------------获取总数并进行切割判断需要执行多少次分页查询----------------
        int count = selectPhotoCount(queryDate);
        int whileCount = count%queryMaxSize==0?count/queryMaxSize:count/queryMaxSize+1 ;
//        int whileCount =5;

        //--------------------循环执行数据下载功能----------------
        int begin = 0;
        int end = queryMaxSize;
        for (int i = 0; i < whileCount; i++) {
            Date beginDate = new Date();
            List<Photo> photos = findByPhotos(begin, end,queryDate);
            for (Photo photo : photos) {
                if(ContinuinglyNodeSwitch){
                    downloadThreadPool.putImgUrl(photo);
                }
                if(ContinuinglyNodeSwitch == false && photo.getCardId().equals(cardId)){
                    ContinuinglyNodeSwitch =true;
                }
            }
            Date endDate = new Date();
            log.info("执行第" + (i + 1) + "批数据，每批数据最大上限为" + queryMaxSize + ",耗时" + (endDate.getTime() - beginDate.getTime()));
            begin = end;
            end = end + queryMaxSize;
        }
    }

    @Override
    public void shellImprotPhoto(String fileName,String shellPath,String shellConfigPath) {
        log.info("开始执行图片及信息入库功能！");
        String command = "sh "+ fileName+" "+shellConfigPath;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("changeTiem",simpleDateFormat.format(new Date()));

        map.put("cradId",cradId);
        photoMapper.testInsert(map);
    }
}
