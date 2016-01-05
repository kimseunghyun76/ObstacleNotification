package com.ksh.monitor.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-04
 * Time : 오후 3:13
 * To change this template use File | Settings | File and Code Templates.
 */
public class JsonUtils {
    /**
     * ObjectMapper provides functionality for reading and writing JSON,
     either to and from basic POJOs (Plain Old Java Objects), or to and from
     a general-purpose JSON Tree Model ({@link JsonNode}), as well as
     related functionality for performing conversions.
     */
    private final ObjectMapper mapper;

    public JsonUtils() {
        this.mapper =new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, true);
        this.mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true);
        this.mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true);
    }

    public static JsonUtils getInstance(){
        return new JsonUtils();
    }

    private static ObjectMapper getMapper(){
        return getInstance().mapper;
    }

    public static String toJson(Object object){
        try{
            return getMapper().writeValueAsString(object);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String jsonStr, Class<T> cls){
        try{
            return getMapper().readValue(jsonStr,cls);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> typeReference){
        try{
            return getMapper().readValue(jsonStr,typeReference);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static JsonNode fromJson(String json) throws Exception{
        try{
            return getMapper().readTree(json);
        }catch(IOException e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static <T extends Collection> T fromJson(String jsonStr, CollectionType collectionType){
        try{
            return getMapper().readValue(jsonStr, collectionType);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String toPrettyJson(String json){
        Object jsonObject = JsonUtils.fromJson(json, Object.class);

        try{
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "";
    }
}
