package com.megvii.service;


import com.megvii.po.Photo;

import java.util.List;

public interface PhotoService {

    List<Photo>  findByPhotos(int begin,int end);

    int selectPhotoCount();

    void photoToLoca(Integer queryMaxSize);

    void shellImprotPhoto(String fileName,String shellPath,String shellConfigPath);


    void testInsert(String id,byte[] bytes,String cardId);
}
