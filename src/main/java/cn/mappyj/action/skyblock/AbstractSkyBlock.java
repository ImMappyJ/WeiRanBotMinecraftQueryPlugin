package cn.mappyj.action.skyblock;

import cn.mappyj.action.AbstractGet;
import cn.mappyj.MinecraftQueryPlugin;
import cn.mappyj.utils.LanguageUtil;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.util.GameType;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public abstract class AbstractSkyBlock extends AbstractGet {
    protected final String type = GameType.SKYBLOCK.getDbName();
    protected AbstractSkyBlock(long GroupID, CoolQ CQ, HypixelAPI apiKey,String... args) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, args);
    }

    static{
        File file = new File(MinecraftQueryPlugin.AppDirectory+File.separator+"skyblock"+File.separator+"profiles");
        if(!file.exists()){
            if (!file.mkdirs()) {
                System.out.println(LanguageUtil.CatchException);
            }
        }
    }
}
