package com.megvii.component.run.impl;

import com.megvii.component.io.bean.FileIoObject;
import com.megvii.component.io.FileIo;
import com.megvii.component.run.RunService;

/**
 * @ClassName FileIoRunImpl
 * @Description 文件打开下载所使用run方法
 * @Author shuliyao
 * @CreateTime 2018/7/23 下午3:53
 */
public class FileIoRunImpl implements RunService{

    FileIoObject fileObject = null;

    FileIo fileIo = null;

    /**
     *  文件下载用
     * @param fileObject
     * @param fileIo
     */
    public FileIoRunImpl(FileIoObject fileObject, FileIo fileIo){
        this.fileObject = fileObject;
        this.fileIo = fileIo;
    }


    @Override
    public void run() {
        String fileName = fileObject.getFileName()+"."+fileObject.getFilesSuffix();
        Object fileValue = fileObject.getFileValue();
        String filePath =fileObject.getFilePaht();
        fileIo.outputFile(fileValue,filePath,fileName);
    }



}
