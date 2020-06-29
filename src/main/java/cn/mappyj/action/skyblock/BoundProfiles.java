package cn.mappyj.action.skyblock;

import cn.mappyj.action.AbstractGet;
import cn.mappyj.utils.LanguageUtil;
import net.hypixel.api.HypixelAPI;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class BoundProfiles extends AbstractSkyBlock {

    int index;

    protected BoundProfiles(long GroupID, CoolQ CQ, HypixelAPI apiKey, String... args) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, args);
        //args[0] ID [1] Index
        if(!args[1].matches("[0-9]+")){
            CQ.sendGroupMsg(GroupID, LanguageUtil.PleaseKeyInteger);
        }else{
            execute();
        }
    }

    @Override
    protected void execute(){

    }
}
