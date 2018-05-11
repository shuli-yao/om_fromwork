package com.megvii.dbsource.oracle.mapper;

import com.megvii.po.Photo;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhotoMapper {

    List<Photo> selectPhotoByPage(int begin,int end);

    int selectPhotoCount();

    void testInsert(Map<String,Object> map);
}

