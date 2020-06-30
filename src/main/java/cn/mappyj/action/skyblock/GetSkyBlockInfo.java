package cn.mappyj.action.skyblock;

import net.hypixel.api.HypixelAPI;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GetSkyBlockInfo extends SkyBlockJson{
    protected GetSkyBlockInfo(long GroupID, CoolQ CQ, HypixelAPI apiKey, String... args) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, args);
    }
}
