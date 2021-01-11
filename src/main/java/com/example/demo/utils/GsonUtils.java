package com.example.demo.utils;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import org.thymeleaf.util.DateUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Gson工具类
 *
 * @author zhanjie.zhang
 * @date 17/9/1
 */
public class GsonUtils {
    private static Gson gson = null;
    /**
     * 忽略掉含有注解  {@link com.google.gson.annotations.Expose}
     */
    private static Gson gsonWithoutExpose = null;
    static {
        gson = new Gson();
        gsonWithoutExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    private GsonUtils() {
    }

    /**
     * 将对象转换成json格式
     *
     * @param ts
     * @return
     */
    public static String objectToJson(Object ts) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }

    public static String object2JsonWithoutExpose(Object o){
        String jsonStr = null;
        if (gsonWithoutExpose != null) {
            jsonStr = gsonWithoutExpose.toJson(o);
        }
        return jsonStr;
    }



    /**
     * 将json格式转换成list对象
     * 用下面的json2List
     * @param jsonStr
     * @return
     */
    @Deprecated
    public static List<?> jsonToList(String jsonStr) {
        List<?> objList = null;
        if (gson != null) {
            Type type = new com.google.common.reflect.TypeToken<List<?>>() {
            }.getType();
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    
    /**
     * jsonarray格式字符串 转化为List<T>
     * @param jsonStr
     * @param token
     * @return
     */
    public static <T> List<T> json2List(String jsonStr, com.google.common.reflect.TypeToken<List<T>> token) {
        List<T> objList = null;
        if (gson != null) {
            Type type = token.getType();
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }
    /**
     * list转化为String
     * @param list
     * @return
     */
    public static String list2String(Object list){
    	String string = null;
    	if(gson != null){
    		string = gson.toJson(list);
    	} 
    	return string;
    } 
    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<?, ?> jsonToMap(String jsonStr) {
        Map<?, ?> objMap = null;
        if (gson != null) {
            Map<String, String> map = new HashMap<String, String>();
            objMap = (Map<String, String>) gson.fromJson(jsonStr, map.getClass());
        }
        return objMap;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(String jsonStr, Class<T> cl) {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
            return (T) obj;
        }
        return null;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(String jsonStr, Type type) {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, type);
            return (T) obj;
        }
        return null;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(String jsonStr, TypeToken<T> token) {
        Object obj = null;
        if (gson != null) {
            Type type = token.getType();
            obj = gson.fromJson(jsonStr, type);
            return (T) obj;
        }
        return null;
    }


    /**
     * 根据
     *
     * @param jsonStr
     * @param key
     * @return
     */
    public static Object getJsonValue(String jsonStr, String key) {
        Object rulsObj = null;
        Map<?, ?> rulsMap = jsonToMap(jsonStr);
        if (rulsMap != null && rulsMap.size() > 0) {
            rulsObj = rulsMap.get(key);
        }
        return rulsObj;
    }

    /**
     * JSON 转MAP或LIST集合
     * @param json 标准JSON格式字符串
     * @return Object （主要是MAP与List集合）
     */
    @SuppressWarnings("unchecked")
    public static Object parse(String json) {
        if(json==null){
            return json;
        }
        JsonElement ele  = new JsonParser().parse(json);
        if  (ele.isJsonObject()){
            Set<Map.Entry<String, JsonElement>> set = ((JsonObject)ele).entrySet();
            Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
            HashMap<String, Object> map = new HashMap<String, Object>();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = iterator.next();
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (!value.isJsonPrimitive()) {
                    map.put(key, parse(value.toString()));
                } else {
                    map.put(key, value.getAsString());
                }
            }
            return map;
        }else if (ele.isJsonArray()){
            JsonArray set = ele.getAsJsonArray();
            Iterator<JsonElement> iterator = set.iterator();
            List list = new ArrayList();
            while (iterator.hasNext()){
                JsonElement entry = iterator.next();
                if (!entry.isJsonPrimitive()) {
                    list.add(parse(entry.toString()));
                } else {
                    list.add(entry.getAsString());
                }
            }
            return list;
        }else if (ele.isJsonPrimitive()){
            return json;
        }
        return null;
    }

    /**
     * JSON 转换MAP 对象
     * @param json 标准字符串
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMapEx2(String json) {
        if(json==null){
            return null;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        JsonElement element = (JsonElement) new JsonParser().parse(json);
        if(element.isJsonObject()){
            JsonObject object = (JsonObject) new JsonParser().parse(json);
            Set<Map.Entry<String, JsonElement>> set = object.entrySet();
            Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = iterator.next();
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if(value.isJsonPrimitive()){
                    map.put(key,value.getAsString());
                }else if(value.isJsonObject()){
                    map.put(key,jsonToMapEx2(value.toString()));
                }else if(value.isJsonArray()){
                    List list = new ArrayList();
                    for(JsonElement arrayElement :value.getAsJsonArray()){
                        if(arrayElement.isJsonPrimitive()){
                            list.add(arrayElement.getAsString());
                        }else if(arrayElement.isJsonObject()){
                            list.add(jsonToMapEx2(arrayElement.toString()));
                        } else if(arrayElement.isJsonNull()){
                            list.add(null);
                        } else if(arrayElement.isJsonArray()){
                            list.add(jsonToMapEx2(arrayElement.toString()));
                        }else{
                            list.add(arrayElement.toString());
                        }
                    }
                    map.put(key,list);
                }else if(value.isJsonNull()){
                    map.put(key,null);
                }else{
                    map.put(key,value.getAsString());
                }
            }
        }
        return map;
    }
}
