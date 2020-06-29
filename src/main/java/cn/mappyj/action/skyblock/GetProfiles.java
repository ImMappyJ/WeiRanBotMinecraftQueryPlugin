package cn.mappyj.action.skyblock;

import cn.mappyj.utils.CharProcessUtil;
import cn.mappyj.utils.LanguageUtil;
import cn.mappyj.utils.MojangCastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.util.GameType;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class GetProfiles extends SkyBlockJson {

    protected JsonObject statsJson;
    protected JsonObject achievementJson;
    protected String playerName;
    protected String arg;
    private final String type = GameType.SKYBLOCK.getDbName();

    public GetProfiles(long GroupID, CoolQ CQ, HypixelAPI apiKey, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, arg);
        getstatsJson();
        checkFile();
        execute();
    }

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
        this.statsJson = playerReply.getPlayer().get("stats").getAsJsonObject().get(type).getAsJsonObject();
        if(isnull(this.statsJson)){CQ.sendGroupMsg(GroupID,LanguageUtil.CantGetGameStats);return;}
        this.achievementJson = playerReply.getPlayer().get("achievements").getAsJsonObject();
        this.playerName = playerReply.getPlayer().get("displayname").getAsString();
        super.uuid = playerReply.getPlayer().get("uuid").getAsString();
    }

    protected void execute(){
        if(isnull(statsJson)||isnull(achievementJson)) return;
        JsonElement json_Profiles = statsJson.get("profiles");
        if(isnull(json_Profiles)){CQ.sendGroupMsg(GroupID, LanguageUtil.CantGetGameStats);return;}
        JsonObject json_ProfilesList = json_Profiles.getAsJsonObject();
        Map<String,String> profiles = new TreeMap<>();
        for(Map.Entry<String,JsonElement> entry:json_ProfilesList.entrySet()){
            JsonObject profile = (JsonObject)entry.getValue();
            profiles.put(profile.get("cute_name").getAsString(),profile.get("profile_id").getAsString());
        }
        StringBuilder msg = new StringBuilder();
        int counter = 1;
        msg.append("[Hypixel]玩家").append(playerName).append("的Skyblock存档:\n");
        for(Map.Entry<String,String> entry:profiles.entrySet()){
            msg.append(counter).append(".").append(entry.getKey()).append("\n");
            counter++;
        }
        addToJson(uuid,profiles);
        msg.append("输入/hyp sb ID bound [序号]即可绑定存档");
        CQ.sendGroupMsg(GroupID,msg.toString());
    }

    private void addToJson(String uuid, Map<String,String> map){
        WriteLock.ProfileLock.writeLock().lock();
        try{
            JsonObject perProfiles = new JsonObject();
            for(Map.Entry<String,String>entry:map.entrySet()){
                perProfiles.addProperty(entry.getKey().toLowerCase(),entry.getValue());
            }
            super.perJson.add(uuid,perProfiles);
            FileWriter writer = new FileWriter(file);
            CharProcessUtil processUtil = new CharProcessUtil();
            writer.write(processUtil.jsonObjectToJsonString(super.perJson));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            WriteLock.ProfileLock.writeLock().unlock();
        }
    }
}
