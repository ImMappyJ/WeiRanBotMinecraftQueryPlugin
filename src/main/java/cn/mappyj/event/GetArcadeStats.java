package cn.mappyj.event;

import cn.mappyj.utils.LanguageUtil;
import com.google.gson.JsonElement;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetArcadeStats extends AbstractGetPlayerStatsInfo{
    String type;
    protected GetArcadeStats(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg, String type) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, theConsumer, arg, "Arcade");
        this.type = type; //miniwalls farmhunt
        Arcadeexecute();
    }

    protected void Arcadeexecute() {
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
                msg.append("[Hypixel]已查询到").append(playerName).append("的街机信息:")
                        .append("\n").append("硬币:").append(Coins).append("  ").append("击杀凋零:").append(KillWitherCounts)
                        .append("\n").append("击杀:").append(Kills).append("  ").append("死亡:").append(Deaths).append("  ").append("KD:").append(format.format(KD));

                CQ.sendGroupMsg(GroupID,msg.toString());
                return;
            case"farmhunt":
                JsonElement json_SeekerWin,json_HunterWin,json_PoopCollect;
                json_SeekerWin = statsJson.get("seeker_wins_hide_and_seek");
                json_HunterWin = statsJson.get("wins_farm_hunt");
                json_PoopCollect = statsJson.get("poop_collected");

                int SeekerWin,HunterWin,PoopCollect;

                SeekerWin = isnull(json_SeekerWin)?0:json_SeekerWin.getAsInt();
                HunterWin = isnull(json_HunterWin)?0:json_HunterWin.getAsInt();
                PoopCollect = isnull(json_PoopCollect)?0:json_PoopCollect.getAsInt();
                /*
                [Hypixel]已查询到Player的街机信息:
                硬币:22222  收集粑粑1个
                动物获胜1局 农夫获胜1局
                 */
                msg.append("[Hypixel]已查询到").append(playerName).append("的街机信息:")
                        .append("\n").append("硬币:").append(Coins).append("  ").append("收集粑粑").append(PoopCollect).append("个")
                        .append("\n").append("动物获胜").append(SeekerWin).append("局").append("  ").append("农夫获胜").append(HunterWin).append("局");

                CQ.sendGroupMsg(GroupID,msg.toString());
                return;
            default:
                CQ.sendGroupMsg(GroupID, LanguageUtil.Wrong_Command);
        }
    }
}
