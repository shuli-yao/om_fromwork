package com.megvii.utlis;

import jdk.internal.util.xml.impl.Input;

import java.io.*;

/**
 *
 *  txt操作
 */
public class TextUtils {

    /**
     * 写入text工具方法,追加形式
     * @param textPath
     * @param context
     * @return
     */
    public boolean writerText(String textPath,String context,boolean isAppend){

        FileWriter fileWriter = null;

        BufferedWriter bufferedWriter =null;
        try {
            fileWriter = new FileWriter(textPath,isAppend);
            bufferedWriter = new BufferedWriter(fileWriter);
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

    public String readerText(String textPath){
        FileReader fileReader = null;

        BufferedReader bufferedReader =null;
        try {
            fileReader = new FileReader(textPath);
            bufferedReader = new BufferedReader(fileReader);
            String count = "";
            while (bufferedReader.read() !=-1){
                count+="\n"+bufferedReader.readLine();
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

    public String readerOneRowText(String textPath){
        FileReader fileReader = null;

        BufferedReader bufferedReader =null;
        try {
            fileReader = new FileReader(textPath);
            bufferedReader = new BufferedReader(fileReader);
            String count = bufferedReader.readLine();

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


}
