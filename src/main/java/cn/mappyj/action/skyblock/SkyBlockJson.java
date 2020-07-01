package cn.mappyj.action.skyblock;

import cn.mappyj.MinecraftQueryPlugin;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SkyBlockJson extends AbstractSkyBlock{

    protected JsonObject perJson = new JsonObject();
    protected String uuid;
    protected File file;

    protected SkyBlockJson(long GroupID, CoolQ CQ, HypixelAPI apiKey, String... args) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, args);
    }

    protected File getFile(){
        return new File(MinecraftQueryPlugin.AppDirectory+File.separator+"skyblock"+File.separator+"profiles"+File.separator+uuid+".json");
    }
}

class RWLock {
    public static ReadWriteLock ProfileLock = new ReentrantReadWriteLock();
}