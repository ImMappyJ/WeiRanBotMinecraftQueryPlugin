package cn.mappyj.action;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public abstract class AbstractGet extends Thread{
    protected CoolQ CQ;
    protected long GroupID;
    protected HypixelAPI apiKey;
    protected String[] args;
    protected AbstractGet(long GroupID, CoolQ CQ, HypixelAPI apiKey,String... args) throws InterruptedException, ExecutionException, IOException {
        this.CQ = CQ;
        this.GroupID = GroupID;
        this.apiKey= apiKey;
        this.args = args;
    }

    protected void execute() throws IOException, ExecutionException, InterruptedException {}

    protected boolean isnull(Object object){return Objects.isNull(object); }
}
