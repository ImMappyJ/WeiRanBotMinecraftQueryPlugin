package cn.mappyj.event;

import cn.mappyj.utils.CharProcessUtil;
import cn.mappyj.utils.MojangCastUtil;
import cn.mappyj.utils.LanguageUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.reply.PlayerReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class GetPlayerInfo extends AbstractGet {
    protected GetPlayerInfo(long GroupID, CoolQ CQ, HypixelAPI apiKey, BiConsumer<AbstractReply, Throwable> theConsumer,String arg) throws InterruptedException, ExecutionException, IOException {
        super(GroupID,CQ,apiKey,theConsumer,arg);
    }

    @Override
    protected void execute(){
        try{
            CharProcessUtil charProcessUtil = new CharProcessUtil();
            MojangCastUtil mojangCastUtil = new MojangCastUtil();
            UUID uuid = charProcessUtil.stringUUIDToUUID(mojangCastUtil.nametoStringUUID(arg));
            if(isnull(uuid)){
                CQ.sendGroupMsg(GroupID, LanguageUtil.Mojang_InvalidName);
                return;
            }
            PlayerReply player = apiKey.getPlayerByUuid(uuid).whenComplete(theConsumer).get();
            if(player.isSuccess()){
                JsonObject getPlayerJsonObject = player.getPlayer();
                if(isnull(getPlayerJsonObject)){
                    CQ.sendGroupMsg(GroupID, LanguageUtil.Hypixel_InvalidName);
                    return;
                }
                StringBuilder msg = new StringBuilder();
                JsonElement json_NetWorkExp,json_PlayerName,json_VersionRp,json_AchievementPoints,json_NewPackageRank,json_Rank,json_RecentGameType,json_Language,json_LastLogout,json_LastLogin,json_Karma;
                json_AchievementPoints = getPlayerJsonObject.get("achievementPoints");
                json_NetWorkExp = getPlayerJsonObject.get("networkExp");
                json_PlayerName = getPlayerJsonObject.get("displayname");
                json_Karma = getPlayerJsonObject.get("karma");
                json_Language = getPlayerJsonObject.get("userLanguage");
                json_LastLogin = getPlayerJsonObject.get("lastLogin");
                json_LastLogout = getPlayerJsonObject.get("lastLogout");
                json_VersionRp = getPlayerJsonObject.get("mcVersionRp");
                json_RecentGameType = getPlayerJsonObject.get("mostRecentGameType");
                json_NewPackageRank = getPlayerJsonObject.get("newPackageRank");
                json_Rank = getPlayerJsonObject.get("rank");
                /*
                开始判断是否为null
                 */
                String PlayerName,Language,Version,RecentGameType,Rank,isOnline;
                int AchievementPoints,Level,Karma;
                long LastLogin,LastLogout;

                AchievementPoints = isnull(json_AchievementPoints)?0:json_AchievementPoints.getAsInt();
                Karma = isnull(json_Karma)?0:json_Karma.getAsInt();
                Language = isnull(json_Language)?"null":json_Language.getAsString();
                LastLogin = isnull(json_LastLogin)?0:json_LastLogin.getAsLong();
                LastLogout = isnull(json_LastLogout)?0:json_LastLogout.getAsLong();
                Level = isnull(json_NetWorkExp)?0:(int)(((Math.sqrt(json_NetWorkExp.getAsLong()+15312.5))-(125/Math.sqrt(2)))/(25*Math.sqrt(2)));
                PlayerName = isnull(json_PlayerName)?"null":json_PlayerName.getAsString();
                RecentGameType = isnull(json_RecentGameType)? LanguageUtil.NoPermission:json_RecentGameType.getAsString();
                Version = isnull(json_VersionRp)?LanguageUtil.Unknown:json_VersionRp.getAsString();
                isOnline = LastLogout<LastLogin?"Online":"OffLine";
                if(isnull(json_Rank)){if(isnull(json_NewPackageRank)){Rank = "Default";}else{Rank = json_NewPackageRank.getAsString();}}else{Rank = json_Rank.getAsString();}
                /*
                大厅等级:122 人品值:2902855
                头衔:MVP+ 成就点数:2575
                最近游戏类型:SkyWars
                使用语言:Chinese
                最近使用游戏版本:null
                最后登出时间：2020-6-25 11:33:28
                当前在线
                 */
                msg.append("[Hypixel]已查询到玩家").append(PlayerName).append("的信息:\n").append("大厅等级:").append(Level).append(" ").append("人品值:").append(Karma).append("\n").append("头衔:").append(Rank).append(" ").append("成就点数:").append(AchievementPoints).append("\n").append("最近游戏类型:").append(RecentGameType).append("\n").append("使用语言:").append(Language).append("\n").append("客户端版本:").append(Version).append("\n").append("最后登出时间").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(LastLogout))).append("\n").append("当前").append(isOnline);
                CQ.sendGroupMsg(GroupID,msg.toString());
                return;
            }
            CQ.sendGroupMsg(GroupID, LanguageUtil.Hypixel_InvalidKey);
        }catch (NullPointerException | IOException | InterruptedException | ExecutionException e){
            e.printStackTrace();
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
            CQ.logInfo(LanguageUtil.CatchException,e);
        }
    }
}
