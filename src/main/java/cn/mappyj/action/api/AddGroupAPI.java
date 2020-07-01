package cn.mappyj.action.api;

import cn.mappyj.utils.LanguageUtil;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;

public class AddGroupAPI extends AbstractApiKey{

    private long QQID;
    private String apiKey;

    public AddGroupAPI(CoolQ CQ, long QQID, long GroupID, String apiKey) throws IOException {
        super(CQ, GroupID);
        this.QQID = QQID;
        this.apiKey = apiKey;
        add();
    }

    private void add() throws IOException {
        if(api.addToGroup(apiKey)){
            CQ.sendPrivateMsg(QQID,"添加成功!");
        }else{
            CQ.sendPrivateMsg(QQID, LanguageUtil.Hypixel_InvalidKey);
        }
    }
}
