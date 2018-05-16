package com.megvii.utlis;

import java.io.File;

public class FileIoUtils
{
    public String deleteFile(String path){
        int fileNumebr = 0;
        int failureNumber =0;
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {

            if (tempList[i].isFile()) {
                fileNumebr++;
                try {
                    tempList[i].delete();
                }catch (Exception e){
                    failureNumber++;
                }

            }
        }
        return "删除图像数量:"+fileNumebr+"，失败数量:"+failureNumber;
    }

}
