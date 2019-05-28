package com.epsit.facadedemo.util;

import android.text.TextUtils;

import com.epsit.facadedemo.FacadeNetwork;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    HashMap map = new HashMap();
    public static String appendParams(String url,Map<String,Object> params){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        if(params==null || params.size()<=0){
            return url;
        }
        if(url.endsWith("\\") || url.endsWith("/")){
            url = url.substring(0, url.length()-1);
        }
        StringBuffer stringBuffer = new StringBuffer(url).append("?");
        for (Map.Entry<String,Object> e : params.entrySet()) {
            String key = e.getKey();
            String value = e.getValue()!=null ? e.getValue().toString() :"";
            stringBuffer.append(key).append("=").append(value).append("&");
        }
        url = stringBuffer.toString();
        if(url.endsWith("&")){
            url = url.substring(0,url.length()-1);
        }
        return url;
    }
    public static <T> Class<T> getClassByClass(FacadeNetwork.Callback<T> object){
        Type[] types = object.getClass().getGenericInterfaces();
        ParameterizedType parameterized = (ParameterizedType) types[0];
        Class<T> clazz = (Class<T>) parameterized.getActualTypeArguments()[0];
        return clazz;
    }
}
