package com.megvii.utlis;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 处理文件下载类
 * @author shuli.yao
 */

public class FileDownload {
    /**
     * 读取网络图片，转换成byte数组.
     *
     * @param imgUrl 网络图片地址
     */
    public byte[] getImgNetworkUrlToByte(String imgUrl) throws Exception {
        URL url = new URL(imgUrl);// 构造URL
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");// 设置请求方式为"GET"
        con.setConnectTimeout(5 * 1000);// 超时响应时间为5秒

        InputStream is = con.getInputStream();// 输入流
        byte[] bs = new byte[1024];// 1K的数据缓冲
        int len = 0;// 读取到的数据长度

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            outputStream.write(bs, 0, len);
        }

        is.close();
        
        byte [] fileByte =outputStream.toByteArray();
        outputStream.close();
        return fileByte;

    }

    /**
     * 将下载好的文件，写入本地
     * @param bytes
     * @param fileName
     * @return
     */
    public   boolean outputFile(byte [] bytes,String fileName,String filePath) {
        BufferedOutputStream bufferedOutputStream = null;
        OutputStream outputStream =null;
        try {
            isPathExit(filePath);
            if(new File(filePath+File.separator+fileName).exists()){
                return true;
            }
            outputStream = new FileOutputStream(filePath+File.separator+fileName);
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

    public boolean isPathExit(String filePath){
        File file =new File(filePath);
        if(!file.isDirectory()){
            file.mkdirs();
        }
        return true;
    }
}
