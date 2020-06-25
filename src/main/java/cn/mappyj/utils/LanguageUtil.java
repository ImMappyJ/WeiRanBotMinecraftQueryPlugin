package cn.mappyj.utils;

import java.io.*;
import java.util.Properties;

public class LanguageUtil {
    public static File languageFile = null;
    public static String Hypixel_InvalidName,Mojang_InvalidName,Hypixel_InvalidKey,CatchException,Wrong_Command,NoPermission,Unknown= "";

    public static boolean propload(String path) throws IOException, InterruptedException {
        Properties languageProp = new Properties();
        languageFile = new File(path);
        if(languageFile.exists()){
            languageProp.load(new FileReader(languageFile));
            Hypixel_InvalidName = languageProp.getProperty("Hypixel_InvalidName");
            Mojang_InvalidName = languageProp.getProperty("Hypixel_InvalidName");
            Hypixel_InvalidKey = languageProp.getProperty("Hypixel_InvalidKey");
            CatchException = languageProp.getProperty("CatchException");
            Wrong_Command = languageProp.getProperty("Wrong_Command");
            NoPermission = languageProp.getProperty("NoPermission");
            Unknown = languageProp.getProperty("Unknown");
            return true;
        }
        if(languageFile.createNewFile()){
            languageProp.setProperty("Hypixel_InvalidName","没有在服务器里找到这个玩家呐");
            languageProp.setProperty("Mojang_InvalidName","没有在Mojang找到这个玩家呐");
            languageProp.setProperty("Hypixel_InvalidKey","Key被榨得一滴都不剩辣");
            languageProp.setProperty("CatchException","啊咧,出错了!");
            languageProp.setProperty("Wrong_Command","啊这,指令都输错了→_→");
            languageProp.setProperty("NoPermission","无权限");
            languageProp.setProperty("Unknown","未知");
            languageProp.store(new FileWriter(languageFile),"MinecraftQueryPlugin LanguageHub V1.0");
            System.out.println("成功创建文件");
            propload(languageFile.getCanonicalPath());
        }
        return false;
    }
}
