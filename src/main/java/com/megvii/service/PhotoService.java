package com.megvii.service;


import com.megvii.po.Photo;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface PhotoService {

    List<Photo>  findByPhotos(int begin, int end, String date);

    int selectPhotoCount(String date);

    Integer photoToLoca(Integer queryMaxSize) throws ParseException;

    Integer photoPartialToLoca(Integer beginNumber,Integer sizeNumber,String date,Integer topEndNumber);

    Integer XDBPhotoToLoac(Integer beginNumber,Integer sizeNumber,String date,Integer topEndNumber);

    byte [] findDataByCardId(String cardId);

    void shellImprotPhoto(String fileName,String shellPath,String shellConfigPath);


    void testInsert(String id,byte[] bytes,String cardId);

    String checkPhotoImport(Integer end);
}
