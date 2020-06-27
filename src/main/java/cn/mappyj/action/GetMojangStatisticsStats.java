package cn.mappyj.action;

import cn.mappyj.utils.HttpPostTextUtil;
import cn.mappyj.utils.LanguageUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Objects;

public class GetMojangStatisticsStats {
    StringBuilder msg = new StringBuilder();
    JsonObject postStr = new JsonObject(),returnStr = new JsonObject();
    JsonArray type = new JsonArray();
    JsonParser parser = new JsonParser();
    JsonElement json_total,json_last24h,json_perSec;
    int total,last24h;
    String returned;
    double perGameneedSec,perSec;
    HttpPostTextUtil postTextUtil = new HttpPostTextUtil();
    CoolQ CQ;
    long GroupID;
    NumberFormat format = NumberFormat.getInstance();

    public GetMojangStatisticsStats(CoolQ CQ,long GroupID,String game) throws IOException {
        format.setMaximumFractionDigits(2);
        this.CQ = CQ;
        this.GroupID = GroupID;
        switch (game){
            case"minecraft":
                minecraft();
                break;
            case "minecraft_dungeon":
                minecraft_dungeon();
                break;
        }
    }

    private void minecraft_dungeon() throws IOException{
        HttpURLConnection conn = (HttpURLConnection)new URL("https://api.mojang.com/orders/statistics").openConnection();
        type.add(parser.parse("item_sold_dungeons"));
        postStr.add("metricKeys",(JsonElement) type);
        returned = postTextUtil.postURLText(conn, postStr.toString());
        if(Objects.isNull(returned)){CQ.sendGroupMsg(GroupID, LanguageUtil.CatchException); return;}
        returnStr = parser.parse(returned).getAsJsonObject();

        json_total = returnStr.get("total");
        json_last24h = returnStr.get("last24h");
        json_perSec = returnStr.get("saleVelocityPerSeconds");

        total = json_total == null?0:json_total.getAsInt();
        last24h = json_last24h == null?0:json_last24h.getAsInt();
        perSec = json_perSec == null?0:json_perSec.getAsDouble();
        perGameneedSec = 1/perSec;
        /*
        Minecraft Dungeon销量统计:
        迄今为止共售出36140370份
        在24小时内共售出16390份
        平均每5秒售出一份
         */
        msg.append("Minecraft Dungeon销量统计:" + "\n" + "迄今为止共售出")
                .append(total).append("份")
                .append("\n").append("在24小时内共售出").append(last24h).append("份")
                .append("\n").append("平均每").append(format.format(perGameneedSec)).append("秒售出一份");

        CQ.sendGroupMsg(GroupID,msg.toString());
    }

    private void minecraft() throws IOException {
        HttpURLConnection conn = (HttpURLConnection)new URL("https://api.mojang.com/orders/statistics").openConnection();
        type.add(parser.parse("item_sold_minecraft"));
        type.add(parser.parse("prepaid_card_redeemed_minecraft"));
        postStr.add("metricKeys",(JsonElement) type);
        returned = postTextUtil.postURLText(conn, postStr.toString());
        if(Objects.isNull(returned)){CQ.sendGroupMsg(GroupID, LanguageUtil.CatchException); return;}
        returnStr = parser.parse(returned).getAsJsonObject();

        json_total = returnStr.get("total");
        json_last24h = returnStr.get("last24h");
        json_perSec = returnStr.get("saleVelocityPerSeconds");

        total = json_total == null?0:json_total.getAsInt();
        last24h = json_last24h == null?0:json_last24h.getAsInt();
        perSec = json_perSec == null?0:json_perSec.getAsDouble();
        perGameneedSec = 1/perSec;
        /*
        Minecraft销量统计:
        迄今为止共售出36140370份
        在24小时内共售出16390份
        平均每5秒售出一份
         */
        msg.append("Minecraft JavaEdition销量统计:" + "\n" + "迄今为止共售出")
                .append(total).append("份")
                .append("\n").append("在24小时内共售出").append(last24h).append("份")
                .append("\n").append("平均每").append(format.format(perGameneedSec)).append("秒售出一份");

        CQ.sendGroupMsg(GroupID,msg.toString());
    }
}
