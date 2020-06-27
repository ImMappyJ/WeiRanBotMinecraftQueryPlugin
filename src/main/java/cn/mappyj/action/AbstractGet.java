package cn.mappyj.action;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public abstract class AbstractGet extends Thread{
    CoolQ CQ;
    long GroupID;
    HypixelAPI apiKey;
    BiConsumer<AbstractReply,Throwable> theConsumer;
    String arg;
    protected AbstractGet(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer,String arg) throws InterruptedException, ExecutionException, IOException {
        this.CQ = CQ;
        this.GroupID = GroupID;
        this.apiKey= apiKey;
        this.theConsumer = theConsumer;
        this.arg = arg;
        execute();
    }

    protected void execute() throws IOException, ExecutionException, InterruptedException {}

    protected boolean isnull(Object object){return Objects.isNull(object); }
}
