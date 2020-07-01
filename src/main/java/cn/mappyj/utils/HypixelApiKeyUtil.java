package cn.mappyj.utils;

import cn.mappyj.MinecraftQueryPlugin;
import com.google.gson.*;
import net.hypixel.api.HypixelAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HypixelApiKeyUtil {
    private static JsonObject apiKeyCollection;
    private final static ReadWriteLock RWLOCK= new ReentrantReadWriteLock();
    private final String PATH = MinecraftQueryPlugin.AppDirectory+File.separator+"apiKey.json";
    private final long GroupID;
    private JsonArray apiKeyList;
    static{
        try {
            JsonObject temp = new GetFileTextUtil().getJson(new File(MinecraftQueryPlugin.AppDirectory+File.separator+"apiKey.json"));
            apiKeyCollection = temp != null?temp:new JsonObject();
        } catch (IOException e) {
            apiKeyCollection = new JsonObject();
            e.printStackTrace();
        }
    }

    public HypixelApiKeyUtil(long GroupID){
        this.GroupID = GroupID;
    }

    public int[] checkAllApiKeyStatus() throws IOException {
        /*0:GOOD 1:BAD 2:UNKNOWN */
        int[] status = new int[3];
        apiKeyList = apiKeyCollection.get(Long.toString(GroupID)) != null ? apiKeyCollection.get(Long.toString(GroupID)).getAsJsonArray():new JsonArray();
        if(apiKeyList.size()<=0) return status;
        ExecutorService pool = Executors.newFixedThreadPool(6);
        JsonArray checkedList = new JsonArray();
        List<Future<?>> taskList = new ArrayList<>();
        for(JsonElement perApiKey:apiKeyList){
            taskList.add(pool.submit(()->{
                try {
                    if(checkUsefulApiKey(perApiKey.getAsString())){status[0]++;checkedList.add(perApiKey);} else{status[1]++;}
                } catch (IOException e) {
                    status[2]++;
                    checkedList.add(perApiKey);
                    e.printStackTrace();
                }
            }));
        }
        for(Future<?> task:taskList){
            while(!task.isDone()){}
        }
        pool.shutdown();
        apiKeyCollection.add(Long.toString(GroupID),apiKeyList);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(PATH));
        fileOutputStream.write(new CharProcessUtil().jsonObjectToJsonString(apiKeyCollection).getBytes(StandardCharsets.UTF_8));
        fileOutputStream.flush();
        return status;
    }

    public UUID getRandomApiKey(){
        apiKeyList = apiKeyCollection.get(Long.toString(GroupID)) != null ? apiKeyCollection.get(Long.toString(GroupID)).getAsJsonArray():new JsonArray();
        if(apiKeyList.size()<=0) return null;
        Random random = new Random();
        int index = random.nextInt(apiKeyList.size());
        return UUID.fromString(apiKeyList.get(index).getAsString());
    }

    public boolean addToGroup(String apiKey) throws IOException {
        apiKeyList = apiKeyCollection.get(Long.toString(GroupID)) != null ? apiKeyCollection.get(Long.toString(GroupID)).getAsJsonArray():new JsonArray();
        for(JsonElement PerApiKey:apiKeyList){
            if(PerApiKey.getAsString().equals(apiKey)){
                return false;
            }
        }
        boolean isUsefulApiKey = checkUsefulApiKey(apiKey);
        if(isUsefulApiKey){
            apiKeyList.add(new JsonPrimitive(apiKey));
            apiKeyCollection.add(Long.toString(GroupID),apiKeyList);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(PATH));
            fileOutputStream.write(new CharProcessUtil().jsonObjectToJsonString(apiKeyCollection).getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
        }
        return isUsefulApiKey;
    }

    private boolean checkUsefulApiKey(String apiKey) throws IOException {
        String returnText = new HttpGetTextUtil().getURLText((HttpURLConnection)new URL("https://api.hypixel.net/key?key="+apiKey).openConnection());
        return returnText != null && new JsonParser().parse(returnText).getAsJsonObject().get("success").getAsBoolean();
    }
}
