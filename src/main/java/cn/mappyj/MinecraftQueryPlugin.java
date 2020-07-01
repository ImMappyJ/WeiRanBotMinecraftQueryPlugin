package cn.mappyj;


import cn.mappyj.action.EventExecutor;
import cn.mappyj.utils.LanguageUtil;
import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.entity.IAuth;
import org.meowy.cqp.jcq.entity.ICQVer;
import org.meowy.cqp.jcq.entity.IRequest;
import org.meowy.cqp.jcq.event.JcqAppAbstract;

import java.io.File;
import java.io.IOException;

public class MinecraftQueryPlugin extends JcqAppAbstract implements ICQVer, IAuth, IRequest {
    public static String AppDirectory;
    public MinecraftQueryPlugin(){ }

    public MinecraftQueryPlugin(CoolQ CQ){
        super(CQ);
    }

    public static void main(String[] args){
        MinecraftQueryPlugin minecraftQueryPlugin = new MinecraftQueryPlugin();
        CoolQ CQ = minecraftQueryPlugin.getCoolQ();
        minecraftQueryPlugin.startup();
        minecraftQueryPlugin.enable();
        minecraftQueryPlugin.disable();
        minecraftQueryPlugin.exit();
    }

    public String appInfo() {
        String appid = "cn.mappyj.minecraftqueryplugin";
        return CQAPIVER+","+appid;
    }

    public int startup() {
        appDirectory = CQ.getAppDirectory();
        return 0;
    }

    public int exit() {
        return 0;
    }

    public int enable() {
        AppDirectory = appDirectory;
        try {
            if(!LanguageUtil.propload(appDirectory+File.separator+"language.properties")){
                CQ.logInfo("加载失败",new IOException("Wrong load"));
            };
        } catch (IOException | InterruptedException e) {
            CQ.logInfo("啊咧 创建文件时出错了",e);
        }
        enable = true;
        return 0;
    }

    public int disable() {
        enable = false;
        return 0;
    }

    public int privateMsg(int i, int i1, long l, String s, int i2) {
        return 0;
    }

    public int groupMsg(int subType, int msgId, long fromGroup, long fromQQ, String fromAnonymous, String msg, int font) {
        Thread executeCmd = new EventExecutor(fromGroup,msg,CQ);
        executeCmd.start();
        try {
            executeCmd.join();
        } catch (InterruptedException e) {
            CQ.sendGroupMsg(fromGroup,LanguageUtil.CatchException);
            e.printStackTrace();
        }
        return 0;
    }

    public int discussMsg(int i, int i1, long l, long l1, String s, int i2) {
        return 0;
    }

    public int groupUpload(int i, int i1, long l, long l1, String s) {
        return 0;
    }

    public int groupAdmin(int i, int i1, long l, long l1) {
        return 0;
    }

    public int groupMemberDecrease(int i, int i1, long l, long l1, long l2) {
        return 0;
    }

    public int groupMemberIncrease(int i, int i1, long l, long l1, long l2) {
        return 0;
    }

    public int friendAdd(int i, int i1, long l) {
        return 0;
    }

    public int requestAddFriend(int i, int i1, long l, String s, String s1) {
        return 0;
    }

    public int requestAddGroup(int i, int i1, long l, long l1, String s, String s1) {
        return 0;
    }
}
