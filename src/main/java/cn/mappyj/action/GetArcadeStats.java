package cn.mappyj.action;

import com.google.gson.JsonElement;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.util.GameType;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetArcadeStats extends AbstractGetPlayerStatsInfo{
    String type;
    protected GetArcadeStats(long GroupID, CoolQ CQ, HypixelAPI apiKey, String arg, String type) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, arg, GameType.ARCADE.getDbName());
        this.type = type; //miniwalls farmhunt
        execute();
    }

    @Override
    protected void execute() {
        if(isnull(statsJson)||isnull(achievementJson)) return;
        JsonElement json_Coins = statsJson.get("coins");
        StringBuilder msg = new StringBuilder();
        int Coins = isnull(json_Coins)?0:json_Coins.getAsInt();
        switch (type){
            case"miniwalls":
                JsonElement json_Deaths,json_Kills,json_FinalKills,json_KillWitherCounts;
                json_Deaths = statsJson.get("deaths_mini_walls");
                json_Kills = statsJson.get("kills_mini_walls");
                json_FinalKills = statsJson.get("final_kills_mini_walls");
                json_KillWitherCounts = statsJson.get("wither_kills_mini_walls");

                int Deaths,Kills,FinalKills,KillWitherCounts;
                double KD;
                NumberFormat format = NumberFormat.getInstance();
                format.setMaximumFractionDigits(2);

                Deaths = isnull(json_Deaths)?0:json_Deaths.getAsInt();
                Kills = isnull(json_Kills)?0:json_Kills.getAsInt();
                FinalKills = isnull(json_FinalKills)?0:json_FinalKills.getAsInt();
                KillWitherCounts = isnull(json_KillWitherCounts)?0:json_KillWitherCounts.getAsInt();
                KD = Deaths == 0?Kills:(float)Kills/(float)Deaths;
                /*
                [Hypixel]已查询到Player的街机信息:
                硬币:22222  击杀凋零:1
                击杀:2  死亡:3  KD:2/3
                 */
                msg.append("[Hypixel]已查询到").append(playerName).append("的迷你战墙战绩:")
                        .append("\n").append("击杀凋零:").append(KillWitherCounts).append("  ").append("最终击杀:").append(FinalKills)
                        .append("\n").append("击杀:").append(Kills).append("  ").append("死亡:").append(Deaths).append("  ").append("KD:").append(format.format(KD));

                CQ.sendGroupMsg(GroupID,msg.toString());
                return;
            case"farmhunt":
                JsonElement json_Win,json_PoopCollect;
                json_Win = statsJson.get("wins_farm_hunt");
                json_PoopCollect = statsJson.get("poop_collected");

                int Win,PoopCollect;

                Win = isnull(json_Win)?0:json_Win.getAsInt();
                PoopCollect = isnull(json_PoopCollect)?0:json_PoopCollect.getAsInt();
                /*
                [Hypixel]已查询到Player的街机信息:
                硬币:22222  收集粑粑1个
                共获胜**局
                 */
                msg.append("[Hypixel]已查询到").append(playerName).append("的躲猫猫战绩:")
                        .append("\n").append("收集粑粑:").append(PoopCollect).append("  ").append("胜利:").append(Win);

                CQ.sendGroupMsg(GroupID,msg.toString());
                return;
            case"simonsays":
                JsonElement json_SimonRounds,json_SimonWin;
                json_SimonRounds = statsJson.get("rounds_simon_says");
                json_SimonWin = statsJson.get("wins_simon_says");

                int SimonRounds,SimonWin;

                SimonRounds = isnull(json_SimonRounds)?0:json_SimonRounds.getAsInt();
                SimonWin = isnull(json_SimonWin)?0:json_SimonWin.getAsInt();
                /*
                [Hypixel]已查询到Player的街机信息:
                你说我做共进行133局  共上榜16次
                 */
                msg.append("[Hypixel]已查询到").append(playerName).append("的我说你做战绩:")
                        .append("\n").append("局数:").append(SimonRounds).append("  ").append("榜一次数:").append(SimonWin);
                CQ.sendGroupMsg(GroupID,msg.toString());
                return;
            default:
                msg.append("[Hypixel]已查询到").append(playerName).append("的街机信息:").append("街机硬币:").append(Coins);
                CQ.sendGroupMsg(GroupID, msg.toString());
        }
    }
}
