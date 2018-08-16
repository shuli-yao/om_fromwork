package com.megvii.component.io;

import com.megvii.component.io.FileIo;

import java.io.File;

/**
 * @ClassName FileIoAbstract
 * @Description 文件下载抽象类
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午6:25
 */
public abstract class FileIoAbstract<T> implements FileIo<T> {



    /**
     * 检查文件夹是否存在
     * @param filePath
     * @return
     */
    public boolean isPathExit(String filePath){
        File file =new File(filePath);
        if(!file.isDirectory()){
            file.mkdirs();
        }
        return true;
    }
}
