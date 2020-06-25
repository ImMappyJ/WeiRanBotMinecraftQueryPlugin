package cn.mappyj.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

public class HttpGetTextUtil {
    public String getURLText(HttpURLConnection conn) throws IOException {
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.setConnectTimeout(10000);
        conn.addRequestProperty("Accept","*/*");
        conn.addRequestProperty("Connection","keep-alive");
        conn.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3761.400 QQBrowser/10.6.4121.400");
        if(conn.getResponseCode() != 200){
            return null;
        }
        byte[] bytes = new byte[conn.getContentLength()];
        conn.getInputStream().read(bytes);
        return new String(bytes);
    }
}
