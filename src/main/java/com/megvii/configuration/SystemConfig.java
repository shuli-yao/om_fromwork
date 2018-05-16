package com.megvii.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Data
public class SystemConfig {

    Boolean TimerOnOff = false;

    @Value("${query.data.number}")
    Integer queryMaxSize;

    @Value("${shell.path}")
    String shellPath;

    @Value("${shell.config.path}")
    String shellConfigPath;

    @Value("${shell.improt.file.name}")
    String improtShellName;

    @Value("${shell.clear.file.name}")
    String clearShellName;

    @Value("${continuingly.text.path}")
    String textPaht;

    String textFileName = "continuingly.txt";

    String filePath;

    @Value("${photo.download.file.suffix}")
    String fileSuffix;

   public String getTextFilePaht(){
        return textPaht+textFileName;
    }

    public void setJobDownloadFilePath(String jobPhotoFilePath){
       filePath= jobPhotoFilePath+"job_photo_img";
    }


    //定时器执行状态，防止重复执行，true执行完毕 false正在执行
    boolean jobExecutionStatus = true;

   //增量执行时定时器入库完成的图像是否删除
    boolean jobPhotoDelete = true;

}
