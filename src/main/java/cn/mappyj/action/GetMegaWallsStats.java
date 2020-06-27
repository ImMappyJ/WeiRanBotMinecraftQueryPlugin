package cn.mappyj.action;

import cn.mappyj.utils.LanguageUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetMegaWallsStats extends AbstractGetPlayerStatsInfo{
    String profession;

    protected GetMegaWallsStats(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, theConsumer, arg, "Walls3");
        commonExecute();
    }

    protected GetMegaWallsStats(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg, String profession) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, theConsumer, arg, "Walls3");
        this.profession = profession;
        queryExecute();
    }

    protected void queryExecute(){
        if(isnull(statsJson) || isnull(achievementJson)) return;
        JsonElement json_nowKills,json_nowDeaths,json_nowAssists,json_nowWin,json_nowFinalKills,json_nowFinalDeaths;
        JsonElement[] json_SkillsTrees = new JsonElement[5];
        String nowClass = this.profession.toLowerCase();

        JsonObject json_Profession = isnull(statsJson.get("classes"))?null:(JsonObject) statsJson.get("classes").getAsJsonObject().get(nowClass);

        boolean isOwned = false;
        if(nowClass.equals("shark") || nowClass.equals("cow") || nowClass.equals("hunter")){
            isOwned = true;
        }else if(!isnull(json_Profession)){
            assert json_Profession != null;
            isOwned = !isnull(json_Profession.get("unlocked")) && json_Profession.get("unlocked").getAsBoolean();
        }

        if(!isOwned){CQ.sendGroupMsg(GroupID,"玩家未拥有该职业"); return;}

        StringBuilder msg = new StringBuilder();
        json_nowDeaths = statsJson.get(nowClass+"_total_deaths");
        json_nowKills = statsJson.get(nowClass+"_total_kills");
        json_nowAssists = statsJson.get(nowClass+"_assists");
        json_nowWin = statsJson.get(nowClass+"_wins");
        json_nowFinalDeaths = statsJson.get(nowClass+"_final_deaths");
        json_nowFinalKills = statsJson.get(nowClass+"_final_kills");
        json_SkillsTrees[0] = isnull(json_Profession)?statsJson.get(nowClass+"_new_a"):json_Profession.get("skill_level_a");
        json_SkillsTrees[1] = isnull(json_Profession)?statsJson.get(nowClass+"_new_b"):json_Profession.get("skill_level_b");
        json_SkillsTrees[2] = isnull(json_Profession)?statsJson.get(nowClass+"_new_c"):json_Profession.get("skill_level_c");
        json_SkillsTrees[3] = isnull(json_Profession)?statsJson.get(nowClass+"_new_d"):json_Profession.get("skill_level_d");
        json_SkillsTrees[4] = isnull(json_Profession)?statsJson.get(nowClass+"_new_g"):json_Profession.get("skill_level_g");

        int nowDeaths,nowKills,nowAssists,nowWin,nowFinalDeaths,nowFinalKills;
        int[] SkillsTree = new int[5];
        double nowKD,nowFKD;
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);

        nowDeaths = isnull(json_nowDeaths)?0:json_nowDeaths.getAsInt();
        nowAssists = isnull(json_nowAssists)?0:json_nowAssists.getAsInt();
        nowKills = isnull(json_nowKills)?0:json_nowKills.getAsInt();
        nowWin = isnull(json_nowWin)?0:json_nowWin.getAsInt();
        nowFinalDeaths = isnull(json_nowFinalDeaths)?0:json_nowFinalDeaths.getAsInt();
        nowFinalKills = isnull(json_nowFinalKills)?0:json_nowFinalKills.getAsInt();
        nowKD = nowDeaths == 0?nowKills:(float)nowKills/(float)nowDeaths;
        nowFKD = nowFinalDeaths == 0?nowFinalKills:(float)nowFinalKills/(float)nowFinalDeaths;

        int t = 0;
        for(JsonElement je:json_SkillsTrees) {
            SkillsTree[t] = isnull(je) ? 1 : je.getAsInt();
            t++;
        }
        /*
        [Hypixel]玩家NotSB的超战信息:
        硬币:1000  助攻:222
        总击杀:222  总死亡:222
        最终击杀:222  最终死亡:222
        KD:1  Final KD:1  获胜22局
        获取宝藏:22  神话之尘:22
        当前职业:Herobrine
        Herobrine职业信息:
        技能点: 5 5 5 5 5
        击杀:222  死亡:222  助攻:222
        最终击杀:222 最终死亡:222
        KD:1 Final KD:1  获胜22局
         */
        msg.append("[Hypixel]玩家").append(playerName).append("的超战信息:")
                .append("\n").append("职业:").append(String.valueOf(profession.charAt(0)).toUpperCase()).append(profession.substring(1))
                .append("\n").append("技能点:").append(SkillsTree[0]).append(" ").append(SkillsTree[1]).append(" ").append(SkillsTree[2]).append(" ").append(SkillsTree[3]).append(" ").append(SkillsTree[4])
                .append("\n").append("击杀:").append(nowKills).append("  ").append("死亡:").append(nowDeaths).append("  ").append("助攻:").append(nowAssists)
                .append("\n").append("最终击杀:").append(nowFinalKills).append("  ").append("最终死亡:").append(nowFinalDeaths)
                .append("\n").append("KD:").append(format.format(nowKD)).append("  ").append("Final KD:").append(format.format(nowFKD)).append("  ").append("获胜").append(nowWin).append("局");
        CQ.sendGroupMsg(GroupID,msg.toString());
    }

    protected void commonExecute() {
        if(isnull(statsJson) || isnull(achievementJson)) return;
        JsonElement json_Coins,json_totalKills,json_totalFinalKills,json_totalDeaths,json_totalFinalDeaths,json_TreasureFound,json_MythicFavor,json_Win,json_totalAssists;
        JsonElement json_nowClass,json_nowKills,json_nowDeaths,json_nowAssists,json_nowWin,json_nowFinalKills,json_nowFinalDeaths;
        JsonElement[] json_SkillsTrees = new JsonElement[5];
        String nowClass;
        json_Coins = statsJson.get("coins");
        json_MythicFavor = statsJson.get("mythic_favor");
        json_nowClass = statsJson.get("chosen_class");
        json_totalDeaths = statsJson.get("total_deaths");
        json_totalFinalDeaths = statsJson.get("final_deaths");
        json_totalAssists = statsJson.get("assists");
        json_totalKills = statsJson.get("total_kills");
        json_totalFinalKills = statsJson.get("total_final_kills");
        json_TreasureFound = statsJson.get("treasures_found");
        json_Win = statsJson.get("wins");

        nowClass = isnull(json_nowClass)? LanguageUtil.Unknown :json_nowClass.getAsString();

        json_nowDeaths = statsJson.get(nowClass.toLowerCase()+"_total_deaths");
        json_nowKills = statsJson.get(nowClass.toLowerCase()+"_total_kills");
        json_nowAssists = statsJson.get(nowClass.toLowerCase()+"_assists");
        json_nowWin = statsJson.get(nowClass.toLowerCase()+"_wins");
        json_nowFinalDeaths = statsJson.get(nowClass.toLowerCase()+"_final_deaths");
        json_nowFinalKills = statsJson.get(nowClass.toLowerCase()+"_final_kills");
        json_SkillsTrees[0] = statsJson.get(nowClass.toLowerCase()+"_new_a");
        json_SkillsTrees[1] = statsJson.get(nowClass.toLowerCase()+"_new_b");
        json_SkillsTrees[2] = statsJson.get(nowClass.toLowerCase()+"_new_c");
        json_SkillsTrees[3] = statsJson.get(nowClass.toLowerCase()+"_new_d");
        json_SkillsTrees[4] = statsJson.get(nowClass.toLowerCase()+"_new_g");

        int Coins,MythicFavor,totalDeaths,totalAssists,totalKills,totalFinalKills,totalFinalDeaths,TreasureFonud,Win,nowDeaths,nowKills,nowAssists,nowWin,nowFinalDeaths,nowFinalKills;
        int[] SkillsTree = new int[5];
        double KD,FKD,nowKD,nowFKD;
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        StringBuilder msg = new StringBuilder();

        Coins = isnull(json_Coins)?0:json_Coins.getAsInt();
        MythicFavor = isnull(json_MythicFavor)?0:json_MythicFavor.getAsInt();
        totalDeaths = isnull(json_totalDeaths)?0:json_totalDeaths.getAsInt();
        totalKills = isnull(json_totalKills)?0:json_totalKills.getAsInt();
        totalAssists = isnull(json_totalAssists)?0:json_totalAssists.getAsInt();
        totalFinalKills = isnull(json_totalFinalKills)?0:json_totalFinalKills.getAsInt();
        totalFinalDeaths = isnull(json_totalFinalDeaths)?0:json_totalFinalDeaths.getAsInt();
        TreasureFonud = isnull(json_TreasureFound)?0:json_TreasureFound.getAsInt();
        Win = isnull(json_Win)?0:json_Win.getAsInt();
        nowDeaths = isnull(json_nowDeaths)?0:json_nowDeaths.getAsInt();
        nowAssists = isnull(json_nowAssists)?0:json_nowAssists.getAsInt();
        nowKills = isnull(json_nowKills)?0:json_nowKills.getAsInt();
        nowWin = isnull(json_nowWin)?0:json_nowWin.getAsInt();
        nowFinalDeaths = isnull(json_nowFinalDeaths)?0:json_nowFinalDeaths.getAsInt();
        nowFinalKills = isnull(json_nowFinalKills)?0:json_nowFinalKills.getAsInt();
        KD = totalDeaths == 0?totalKills:(float)totalKills/(float)totalDeaths;
        FKD = totalFinalDeaths == 0?totalFinalKills:(float)totalFinalKills/(float)totalFinalDeaths;
        nowKD = nowDeaths == 0?nowKills:(float)nowKills/(float)nowDeaths;
        nowFKD = nowFinalDeaths == 0?nowFinalKills:(float)nowFinalKills/(float)nowFinalDeaths;

        int t = 0;
        for(JsonElement je:json_SkillsTrees){
            SkillsTree[t] = isnull(je)?1:je.getAsInt();
            t++;
        }
        /*
        [Hypixel]玩家NotSB的超战信息:
        硬币:1000  助攻:222
        总击杀:222  总死亡:222
        最终击杀:222  最终死亡:222
        KD:1  Final KD:1  获胜22局
        获取宝藏:22  神话之尘:22
        当前职业:Herobrine
        Herobrine职业信息:
        技能点: 5 5 5 5 5
        击杀:222  死亡:222  助攻:222
        最终击杀:222 最终死亡:222
        KD:1 Final KD:1  获胜22局
         */
        msg.append("[Hypixel]玩家").append(playerName).append("的超战信息:")
                .append("\n").append("硬币:").append(Coins).append("  ").append("助攻:").append(totalAssists)
                .append("\n").append("总击杀:").append(totalKills).append("  ").append("总死亡:").append(totalDeaths)
                .append("\n").append("最终击杀:").append(totalFinalKills).append("  ").append("最终死亡:").append(totalFinalDeaths)
                .append("\n").append("KD:").append(format.format(KD)).append("  ").append("Final KD:").append(format.format(FKD)).append("  ").append("获胜").append(Win).append("局")
                .append("\n").append("获取宝藏:").append(TreasureFonud).append("  ").append("神话之尘:").append(MythicFavor)
                .append("\n").append("当前职业:").append(nowClass)
                .append("\n").append(nowClass).append("职业信息:").append("\n" + "技能点:").append(SkillsTree[0]).append(" ").append(SkillsTree[1]).append(" ").append(SkillsTree[2]).append(" ").append(SkillsTree[3]).append(" ").append(SkillsTree[4])
                .append("\n").append("击杀:").append(nowKills).append("  ").append("死亡:").append(nowDeaths).append("  ").append("助攻:").append(nowAssists)
                .append("\n").append("最终击杀:").append(nowFinalKills).append("  ").append("最终死亡:").append(nowFinalDeaths)
                .append("\n").append("KD:").append(format.format(nowKD)).append("  ").append("Final KD:").append(format.format(nowFKD)).append("  ").append("获胜").append(nowWin).append("局");
        CQ.sendGroupMsg(GroupID,msg.toString());
    }
}
