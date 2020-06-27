package cn.mappyj.action;

import cn.mappyj.utils.LanguageUtil;
import com.google.gson.JsonElement;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.util.GameType;
import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.entity.Group;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetUHCStats extends AbstractGetPlayerStatsInfo{

    protected GetUHCStats(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, theConsumer, arg, GameType.UHC.getDbName());
    }

    @Override
    protected void execute() {
        if(isnull(statsJson)||isnull(achievementJson)) return;
        JsonElement json_Kit,json_Coins,json_Scores,json_TeamDeaths,json_TeamKills,json_DuoDeaths,json_DuoKills,json_SoloDeaths,json_SoloKills;
        json_Coins = statsJson.get("coins");
        json_Kit = statsJson.get("equippedKit");
        json_Scores = statsJson.get("score");
        json_TeamKills = statsJson.get("kills");
        json_TeamDeaths = statsJson.get("deaths");
        json_SoloKills = statsJson.get("kills_solo");
        json_SoloDeaths = statsJson.get("deaths_solo");

        int Coins,Scores,TeamKills,TeamDeaths,SoloKills,SoloDeaths,Kills,Deaths;
        String Kit;
        double SoloKD,TeamKD,KD;
        StringBuilder msg = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);

        Coins = isnull(json_Coins)?0:json_Coins.getAsInt();
        Kit = isnull(json_Kit)? LanguageUtil.Unknown:json_Kit.getAsString();
        Scores = isnull(json_Scores)?0:json_Scores.getAsInt();
        TeamKills = isnull(json_TeamKills)?0:json_TeamKills.getAsInt();
        TeamDeaths = isnull(json_TeamDeaths)?0:json_TeamDeaths.getAsInt();
        SoloKills = isnull(json_SoloKills)?0:json_SoloKills.getAsInt();
        SoloDeaths = isnull(json_SoloDeaths)?0:json_SoloDeaths.getAsInt();
        Kills = TeamKills+SoloKills;
        Deaths = TeamDeaths+SoloDeaths;
        SoloKD = SoloDeaths == 0?SoloKills:(float)SoloKills/(float)SoloDeaths;
        TeamKD = TeamDeaths == 0?TeamKills:(float)TeamKills/(float)TeamDeaths;
        KD = Deaths == 0?Kills:(float)Kills/(float)Deaths;
        /*
        [Hypixel]已查询到Player的UHC信息:
        积分:22222  硬币:22222
        击杀:4  死亡:4  KD:1
        个人模式:
        击杀:2  死亡:2  KD:1
        组队模式:
        击杀:2  死亡:2  KD:1
         */
        msg.append("[Hypixel]已查询到").append(playerName).append("的UHC信息:")
                .append("\n").append("积分:").append(Scores).append("  ").append("硬币:").append(Coins)
                .append("\n").append("击杀:").append(Kills).append("  ").append("死亡:").append(Deaths).append("  ").append("KD:").append(format.format(KD))
                .append("\n").append("个人模式:")
                .append("\n").append("击杀:").append(SoloKills).append("  ").append("死亡:").append(SoloDeaths).append("  ").append("KD:").append(format.format(SoloKD))
                .append("\n").append("组队模式:")
                .append("\n").append("击杀:").append(TeamKills).append("  ").append("死亡:").append(TeamDeaths).append("  ").append("KD:").append(format.format(TeamKD));
        CQ.sendGroupMsg(GroupID,msg.toString());
    }
}
