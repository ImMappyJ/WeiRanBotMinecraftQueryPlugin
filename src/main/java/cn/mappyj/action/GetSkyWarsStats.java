package cn.mappyj.action;

import cn.mappyj.utils.LanguageUtil;
import com.google.gson.JsonElement;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.util.GameType;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetSkyWarsStats extends AbstractGetPlayerStatsInfo{
    protected GetSkyWarsStats(long GroupID, CoolQ CQ, HypixelAPI apiKey, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, arg, GameType.SKYWARS.getDbName());
        execute();
    }

    @Override
    protected void execute() {
        if(isnull(statsJson) || isnull(achievementJson)) return;
        JsonElement json_Level,json_Souls,json_Kills,json_Deaths,json_GatherSouls,json_Coins,json_Assists,json_LastMode;
        json_Level = statsJson.get("levelFormatted");
        json_Assists = statsJson.get("assists");
        json_Coins = statsJson.get("coins");
        json_Kills = statsJson.get("kills");
        json_Deaths = statsJson.get("deaths");
        json_Souls = statsJson.get("souls");
        json_GatherSouls = statsJson.get("souls_gathered");
        json_LastMode = statsJson.get("lastMode");

        int Assists,Coins,Kills,Deaths,Souls,Souls_Gathered;
        String LastMode,Level;
        double KD;
        StringBuilder msg = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);

        Assists = isnull(json_Assists)?0:json_Assists.getAsInt();
        Coins = isnull(json_Coins)?0:json_Coins.getAsInt();
        Kills = isnull(json_Kills)?0:json_Kills.getAsInt();
        Deaths = isnull(json_Deaths)?0:json_Deaths.getAsInt();
        Souls = isnull(json_Souls)?0:json_Souls.getAsInt();
        Souls_Gathered = isnull(json_GatherSouls)?0:json_GatherSouls.getAsInt();
        Level = isnull(json_Level)?"0⋆":json_Level.getAsString().substring(2);
        LastMode = isnull(json_LastMode)? LanguageUtil.Unknown:json_LastMode.getAsString();
        KD = Deaths == 0?Kills:(float)Kills/(float)Deaths;
        /*
        [Hypixel]已查询到Player的空战信息:
        等级:1⋆  金币:99999  助攻:111
        击杀:111  死亡:111  KD:1.00
        最近匹配模式:TEAM
        共收割8个灵魂  现拥有9个灵魂
         */
        msg.append("[Hypixel]已查询到").append(playerName).append("的空战战绩:\n")
                .append("等级:").append(Level).append("  ").append("金币:").append(Coins).append("  ").append("助攻:").append(Assists)
                .append("\n").append("击杀:").append(Kills).append("  ").append("死亡:").append(Deaths).append("  ").append("KD:").append(format.format(KD))
                .append("\n").append("最近匹配模式:").append(LastMode)
                .append("\n").append("共收割").append(Souls_Gathered).append("个灵魂").append("  ").append("现拥有").append(Souls).append("个灵魂");
        CQ.sendGroupMsg(GroupID,msg.toString());
    }
}