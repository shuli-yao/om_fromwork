package com.megvii.component.io;

/**
 * @ClassName FileIo
 * @Description 文件IO规范接口
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午6:16
 */
public interface FileIo<T> {
    public boolean outputFile(T t,String localPath,String fileName);
}
