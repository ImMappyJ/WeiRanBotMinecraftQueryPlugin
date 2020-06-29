package cn.mappyj.action;

import cn.mappyj.action.skyblock.GetProfiles;
import cn.mappyj.utils.LanguageUtil;
import net.hypixel.api.HypixelAPI;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventExecutor extends Thread{
    private final HypixelAPI apiKey = new HypixelAPI(UUID.fromString("ad8231a5-ee7a-4aad-a83a-486dd3468fbf"));
    private final CoolQ CQ;
    private final String msg;
    private final long GroupID;

    public EventExecutor(long GroupID, String msg, CoolQ CQ){
        this.CQ = CQ;
        this.GroupID = GroupID;
        this.msg = msg;
    }

    public void run(){
        Pattern pattern = Pattern.compile("/(hyp|mojang)\\s?(\\w+|)?\\s?(\\w+|)?\\s?(\\w+|)?\\s?(\\w+)?");
        Matcher matcher = pattern.matcher(msg);
        if(matcher.matches()){
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
                        switch (matcher.group(4)){
                            case"":
                                return false;
                            case"list":
                                GetProfiles getProfiles = new GetProfiles(GroupID,CQ,apiKey,matcher.group(3));
                                return true;
                            default:
                                return false;
                        }
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
