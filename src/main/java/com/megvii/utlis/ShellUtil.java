package com.megvii.utlis;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ShellUtil {

  private  static BufferedReader bufrIn = null;
  private static  BufferedReader bufrError = null;
  private static    StringBuilder result = new StringBuilder();

    public String execute(String shpath,String parameter){
        try {
            log.info("开始执行");
            Process process =null;
            String command1 = "sh "+ shpath+" "+parameter;
            System.out.println(command1);
            process = Runtime.getRuntime().exec(command1);
            process.waitFor();
        } catch (IOException e) {
            log.error("执行失败:"+e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.error("执行失败:"+e);
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * 执行系统命令, 返回执行结果
     *
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static String execCmd(String cmd, File dir) throws Exception {

        result.delete(0,result.length());
        Process process = null;

        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String line = null;
                    int rc=0;
                    try {
                        while (true) {
                            if(rc>2){
                                break;
                            }
                            if((line= bufrIn.readLine()) == null){
                                Thread.sleep(5000);
                                rc++;
                                continue;
                            }

                            log.info("输出流正常执行:"+line);
                            result.append(line).append("\n");
//                            Thread.sleep(500);
                        }
                    }catch (Exception e){

                    }


                }
            });

            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    String line = null;
                    try {
                        int rc=0;
                        while (true) {
                            if(rc>2){
                                break;
                            }
                            if((line= bufrError.readLine()) == null){
                                Thread.sleep(5000);
                                rc++;
                                continue;
                            }
                            log.info("异常流正常执行:"+line);
//                            Thread.sleep(500);
                        }
                    }catch (Exception e){

                    }
                }
            });
            thread.start();
            thread2.start();
            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }

        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }



}
