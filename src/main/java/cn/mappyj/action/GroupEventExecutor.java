package cn.mappyj.action;

import cn.mappyj.action.api.CheckGroupAPI;
import cn.mappyj.action.skyblock.BoundProfiles;
import cn.mappyj.action.skyblock.GetProfiles;
import cn.mappyj.action.skyblock.GetSkyBlockInfo;
import cn.mappyj.action.skyblock.GetSkyBlockSkills;
import cn.mappyj.utils.HypixelApiKeyUtil;
import cn.mappyj.utils.LanguageUtil;
import net.hypixel.api.HypixelAPI;
import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.entity.Group;

import javax.swing.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupEventExecutor extends Thread{
    private final CoolQ CQ;
    private final String msg;
    private final UUID key;
    private final long GroupID,QQID;
    private HypixelAPI apiKey;

    public GroupEventExecutor(long GroupID,long QQID, String msg, CoolQ CQ){
        this.CQ = CQ;
        this.GroupID = GroupID;
        this.msg = msg;
        this.QQID = QQID;
        key = new HypixelApiKeyUtil(GroupID).getRandomApiKey();
    }

    public void run(){
        Pattern pattern = Pattern.compile("/(hyp|mojang)\\s?(\\w+|)?\\s?(\\w+|)?\\s?(\\w+|)?\\s?(\\w+)?");
        Matcher matcher = pattern.matcher(msg);
        if(matcher.matches()){
            if(key == null){CQ.sendGroupMsg(GroupID,LanguageUtil.Hypixel_InvalidKey);return;}
            apiKey = new HypixelAPI(key);
            try {
                if(!commandChecker(matcher)){
                    CQ.sendGroupMsg(GroupID, LanguageUtil.Wrong_Command);
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
                CQ.logInfo("",e);
            }
        }
    }

    private boolean commandChecker(Matcher matcher) throws InterruptedException, ExecutionException, IOException {
        switch (matcher.group(1).toLowerCase()){
            case "hyp":
                switch (matcher.group(2).toLowerCase()){
                    case "info":
                        GetPlayerInfo getPlayerInfo = new GetPlayerInfo(GroupID,CQ,apiKey,matcher.group(3));
                        return true;
                    case"bedwars":
                    case "bw":
                        GetBedWarsStats getBedWarsStats = new GetBedWarsStats(GroupID,CQ,apiKey,matcher.group(3));
                        return true;
                    case"skywars":
                    case "sw":
                        GetSkyWarsStats getSkyWarsStats = new GetSkyWarsStats(GroupID,CQ,apiKey,matcher.group(3));
                        return true;
                    case"arcade":
                    case"a":
                        switch (matcher.group(3).toLowerCase()){
                            case"fh":
                            case"farmhunt":
                                GetArcadeStats getFarmHuntStats = new GetArcadeStats(GroupID,CQ,apiKey,matcher.group(4),"farmhunt");
                                return true;
                            case"mw":
                            case"miniwalls":
                                GetArcadeStats getMiniWallsStats = new GetArcadeStats(GroupID,CQ,apiKey,matcher.group(4),"miniwalls");
                                return true;
                            case"simonsays":
                            case"hypsays":
                                GetArcadeStats getSimonSaysStats = new GetArcadeStats(GroupID,CQ,apiKey,matcher.group(4),"simonsays");
                                return true;
                            default:
                                GetArcadeStats getArcadeStats = new GetArcadeStats(GroupID,CQ,apiKey,matcher.group(4),"");
                                return true;
                        }
                    case"mw":
                    case"megawalls":
                        if(matcher.group(4).equals("")){GetMegaWallsStats getMegaWallsStats = new GetMegaWallsStats(GroupID,CQ,apiKey,matcher.group(3));}
                        else{GetMegaWallsStats getMegaWallsStats = new GetMegaWallsStats(GroupID,CQ,apiKey,matcher.group(3),matcher.group(4));}
                        return true;
                    case"uhc":
                        GetUHCStats getUHCStats = new GetUHCStats(GroupID,CQ,apiKey,matcher.group(3));
                        return true;
                    case"watchdog":
                    case"wd":
                        GetWatchDogStatus getWatchDogStatus = new GetWatchDogStatus(GroupID,CQ,apiKey,matcher.group(3));
                        return true;
                    case"guild":
                    case"g":
                        if(!matcher.group(4).equals("")){
                            GetGuildInfo getGuildInfo = new GetGuildInfo(GroupID,CQ,apiKey,matcher.group(3),matcher.group(4));
                        }else{
                            GetGuildInfo getGuildInfo = new GetGuildInfo(GroupID,CQ,apiKey,matcher.group(3),"player");
                        }
                        return true;
                    case"skyblock":
                    case"sb":
                        switch (matcher.group(4).toLowerCase()){
                            case"":
                                GetSkyBlockInfo getSkyBlockInfo = new GetSkyBlockInfo(GroupID,CQ,apiKey,matcher.group(3));
                                return true;
                            case"list":
                                GetProfiles getProfiles = new GetProfiles(GroupID,CQ,apiKey,matcher.group(3));
                                return true;
                            case"bound":
                                BoundProfiles boundProfiles = new BoundProfiles(GroupID,CQ,apiKey,matcher.group(3),matcher.group(5));
                                return true;
                            case"skill":
                                GetSkyBlockSkills getSkyBlockSkills = new GetSkyBlockSkills(GroupID,CQ,apiKey,matcher.group(3));
                                return true;
                            default:
                                return false;
                        }
                    case"api":
                        if ("".equals(matcher.group(3).toLowerCase())) {
                            if (QQID == 1603931150 || CQ.getGroupMemberInfo(GroupID, QQID).getAuthority().value() != 1) {
                                new CheckGroupAPI(CQ, GroupID);
                            } else {
                                CQ.sendGroupMsg(GroupID, LanguageUtil.NoPermission);
                            }
                            return true;
                        }
                        return false;
                    default: return false;
                }
            case "mojang":
                switch (matcher.group(2).toLowerCase()){
                    case "names":
                    case "nh":
                        GetHistoryName getHistoryName = new GetHistoryName(GroupID,CQ,apiKey,matcher.group(3));
                        return true;
                    case "minecraft":
                    case "mc":
                        GetMojangStatisticsStats getMinecraftStatisticsStats = new GetMojangStatisticsStats(CQ,GroupID,"minecraft");
                        return true;
                    case "minecraft_dungeon":
                    case "dungeon":
                        GetMojangStatisticsStats getDungeonStatisticsStats = new GetMojangStatisticsStats(CQ,GroupID,"minecraft_dungeon");
                        return true;
                    default:return false;
                }
            default:return false;
        }
    }
}
