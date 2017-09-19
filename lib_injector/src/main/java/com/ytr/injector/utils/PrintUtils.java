package com.ytr.injector.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by YTR on 2017/8/31.
 */

public class PrintUtils {
    public static void printInfo(String fileName,String info){
        if(info == null){
            return;
        }
        try {
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
            bw.newLine();
            bw.append(info);
            bw.flush();
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
