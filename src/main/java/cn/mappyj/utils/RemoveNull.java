package cn.mappyj.utils;

import java.util.ArrayList;
import java.util.List;

public class RemoveNull{
    public String[] deleteArrayNull(String[] arr){
        List<String> Tarr = new ArrayList<>();
        for(String a:arr){
            Tarr.add(a);
            Tarr.remove(null);
        }
        String[] arrs = new String[Tarr.size()];
        Tarr.toArray(arrs);
        return arrs;
    }
}
