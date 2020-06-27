package cn.mappyj.action;

import cn.mappyj.utils.CharProcessUtil;
import cn.mappyj.utils.LanguageUtil;
import cn.mappyj.utils.MojangCastUtil;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.reply.GuildReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetGuildInfo extends AbstractGet{
    String type;
    protected GetGuildInfo(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer, String arg,String type) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, theConsumer, arg);
        this.type = type.toLowerCase();
        getGuildInfo();
    }

    protected void getGuildInfo() throws IOException, ExecutionException, InterruptedException {
        GuildReply guildReply;
        GuildReply.Guild.Member member = null;
        switch (type){
            case"player":
                MojangCastUtil castUtil = new MojangCastUtil();
                CharProcessUtil processUtil = new CharProcessUtil();
                String stringuuid = castUtil.nametoStringUUID(arg);
                if(isnull(stringuuid)){CQ.sendGroupMsg(GroupID,LanguageUtil.Mojang_InvalidName);return;}
                UUID uuid =processUtil.stringUUIDToUUID(stringuuid);
                guildReply = apiKey.getGuildByPlayer(uuid).whenComplete(theConsumer).get();
                if(isnull(guildReply.getGuild())){CQ.sendGroupMsg(GroupID,LanguageUtil.GuildNotFound);return;}
                for(GuildReply.Guild.Member temp_Member:guildReply.getGuild().getMembers()){
                    if(temp_Member.getUuid().equals(uuid)){ member = temp_Member; break;}
                }
                break;
            case"name":
                guildReply = apiKey.getGuildByName(arg).whenComplete(theConsumer).get();
                if(isnull(guildReply.getGuild())){CQ.sendGroupMsg(GroupID,LanguageUtil.GuildNotFound);return;}
                break;
            default:
                CQ.sendGroupMsg(GroupID, LanguageUtil.Wrong_Command);
                return;
        }
        GuildReply.Guild theGuild = guildReply.getGuild();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder msg = new StringBuilder();
        if(!isnull(member)){
            String rank = !isnull(member.getRank())?member.getRank():"Default";
            msg.append("[Hypixel]玩家所在公会信息:")
                    .append("\n").append("名称:").append(theGuild.getName()).append("  ").append("Tag:").append(theGuild.getTag())
                    .append("\n").append("简介:").append(theGuild.getDescription())
                    .append("\n").append("等级:").append(theGuild.getLevel())
                    .append("\n").append("创建日期:").append(theGuild.getCreated().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).format(formatter))
                    .append("\n").append("玩家在公会中的信息:")
                    .append("\n").append("Rank:").append(rank)
                    .append("\n").append("加入日期:").append(member.getJoined().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).format(formatter));
        }else{
            msg.append(theGuild.getName()).append("公会信息:")
                    .append("\n").append("名称:").append(theGuild.getName()).append("  ").append("Tag:").append(theGuild.getTag())
                    .append("\n").append("简介:").append(theGuild.getDescription())
                    .append("\n").append("等级:").append(theGuild.getLevel())
                    .append("\n").append("创建日期:").append(theGuild.getCreated().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).format(formatter));
        }
        CQ.sendGroupMsg(GroupID,msg.toString());
        /*
        [Hypixel]玩家***所在公会信息:
        名称:nmslnmsl  Tag:***
        简介:nmslnmsl
        等级:16
        创建日期:*******
        ***在公会信息:
        Rank:
        加入日期:
         */

    }
}
