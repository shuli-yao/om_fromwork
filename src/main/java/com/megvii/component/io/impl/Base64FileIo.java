package com.megvii.component.io.impl;

import com.megvii.component.io.FileIoAbstract;
import org.apache.tomcat.util.codec.binary.Base64;


/**
 * @ClassName Base64FileIo
 * @Description base64格式数据源io
 * @Author shuliyao
 * @CreateTime 2018/7/23 上午11:24
 */
public class Base64FileIo extends FileIoAbstract<String> {

    ByteFileIo fileIo = new ByteFileIo();

    @Override
    public boolean outputFile(String s, String localPath, String fileName) {
        //截取相应的前缀
        String [] base64s=s.split(",");
        byte [] bytes =null;
        if(base64s.length > 1){
            bytes = Base64.decodeBase64(base64s[1]);
        }else{
            bytes =Base64.decodeBase64(base64s[0]);
        }
        return fileIo.outputFile(bytes,localPath,fileName);

    }
}
