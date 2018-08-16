package com.megvii.component.io.bean;

import lombok.Data;

/**
 * @ClassName FileObject
 * @Description 下载文件对象.
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午5:53
 */
@Data
public class FileIoObject {

    private String fileName;    //文件名
    private String filesSuffix; //文件类型
    private String filePaht;    //文件下载地址
    private Object fileValue;   //文件


    public FileIoObject(String fileName,String filesSuffix,String filePath,Object fileValue){
        this.fileName = fileName;
        this.filesSuffix = filesSuffix;
        this.fileValue = fileValue;
        this.filePaht = filePath;
    }


}

