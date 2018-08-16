package com.megvii.component.io.impl;

import com.megvii.component.io.FileIoAbstract;

import java.io.*;

/**
 * @ClassName ByteFileIo
 * @Description byte类型数据io
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午6:19
 */
public class ByteFileIo extends FileIoAbstract<byte[]> {


    @Override
    public boolean outputFile(byte[] bytes, String localPath, String fileName) {
        BufferedOutputStream bufferedOutputStream = null;
        OutputStream outputStream =null;
        try {
            super.isPathExit(localPath);
            if(new File(localPath+File.separator+fileName).exists()){
                return true;
            }
            outputStream = new FileOutputStream(localPath+File.separator+fileName);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(bytes,0,bytes.length);

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedOutputStream!=null){
                try {
                    bufferedOutputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if(outputStream!=null){
                    outputStream.close();
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }
}
