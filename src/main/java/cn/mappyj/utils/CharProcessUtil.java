package cn.mappyj.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharProcessUtil {
    public UUID stringUUIDToUUID(String text){
        if(text != null){
            StringJoiner sj = new StringJoiner("-");
            sj.add(text.substring(0,8));
            sj.add(text.substring(8,12));
            sj.add(text.substring(12,16));
            sj.add(text.substring(16,20));
            sj.add(text.substring(20,32));
            return UUID.fromString(sj.toString());
        }
        return null;
    }

    public String jsonObjectToJsonString(JsonObject o){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(o);
    }
}
