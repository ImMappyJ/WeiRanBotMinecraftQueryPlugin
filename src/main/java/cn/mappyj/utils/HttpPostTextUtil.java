package cn.mappyj.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpPostTextUtil {

    public String postURLText(HttpURLConnection conn,String postStr) throws IOException {
        conn.setRequestMethod("POST");
        conn.setUseCaches(true);
        conn.setConnectTimeout(10000);
        conn.addRequestProperty("Content-Type","application/json;charset=UTF-8*");
        conn.addRequestProperty("Contect-Length",String.valueOf(postStr.length()));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postStr.getBytes(StandardCharsets.UTF_8));
        if(conn.getResponseCode() != 200){
            return null;
        }
        byte[] bytes = new byte[conn.getContentLength()];
        conn.getInputStream().read(bytes);
        return new String(bytes);
    }
}
