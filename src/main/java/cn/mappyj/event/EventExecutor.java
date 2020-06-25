package cn.mappyj.event;

import cn.mappyj.utils.LanguageUtil;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventExecutor extends Thread{
    private BiConsumer<AbstractReply,Throwable> consumer = theConsumer();
    private HypixelAPI apiKey = new HypixelAPI(UUID.fromString("ad8231a5-ee7a-4aad-a83a-486dd3468fbf"));
    private CoolQ CQ;
    private String msg;
    private long GroupID;

    public EventExecutor(long GroupID, String msg, CoolQ CQ){
        this.CQ = CQ;
        this.GroupID = GroupID;
        this.msg = msg;
    }

    public void run(){
        Pattern pattern = Pattern.compile("/(hyp|mojang)\\s?(\\w+|)?\\s?(\\w+|)?\\s?(\\w+|)?");
        Matcher matcher = pattern.matcher(msg);
        if(matcher.matches()){
            try {
                if(!commandChecker(matcher)){
                    CQ.sendGroupMsg(GroupID, LanguageUtil.Wrong_Command);
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
            }
        }
    }

    private boolean commandChecker(Matcher matcher) throws InterruptedException, ExecutionException, IOException {
        switch (matcher.group(1)){
            case "hyp":
                switch (matcher.group(2)){
                    case "info":
                        GetPlayerInfo getPlayerInfo = new GetPlayerInfo(GroupID,CQ,apiKey,theConsumer(),matcher.group(3));
                        return true;
                    case"bedwars":
                    case "bw":
                        GetBedWarsStats getBedWarsStats = new GetBedWarsStats(GroupID,CQ,apiKey,theConsumer(),matcher.group(3));
                        return true;
                    case"skywars":
                    case "sw":
                        GetSkyWarsStats getSkyWarsStats = new GetSkyWarsStats(GroupID,CQ,apiKey,theConsumer(),matcher.group(3));
                        return true;
                    case"arcade":
                    case"a":
                        switch (matcher.group(3)){
                            case"fh":
                            case"farmhunt":
                                GetArcadeStats getFarmHuntStats = new GetArcadeStats(GroupID,CQ,apiKey,theConsumer(),matcher.group(4),"farmhunt");
                                return true;
                            case"mw":
                            case"miniwalls":
                                GetArcadeStats getMiniWallsStats = new GetArcadeStats(GroupID,CQ,apiKey,theConsumer(),matcher.group(4),"miniwalls");
                                return true;
                            default:
                                return false;
                        }
                    default: return false;
                }
            case "mojang":
                switch (matcher.group(2)){
                    case "names":
                    case "nh":
                        GetHistoryName getHistoryName = new GetHistoryName(GroupID,CQ,apiKey,theConsumer(),matcher.group(3));
                        return true;
                    default:return false;
                }
            default:return false;
        }
    }

    private <T extends AbstractReply> BiConsumer <T,Throwable> theConsumer(){
        return(result,throwable)->{
            if(throwable != null){
                throwable.printStackTrace();
                return;
            }
        };
    }
}
