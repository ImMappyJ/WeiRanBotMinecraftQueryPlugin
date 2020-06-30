package cn.mappyj.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.processing.FilerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GetFileTextUtil {

    public JsonObject getJson(File file) throws IOException {
        if(!new CheckFileUtil().checkFile(file)){
            return null;
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] temp = new byte[(int) file.length()];
        if (inputStream.read(temp) < file.length()) {
            inputStream.close();
            throw new FilerException("So Large File");
        } else {
            inputStream.close();
            JsonParser parser = new JsonParser();
            JsonElement temp_Object = parser.parse(new String(temp));
            if (temp_Object.isJsonObject()) {
                return temp_Object.getAsJsonObject();
            }
        }
    return null;
    }
}
