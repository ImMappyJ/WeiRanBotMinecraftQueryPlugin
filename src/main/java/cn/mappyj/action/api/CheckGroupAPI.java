package cn.mappyj.action.api;

import cn.mappyj.utils.LanguageUtil;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;

public class CheckGroupAPI extends AbstractApiKey{
    public CheckGroupAPI(CoolQ CQ, long GroupID) {
        super(CQ, GroupID);
        check();
    }

    private void check(){
        try {
            int[] status = api.checkAllApiKeyStatus();
            int
                    good = status[0],
                    bad = status[1],
                    error = status[2];
            StringBuilder msg = new StringBuilder();
            msg.append("[HYPIXELAPI]该群所有Key状态如下:" + "\n" + "可用ApiKey:").append(good).append("个")
                    .append("\n").append("不可用ApiKey:").append(bad).append("个")
                    .append("\n").append("查询失败:").append(error).append("个")
                    .append("\n").append("已对不可用ApiKey采取强制删除措施!");
            CQ.sendGroupMsg(GroupID,msg.toString());
        } catch (IOException e) {
            CQ.sendGroupMsg(GroupID, LanguageUtil.CatchException);
            e.printStackTrace();
        }
    }
}
