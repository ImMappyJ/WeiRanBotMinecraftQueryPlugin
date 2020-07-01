package cn.mappyj.action.api;

import cn.mappyj.utils.HypixelApiKeyUtil;
import org.meowy.cqp.jcq.entity.CoolQ;

public abstract class AbstractApiKey {
    protected final long GroupID;
    protected final CoolQ CQ;
    protected final HypixelApiKeyUtil api;

    protected AbstractApiKey(CoolQ CQ, long GroupID){
        this.GroupID = GroupID;
        this.CQ = CQ;
        api = new HypixelApiKeyUtil(GroupID);
    }
}
