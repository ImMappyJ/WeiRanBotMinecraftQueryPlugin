package cn.mappyj.event;

import cn.mappyj.utils.CharProcessUtil;
import cn.mappyj.utils.LanguageUtil;
import cn.mappyj.utils.MojangCastUtil;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.reply.PlayerReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public abstract class AbstractGetPlayerStatsInfo{
    JsonObject statsJson;
    JsonObject achievementJson;
    String playerName;
    private String type;
    CoolQ CQ;
    long GroupID;
    HypixelAPI apiKey;
    BiConsumer<AbstractReply,Throwable> theConsumer;
    String arg;
    protected AbstractGetPlayerStatsInfo(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg,String type) throws InterruptedException, ExecutionException, IOException {
        this.type = type;
        this.GroupID = GroupID;
        this.CQ = CQ;
        this.apiKey = apiKey;
        this.theConsumer = theConsumer;
        this.arg = arg;
        getstatsJson();
        execute();
    }

    protected void execute(){}

    protected boolean isnull(Object o){return Objects.isNull(o); }

    private void getstatsJson() throws IOException, ExecutionException, InterruptedException {
        CharProcessUtil processUtil = new CharProcessUtil();
        MojangCastUtil castUtil = new MojangCastUtil();
        UUID uuid = processUtil.stringUUIDToUUID(castUtil.nametoStringUUID(arg));
        if(isnull(uuid)){
            CQ.sendGroupMsg(GroupID, LanguageUtil.Mojang_InvalidName);
            return;
        }
        PlayerReply playerReply = apiKey.getPlayerByUuid(uuid).whenComplete(theConsumer).get();
        if(isnull(playerReply.getPlayer())){CQ.sendGroupMsg(GroupID,LanguageUtil.Hypixel_InvalidName);return;}
        this.statsJson = playerReply.getPlayer().get("stats").getAsJsonObject().get(type).getAsJsonObject();
        if(isnull(this.statsJson)){CQ.sendGroupMsg(GroupID,LanguageUtil.CantGetGameStats);return;}
        this.achievementJson = playerReply.getPlayer().get("achievements").getAsJsonObject();
        this.playerName = playerReply.getPlayer().get("displayname").getAsString();
    }
}
