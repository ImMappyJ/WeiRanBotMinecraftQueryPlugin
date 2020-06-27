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

public class GetBedWarsStats extends AbstractGetPlayerStatsInfo{
    protected GetBedWarsStats(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, theConsumer, arg, GameType.BEDWARS.getDbName());
    }

    @Override
    protected void execute(){
        if(isnull(super.statsJson) || isnull(super.achievementJson)) return;
        JsonElement json_Level,json_BreakBedCounts,json_CommonKillCounts,json_LossesBedCounts,json_CommonDieCounts,json_FinalKillCounts,json_FinalDieCounts,json_ResourcesCollectCounts,json_Coins,json_Win,json_Losses,json_LootBox;
        json_Level = super.achievementJson.get("bedwars_level");
        json_Coins = super.statsJson.get("coins");
        json_BreakBedCounts = super.statsJson.get("beds_broken_bedwars");
        json_LossesBedCounts = super.statsJson.get("beds_lost_bedwars");
        json_CommonKillCounts = super.statsJson.get("kills_bedwars");
        json_CommonDieCounts = super.statsJson.get("deaths_bedwars");
        json_FinalKillCounts = super.statsJson.get("final_kills_bedwars");
        json_FinalDieCounts = super.statsJson.get("final_deaths_bedwars");
        json_Win = super.statsJson.get("wins_bedwars");
        json_Losses = super.statsJson.get("losses_bedwars");
        json_ResourcesCollectCounts = super.statsJson.get("resources_collected_bedwars");
        json_LootBox = super.achievementJson.get("bedwars_loot_box");

        int Level,BreakBedCounts,CommonKillCounts,FinalKillCounts,ResourcesCollectCounts,Coins,LossesBedCounts,CommonDieCounts,FinalDieCounts,Win,Lose,LootBoxCounts;
        double Final_KD,Common_KD,Bed_BL,Self_WL;
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        StringBuilder msg = new StringBuilder();

        Level = isnull(json_Level)?0:json_Level.getAsInt();
        Coins = isnull(json_Coins)?0:json_Coins.getAsInt();
        BreakBedCounts = isnull(json_BreakBedCounts)?0:json_BreakBedCounts.getAsInt();
        LossesBedCounts = isnull(json_LossesBedCounts)?0:json_LossesBedCounts.getAsInt();
        CommonKillCounts = isnull(json_CommonKillCounts)?0:json_CommonKillCounts.getAsInt();
        CommonDieCounts = isnull(json_CommonDieCounts)?0:json_CommonDieCounts.getAsInt();
        FinalDieCounts = isnull(json_FinalDieCounts)?0:json_FinalDieCounts.getAsInt();
        FinalKillCounts = isnull(json_FinalKillCounts)?0:json_FinalKillCounts.getAsInt();
        Win = isnull(json_Win)?0:json_Win.getAsInt();
        Lose = isnull(json_Losses)?0:json_Losses.getAsInt();
        ResourcesCollectCounts = isnull(json_ResourcesCollectCounts)?0:json_ResourcesCollectCounts.getAsInt();
        LootBoxCounts = isnull(json_LootBox)?0:json_LootBox.getAsInt();
        Final_KD = FinalDieCounts == 0?FinalKillCounts:(float) FinalKillCounts/(float) FinalDieCounts;
        Common_KD = CommonDieCounts == 0? CommonKillCounts:(float) CommonKillCounts/(float) CommonDieCounts;
        Bed_BL = LossesBedCounts == 0? BreakBedCounts:(float)BreakBedCounts/(float)LossesBedCounts;
        Self_WL = Lose == 0? Win:(float)Win/(float)Lose;
        /*
        [Hypixel]玩家NotSB的起床信息:
        等级:7☆  硬币:21765枚
        摧毁床数:9  B/L:0.1
        总击杀数:178  K/D:0.77
        最终击杀:60  Final K/D:0.65
        总胜场:27场  总W/L:0.31
        总死亡数:** 最终死亡:**
        获取资源数:****  获取宝箱数:***
         */
        msg.append("[Hypixel]玩家").append(playerName).append("的起床战绩:\n")
                .append("等级:").append(Level).append("☆").append("  ").append("硬币:").append(Coins).append("枚")
                .append("\n").append("摧毁床数:").append(BreakBedCounts).append("  ").append("B/L:").append(formatter.format(Bed_BL))
                .append("\n").append("总击杀数:").append(CommonKillCounts).append("  ").append("K/D:").append(formatter.format(Common_KD))
                .append("\n").append("最终击杀:").append(FinalKillCounts).append("  ").append("Final K/D:").append(formatter.format(Final_KD))
                .append("\n").append("总胜场:").append(Win).append("  ").append("W/L:").append(formatter.format(Self_WL))
                .append("\n"+"总死亡数:"+CommonDieCounts+"  "+"最终死亡:"+FinalDieCounts)
                .append("\n").append("获取资源数:").append(ResourcesCollectCounts).append("  ").append("获取宝箱数:").append(LootBoxCounts);
        CQ.sendGroupMsg(GroupID,msg.toString());
    }
}
