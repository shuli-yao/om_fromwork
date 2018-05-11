package com.megvii.service.impl;

import com.megvii.dbsource.oracle.mapper.PhotoMapper;
import com.megvii.po.Photo;
import com.megvii.service.PhotoService;
import com.megvii.thread.DownloadThreadPool;
import com.megvii.utlis.ShellUtil;
import com.sun.javafx.collections.MappingChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Service
@Slf4j

public class PhotoSerivceImpl implements PhotoService{


    private ShellUtil shellUtil =new ShellUtil();

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    DownloadThreadPool downloadThreadPool;

    @Override
    public List<Photo> findByPhotos(int begin,int end) {
        return photoMapper.selectPhotoByPage(begin,end);
    }

    @Override
    public int selectPhotoCount() {
        return photoMapper.selectPhotoCount();
    }

    @Override
    public void photoToLoca(Integer queryMaxSize) {
        log.info("开始执行入库工具，落地图片功能！");
        int count = selectPhotoCount();
//                int whileCount = count/queryMaxSize ;
        int whileCount = 10;
        int begin = 0;
        int end = queryMaxSize;
        for (int i = 0; i < whileCount; i++) {
            Date beginDate = new Date();
            List<Photo> photos = findByPhotos(begin, end);
            for (Photo photo : photos) {
                downloadThreadPool.putImgUrl(photo);
            }
            Date endDate = new Date();
            log.info("执行第" + (i + 1) + "批数据，每批数据最大上限为" + queryMaxSize + ",耗时" + (endDate.getTime() - beginDate.getTime()));
            begin = end;
            end = end + 100;
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
        map.put("changeTiem",new Date());

        map.put("cradId",cradId);
        photoMapper.testInsert(map);
    }
}
