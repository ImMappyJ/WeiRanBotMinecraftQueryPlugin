package cn.mappyj.action.skyblock;

import cn.mappyj.utils.CharProcessUtil;
import cn.mappyj.utils.CheckFileUtil;
import cn.mappyj.utils.LanguageUtil;
import cn.mappyj.utils.MojangCastUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class GetProfiles extends SkyBlockJson {

    private JsonObject statsJson;
    private JsonObject achievementJson;
    private String playerName;

    public GetProfiles(long GroupID, CoolQ CQ, HypixelAPI apiKey, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, arg);
        getstatsJson();
        if(!isnull(uuid)){
            file = getFile();
            execute();
        }
    }

    private void getstatsJson() throws IOException, ExecutionException, InterruptedException {
        CharProcessUtil processUtil = new CharProcessUtil();
        MojangCastUtil castUtil = new MojangCastUtil();
        UUID uuid = processUtil.stringUUIDToUUID(castUtil.nametoStringUUID(args[0]));
        if(isnull(uuid)){
            CQ.sendGroupMsg(GroupID, LanguageUtil.Mojang_InvalidName);
            return;
        }
        PlayerReply playerReply = apiKey.getPlayerByUuid(uuid).get();
        if(isnull(playerReply.getPlayer())){CQ.sendGroupMsg(GroupID,LanguageUtil.Hypixel_InvalidName);return;}

        JsonElement ele_stats = playerReply.getPlayer().get("stats").getAsJsonObject().get(type),
                ele_ach = playerReply.getPlayer().get("stats").getAsJsonObject().get(type);

        this.statsJson = isnull(ele_stats)?null:ele_stats.getAsJsonObject();
        this.achievementJson = isnull(ele_ach)?null:ele_ach.getAsJsonObject();
        if(isnull(this.statsJson)&&isnull(this.achievementJson)){CQ.sendGroupMsg(GroupID,LanguageUtil.CantGetGameStats);return;}
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
        if(!new CheckFileUtil().checkFile(file)){
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
            return;
        }
        RWLock.ProfileLock.writeLock().lock();
        try{
            JsonArray profilesList = new JsonArray();
            for(Map.Entry<String,String>entry:map.entrySet()){
                JsonObject perProfile  = new JsonObject();
                perProfile.addProperty(entry.getKey(),entry.getValue());
                profilesList.add(perProfile);
            }
            super.perJson.add(uuid,profilesList);
            FileWriter writer = new FileWriter(file);
            CharProcessUtil processUtil = new CharProcessUtil();
            writer.write(processUtil.jsonObjectToJsonString(super.perJson));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            RWLock.ProfileLock.writeLock().unlock();
        }
    }
}
