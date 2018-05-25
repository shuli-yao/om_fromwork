package com.megvii.dbsource.oracle.mapper;

import com.megvii.po.Data;
import com.megvii.po.Photo;

import java.util.Date;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface PhotoMapper {

    List<Photo> selectPhotoByPage(int begin, int end, String date,@Param("tableNaem")String tableNaem);

    List<Photo> selectPhotoByPageNoOrder(int begin, int end, String date,@Param("tableNaem")String tableNaem);

    int selectPhotoCount(@Param("date") String date,@Param("tableNaem")String tableNaem);

    List<Data> selectPhotoByCardId(@Param("cardId") String cardId, @Param("tableNaem")String tableNaem);

    void testInsert(Map<String,Object> map);
}

