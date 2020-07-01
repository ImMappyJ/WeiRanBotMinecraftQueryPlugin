package cn.mappyj.action;

import cn.mappyj.action.api.AddGroupAPI;
import cn.mappyj.utils.HypixelApiKeyUtil;
import cn.mappyj.utils.LanguageUtil;
import net.hypixel.api.HypixelAPI;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivateEventExecutor extends Thread{
    private final CoolQ CQ;
    private final String msg;
    private final long QQID;

    public PrivateEventExecutor(long QQID, String msg, CoolQ CQ){
        this.CQ = CQ;
        this.msg = msg;
        this.QQID = QQID;
    }

    public void run(){
        Pattern pattern = Pattern.compile("/(api)\\s?(\\w+|)?\\s?(\\w+|)?\\s?(\\w+|\\w{8}(-\\w{4}){3}-\\w{12}|)?\\s?(\\w+)?");
        Matcher matcher = pattern.matcher(msg);
        if(matcher.matches()){
            try {
                if(!commandChecker(matcher)){
                    CQ.sendPrivateMsg(QQID, LanguageUtil.Wrong_Command);
                }
            } catch (IOException e) {
                CQ.sendPrivateMsg(QQID,LanguageUtil.CatchException);
                CQ.logInfo("",e);
            }
        }
    }

    private boolean commandChecker(Matcher matcher) throws IOException {
        switch (matcher.group(2).toLowerCase()){
            case"give":
                if(matcher.group(4).matches("\\w{8}(-\\w{4}){3}-\\w{12}")&&matcher.group(3).matches("[0-9]+")){new AddGroupAPI(CQ,QQID,Long.parseLong(matcher.group(3)),matcher.group(4));}else{CQ.sendPrivateMsg(QQID,"请输入一个规范的UUID或群号");}
                return true;
            default:return false;
        }
    }
}
