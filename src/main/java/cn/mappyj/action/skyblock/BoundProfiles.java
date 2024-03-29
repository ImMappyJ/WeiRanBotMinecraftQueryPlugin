package cn.mappyj.action.skyblock;

import cn.mappyj.utils.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.hypixel.api.HypixelAPI;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BoundProfiles extends SkyBlockJson {

    private int index;
    private final String ID;

    public BoundProfiles(long GroupID, CoolQ CQ, HypixelAPI apiKey, String... args) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, args);
        this.ID = args[0];
        super.uuid = getStringUUID();
        if(!isnull(super.uuid)){
            super.file = getFile();
            //args[0] ID [1] Index
            if(args[1]==null||!args[1].matches("[1-9]+")){
                CQ.sendGroupMsg(GroupID, LanguageUtil.PleaseKeyInteger);
            }else{
                this.index = Integer.parseInt(args[1]);
                execute();
            }
        }
    }

    @Override
    protected void execute(){
        try {
            perJson = new GetFileTextUtil().getJson(super.file);
            if(isnull(perJson) || perJson.get(uuid).isJsonNull()){
                GetProfiles getProfiles = new GetProfiles(GroupID,CQ,apiKey,ID);
                return;
            }
            JsonArray profilesList = perJson.get(uuid).getAsJsonArray();
            if(profilesList.get(this.index-1).isJsonNull()){CQ.sendGroupMsg(GroupID,"序号输入错误");return;}
            Map.Entry<String,JsonElement> entry = profilesList.get(this.index-1).getAsJsonObject().entrySet().iterator().next();
            addToJson(entry.getKey(),entry.getValue().getAsString());
            StringBuilder msg = new StringBuilder();
            msg.append("成功绑定").append(entry.getKey()).append("存档");
            CQ.sendGroupMsg(GroupID,msg.toString());
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
        }
    }

    private void addToJson(String name,String id){
        if(!new CheckFileUtil().checkFile(file)){
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
            return;
        }
        RWLock.ProfileLock.writeLock().lock();
        try{
            super.perJson.addProperty("profile_id",id);
            super.perJson.addProperty("profile_name",name);
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

    private String getStringUUID(){
        try{
            MojangCastUtil mojangCastUtil = new MojangCastUtil();
            String Stringuuid =  mojangCastUtil.nametoStringUUID(this.ID);
            if(isnull(Stringuuid)){
                CQ.sendGroupMsg(GroupID,LanguageUtil.Mojang_InvalidName);
                return null;
            }else{
                return Stringuuid;
            }
        }catch(IOException e){
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
            e.printStackTrace();
        }
        return null;
    }
}
