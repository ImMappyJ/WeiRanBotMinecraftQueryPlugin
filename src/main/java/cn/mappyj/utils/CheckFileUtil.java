package cn.mappyj.utils;

import java.io.File;
import java.io.IOException;

public class CheckFileUtil {
    public boolean checkFile(File file){
        if(file.exists()){
            return true;
        }
        try {
            if(!file.createNewFile()){
                System.out.println(LanguageUtil.CatchException);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
