package cn.mappyj.action.skyblock;

import cn.mappyj.minecraftqueryplugin;
import cn.mappyj.utils.LanguageUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.hypixel.api.HypixelAPI;
import org.meowy.cqp.jcq.entity.CoolQ;

import javax.annotation.processing.FilerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SkyBlockJson extends AbstractSkyBlock{

    protected JsonObject perJson = new JsonObject();
    protected String uuid;
    protected File file = new File(minecraftqueryplugin.AppDirectory+File.separator+"skyblock"+File.separator+"profiles"+File.separator+uuid+".json");

    protected SkyBlockJson(long GroupID, CoolQ CQ, HypixelAPI apiKey, String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, arg);
    }

    private void getJson() throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] temp = new byte[(int)file.length()];
        if(inputStream.read(temp) < file.length()){
            inputStream.close();
            throw new FilerException("So Large File");
        }else{
            inputStream.close();
            JsonParser parser = new JsonParser();
            JsonElement temp_Object = parser.parse(new String(temp));
            if(temp_Object.isJsonObject()){
                perJson = temp_Object.getAsJsonObject();
            }
        }
    }

    protected void checkFile() throws IOException {
        if(!file.createNewFile()){
            System.out.println(LanguageUtil.CatchException);
        }
    }
}

class WriteLock{
    protected static ReadWriteLock ProfileLock = new ReentrantReadWriteLock();
}