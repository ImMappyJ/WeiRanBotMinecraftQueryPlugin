package cn.mappyj.action;

import cn.mappyj.utils.LanguageUtil;
import cn.mappyj.utils.MojangCastUtil;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetHistoryName extends AbstractGet{
    protected GetHistoryName(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, theConsumer, arg);
    }

    @Override
    protected void execute() throws IOException {
        MojangCastUtil castUtil = new MojangCastUtil();
        String[] names = castUtil.stringUUIDtoNameHistory(castUtil.nametoStringUUID(arg));
        StringBuilder namesList = new StringBuilder();
        StringBuilder msg = new StringBuilder();
        final int MaxNamesCount = 7;
        if(isnull(names)){
            CQ.sendGroupMsg(GroupID, LanguageUtil.Mojang_InvalidName);
            return;
        }
        if(names.length>MaxNamesCount){
            for(int t = 1;t<=MaxNamesCount;t++) namesList.append(t).append(".").append(names[t - 1]).append("\n");
            namesList.append("more info on:https://zh-cn.namemc.com/search?q=").append(arg);
        }else{
            for(int t=1;t<=names.length;t++) namesList.append(t).append(".").append(names[t-1]).append("\n");
        }
        msg.append("查询到").append(arg).append("的历史ID:\n").append(namesList.toString());
        CQ.sendGroupMsg(GroupID,msg.toString());
    }
}
