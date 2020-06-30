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

    int index;
    String ID;

    public BoundProfiles(long GroupID, CoolQ CQ, HypixelAPI apiKey, String... args) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, args);
        this.ID = args[0];
        super.uuid = getStringUUID();
        if(!isnull(uuid)){
            file = getFile();
            //args[0] ID [1] Index
            if(!args[1].matches("[1-9]+")){
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
                CQ.sendGroupMsg(GroupID,"请输入/hyp sb ID list查看存档列表后再进行绑定操作");
                return;
            }
            JsonArray profilesList = perJson.get(uuid).getAsJsonArray();
            if(profilesList.get(this.index-1).isJsonNull()){CQ.sendGroupMsg(GroupID,"序号输入错误");return;}
            Map.Entry<String,JsonElement> entry = profilesList.get(this.index-1).getAsJsonObject().entrySet().iterator().next();
            addToJson(entry.getValue().getAsString());
            StringBuilder msg = new StringBuilder();
            msg.append("成功绑定").append(entry.getKey()).append("存档");
            CQ.sendGroupMsg(GroupID,msg.toString());
        } catch (IOException e) {
            e.printStackTrace();
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
        }
    }

    private void addToJson(String id){
        if(!new CheckFileUtil().checkFile(file)){
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
            return;
        }
        RWLock.ProfileLock.writeLock().lock();
        try{
            super.perJson.addProperty("profile",id);
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
