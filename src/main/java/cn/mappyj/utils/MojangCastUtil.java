package cn.mappyj.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class MojangCastUtil {
    public String nametoStringUUID(String name) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
        HttpURLConnection connToMojang = (HttpURLConnection) url.openConnection();
        HttpGetTextUtil getTextUtil = new HttpGetTextUtil();
        String getText = getTextUtil.getURLText(connToMojang);
        if(getText == null){
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonElement getJsonElement = parser.parse(getText);
        if(getJsonElement.isJsonObject()){
            JsonObject getJsonObject = (JsonObject)getJsonElement;
            String sb = getJsonObject.get("id").getAsString();
            return sb;
        }
        return null;
    }

    public String[] stringUUIDtoNameHistory(String uuid) throws IOException {
        if(Objects.isNull(uuid)) return null;
        URL url = new URL("https://api.mojang.com/user/profiles/"+uuid.toString()+"/names");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        HttpGetTextUtil getTextUtil = new HttpGetTextUtil();
        String getText = getTextUtil.getURLText(conn);
        if(getText == null) return null;
        JsonElement je = new JsonParser().parse(getText);
        String[] names = new String[je.getAsJsonArray().size()];
        int temp = 0;
        while(temp<names.length){
            names[temp] = je.getAsJsonArray().get(names.length-temp-1).getAsJsonObject().get("name").getAsString();
            temp++;
        }
        return names;
    }
}
