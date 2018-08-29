package com.megvii.utlis;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  txt操作工具类
 *  @author shuli.yao
 */
public class TextUtils {

    /**
     * 写入text工具方法,追加形式
     * @param textPath
     * @param context
     * @return
     */
    public synchronized boolean writerText(String textPath,String context,boolean isAppend){

        FileWriter fileWriter = null;

        BufferedWriter bufferedWriter =null;
        try {
            String s = readerOneRowText(textPath);
            fileWriter = new FileWriter(textPath,isAppend);
            bufferedWriter = new BufferedWriter(fileWriter);
            if(isAppend){
                if(s!=null|| !"".equals(s)){
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.write(context);
            bufferedWriter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedWriter !=null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    /**
     * 读取txt中内容
     * @param textPath
     * @return
     */
    public List<String> readerText(String textPath){

        List<String> resultList = new ArrayList<>();
        FileReader fileReader = null;

        BufferedReader bufferedReader =null;
        try {
            fileReader = new FileReader(textPath);
            bufferedReader = new BufferedReader(fileReader);

            String context = "";
            while ((context=bufferedReader.readLine())!=null){
                resultList.add(context);
            }
            return resultList;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader !=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return resultList;
    }

    /**
     * 读取txt中内容仅读取一行
     * @param textPath
     * @return
     */
    public String readerOneRowText(String textPath){
        FileReader fileReader = null;

        BufferedReader bufferedReader =null;
        try {
            fileReader = new FileReader(textPath);
            bufferedReader = new BufferedReader(fileReader);
            String count ="";
            int t =0;
            while(true){
                t++;
                count = bufferedReader.readLine();
                if(t>=10 || !"".equals(count)){
                    System.out.println("结束");
                    break;
                }
            }
            return count;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader !=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return "";
    }

    /**
     * copy及修改txt中内容
     * @param filePath
     * @param rowNumber
     * @param replaceContext
     * @return
     */
    public  Boolean copyChangeTextContext(String filePath, Integer rowNumber, String replaceContext){
        FileReader fileReader = null;

        BufferedReader bufferedReader =null;

        PrintWriter out= null;
        String [] filePaths = filePath.split("\\.");
        if(filePaths.length <= 1){
            return false;
        }
       String fileJobPath = filePaths [0]+"_job."+filePaths[1];

        try {
            out= new PrintWriter(new BufferedWriter(new FileWriter(fileJobPath)));
            InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
            bufferedReader = new BufferedReader(isr);
            String context = "";
            Integer number = 0;
            while ((context=bufferedReader.readLine())!=null){
                number++;
                if(number == rowNumber){
                    out.println(replaceContext);
                }else {
                    out.println(context);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader !=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out !=null){
                out.close();
            }

        }
        return true;
    }
}
