package cn.mappyj.action;

import cn.mappyj.utils.LanguageUtil;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.reply.WatchdogStatsReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetWatchDogStatus extends AbstractGet{
    protected GetWatchDogStatus(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, theConsumer, arg);
    }

    @Override
    protected void execute() throws IOException, ExecutionException, InterruptedException {
        WatchdogStatsReply watchdog =  apiKey.getWatchdogStats().whenComplete(theConsumer).get();
        if(!watchdog.isSuccess()){
            CQ.sendGroupMsg(GroupID, LanguageUtil.CatchException);
            return;
        }
        StringBuilder msg = new StringBuilder();
        /*
        [Hypixel]WatchDogStatus:
        最近一分钟内封禁2名玩家
        今日被看门狗封禁的玩家有***位
        被管理团队封禁的玩家有***位
        迄今为止共有***名玩家被看门狗封禁
        共有***名玩家被管理团队封禁
        健康游戏 | 请勿开纪
         */
        msg.append("[Hypixel]WatchDogStatus:"
                + "\n" + "最近一分钟内封禁").append(watchdog.getWatchdogLastMinute()).append("名玩家")
                .append("\n").append("今日被看门狗封禁的玩家有").append(watchdog.getWatchdogRollingDaily()).append("位")
                .append("\n").append("被管理团队封禁的玩家有").append(watchdog.getStaffRollingDaily()).append("位")
                .append("\n").append("迄今为止共有").append(watchdog.getWatchdogTotal()).append("名玩家被看门狗封禁")
                .append("\n").append("共有").append(watchdog.getStaffTotal()).append("名玩家被管理团队封禁")
                .append("\n").append("==健康游戏 | 小号开纪==");
        CQ.sendGroupMsg(GroupID,msg.toString());
    }
}
