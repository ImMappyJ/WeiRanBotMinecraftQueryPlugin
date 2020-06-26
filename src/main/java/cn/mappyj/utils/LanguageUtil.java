package cn.mappyj.utils;

import java.io.*;
import java.util.Properties;

public class LanguageUtil {
    public static File languageFile = null;
    public static String Hypixel_InvalidName,Mojang_InvalidName,Hypixel_InvalidKey,CatchException,Wrong_Command,NoPermission,Unknown
            ,CantGetGameStats= "";

    public static boolean propload(String path) throws IOException, InterruptedException {
        Properties languageProp = new Properties();
        languageFile = new File(path);
        if(languageFile.exists()){
            languageProp.load(new FileReader(languageFile));
            Hypixel_InvalidName = languageProp.getProperty("Hypixel_InvalidName","没有在服务器里找到这个玩家呐");
            Mojang_InvalidName = languageProp.getProperty("Mojang_InvalidName","没有在Mojang找到这个玩家呐");
            Hypixel_InvalidKey = languageProp.getProperty("Hypixel_InvalidKey","Key被榨得一滴都不剩辣");
            CatchException = languageProp.getProperty("CatchException","啊咧,出错了!");
            Wrong_Command = languageProp.getProperty("Wrong_Command","啊这,指令都输错了→_→\n帮助:https://5imc.github.io/WeiRanBot/");
            NoPermission = languageProp.getProperty("NoPermission","无权限");
            Unknown = languageProp.getProperty("Unknown","未知");
            CantGetGameStats = languageProp.getProperty("CantGetGameStats","无法获取该玩家信息");
            return true;
        }
        if(languageFile.createNewFile()){
            languageProp.setProperty("Hypixel_InvalidName","没有在服务器里找到这个玩家呐");
            languageProp.setProperty("Mojang_InvalidName","没有在Mojang找到这个玩家呐");
            languageProp.setProperty("Hypixel_InvalidKey","Key被榨得一滴都不剩辣");
            languageProp.setProperty("CatchException","啊咧,出错了!");
            languageProp.setProperty("Wrong_Command","啊这,指令都输错了→_→\n帮助:https://5imc.github.io/WeiRanBot/");
            languageProp.setProperty("NoPermission","无权限");
            languageProp.setProperty("Unknown","未知");
            languageProp.setProperty("CantGetGameStats","无法获取该玩家信息");
            languageProp.store(new FileWriter(languageFile),"MinecraftQueryPlugin LanguageHub V1.0");
            System.out.println("成功创建文件");
            propload(languageFile.getCanonicalPath());
        }
        return false;
    }
}
