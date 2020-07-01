package cn.mappyj.action.skyblock;

import cn.mappyj.action.AbstractGet;
import cn.mappyj.utils.CharProcessUtil;
import cn.mappyj.utils.LanguageUtil;
import cn.mappyj.utils.MojangCastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class GetSkyBlockSkills extends AbstractGet {

    final String ID;
    String playerName;
    JsonObject achievementJson;

    public GetSkyBlockSkills(long GroupID, CoolQ CQ, HypixelAPI apiKey, String... args) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, args);
        ID = args[0];
        getstatsJson();
        execute();
    }

    @Override
    protected void execute() throws IOException, ExecutionException, InterruptedException {
        if(isnull(achievementJson)) return;
        JsonElement
                json_Fishing = achievementJson.get("skyblock_angler"),
                json_Mining = achievementJson.get("skyblock_excavator"),
                json_Farming = achievementJson.get("skyblock_harvester"),
                json_Combat = achievementJson.get("skyblock_combat"),
                json_Foraging = achievementJson.get("skyblock_gatherer"),
                json_Enchant = achievementJson.get("skyblock_augmentation"),
                json_Alchemy = achievementJson.get("skyblock_concoctor"),
                json_Taming = achievementJson.get("skyblock_domesticator");
        int
                Fishing = isnull(json_Fishing)?0:json_Fishing.getAsInt(),
                Mining = isnull(json_Mining)?0:json_Mining.getAsInt(),
                Farming = isnull(json_Farming)?0:json_Farming.getAsInt(),
                Combat = isnull(json_Combat)?0:json_Combat.getAsInt(),
                Foraging = isnull(json_Foraging)?0:json_Foraging.getAsInt(),
                Enchant = isnull(json_Enchant)?0:json_Enchant.getAsInt(),
                Alchemy = isnull(json_Alchemy)?0:json_Alchemy.getAsInt(),
                Taming = isnull(json_Taming)?0:json_Taming.getAsInt();
        /*
        [Hypixel]玩家ababa的SkyBlock技能表:
        Combat:22  Mining:22
        Farming:22  Fishing:22
        Foraging:22  Taming:22
        Enchant:22  Alchemy:22
         */
        StringBuilder msg = new StringBuilder();
        msg.append("[Hypixel]玩家").append(playerName).append("的Skyblock技能表:")
                .append("\n").append("Combat:").append(Combat).append("  ").append("Mining:").append(Mining)
                .append("\n").append("Farming:").append(Farming).append("  ").append("Fishing:").append(Fishing)
                .append("\n").append("Foraging:").append(Foraging).append("  ").append("Taming:").append(Taming)
                .append("\n").append("Enchant:").append(Enchant).append("  ").append("Alchemy:").append(Alchemy);
        CQ.sendGroupMsg(GroupID,msg.toString());
    }

    private void getstatsJson() throws IOException, ExecutionException, InterruptedException {
        CharProcessUtil processUtil = new CharProcessUtil();
        MojangCastUtil castUtil = new MojangCastUtil();
        UUID uuid = processUtil.stringUUIDToUUID(castUtil.nametoStringUUID(ID));
        if(isnull(uuid)){
            CQ.sendGroupMsg(GroupID, LanguageUtil.Mojang_InvalidName);
            return;
        }
        PlayerReply playerReply = apiKey.getPlayerByUuid(uuid).get();
        if(isnull(playerReply.getPlayer())){CQ.sendGroupMsg(GroupID,LanguageUtil.Hypixel_InvalidName);return;}

        JsonElement ele_ach = playerReply.getPlayer().get("achievements");

        this.achievementJson = isnull(ele_ach)?null:ele_ach.getAsJsonObject();
        if(isnull(this.achievementJson)){CQ.sendGroupMsg(GroupID,LanguageUtil.CantGetGameStats);return;}
        this.playerName = playerReply.getPlayer().get("displayname").getAsString();
    }
}
