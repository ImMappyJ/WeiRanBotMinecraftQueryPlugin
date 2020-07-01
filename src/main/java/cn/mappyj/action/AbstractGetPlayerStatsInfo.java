package cn.mappyj.action;

import cn.mappyj.utils.CharProcessUtil;
import cn.mappyj.utils.LanguageUtil;
import cn.mappyj.utils.MojangCastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.reply.PlayerReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public abstract class AbstractGetPlayerStatsInfo{
    protected JsonObject statsJson;
    protected JsonObject achievementJson;
    protected String playerName;
    protected String uuid;
    private String type;
    protected CoolQ CQ;
    protected long GroupID;
    protected HypixelAPI apiKey;
    protected String arg;
    protected AbstractGetPlayerStatsInfo(long GroupID, CoolQ CQ, HypixelAPI apiKey, String arg,String type) throws InterruptedException, ExecutionException, IOException {
        this.type = type;
        this.GroupID = GroupID;
        this.CQ = CQ;
        this.apiKey = apiKey;
        this.arg = arg;
        getstatsJson();
    }

    protected void execute(){}

    protected  void extraExecute(){}

    protected boolean isnull(Object o){return Objects.isNull(o); }

    private void getstatsJson() throws IOException, ExecutionException, InterruptedException {
        CharProcessUtil processUtil = new CharProcessUtil();
        MojangCastUtil castUtil = new MojangCastUtil();
        UUID uuid = processUtil.stringUUIDToUUID(castUtil.nametoStringUUID(arg));
        if(isnull(uuid)){
            CQ.sendGroupMsg(GroupID, LanguageUtil.Mojang_InvalidName);
            return;
        }
        PlayerReply playerReply = apiKey.getPlayerByUuid(uuid).get();
        if(isnull(playerReply.getPlayer())){CQ.sendGroupMsg(GroupID,LanguageUtil.Hypixel_InvalidName);return;}
        JsonElement ele_stats = playerReply.getPlayer().get("stats").getAsJsonObject().get(type),
                ele_ach = playerReply.getPlayer().get("achievements").getAsJsonObject().get(type);

        this.statsJson = isnull(ele_stats)?null:ele_stats.getAsJsonObject();
        this.achievementJson = isnull(ele_ach)?null:ele_ach.getAsJsonObject();
        if(isnull(this.statsJson)&&isnull(this.achievementJson)){CQ.sendGroupMsg(GroupID,LanguageUtil.CantGetGameStats);return;}
        this.playerName = playerReply.getPlayer().get("displayname").getAsString();
        this.uuid = playerReply.getPlayer().get("uuid").getAsString();
    }
}
