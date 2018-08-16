package com.megvii.component.io.impl;

import com.megvii.component.io.FileIoAbstract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @ClassName HttpFileIo
 * @Description 通过网络下载文件
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午6:29
 */
public class HttpFileIo extends FileIoAbstract<String> {



    /**
     * 读取网络图片，转换成byte数组.
     *
     * @param s 网络图片地址
     */
    @Override
    public boolean outputFile(String s, String localPath, String fileName) {
        URL url = null;// 构造URL
        ByteArrayOutputStream outputStream = null;
        try {
            url = new URL(s);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");// 设置请求方式为"GET"
            con.setConnectTimeout(5 * 1000);// 超时响应时间为5秒

            InputStream is = con.getInputStream();// 输入流
            byte[] bs = new byte[1024];// 1K的数据缓冲
            int len = 0;// 读取到的数据长度

                outputStream = new ByteArrayOutputStream();
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                outputStream.write(bs, 0, len);
            }

            is.close();

            byte [] fileByte =outputStream.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
