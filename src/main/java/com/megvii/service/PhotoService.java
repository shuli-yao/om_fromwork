package com.megvii.service;


import com.megvii.po.Photo;
import oracle.jdbc.driver.DatabaseError;

import java.util.Date;
import java.util.List;

public interface PhotoService {

    List<Photo>  findByPhotos(int begin, int end, String date);

    int selectPhotoCount(String date);

    Integer photoToLoca(Integer queryMaxSize);

    void shellImprotPhoto(String fileName,String shellPath,String shellConfigPath);


    void testInsert(String id,byte[] bytes,String cardId);
}
