
package com.demo.project.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestUtils {

    private static ObjectMapper jsonMapper;

    static {
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> T get(String url, Map<String, String> headers, Class<T> responseClass) {
        try {
            if(headers != null) {
                Request request = Request.Get(url);
                Set<String> keys = headers.keySet();
                for(String key : keys) {
                    request.addHeader(key, headers.get(key));
                }
                String respJson = request.execute().returnContent().asString(Consts.UTF_8);
                T response = fromJson(respJson, responseClass);
                return response;
            } else {
                String respJson = Request.Get(url).execute().returnContent().asString();
                T response = fromJson(respJson, responseClass);
                return response;
            }
        } catch(IOException e) {
            throw new RuntimeException("Error when request to api.", e);
        }
    }

    public static <T> T post(String url, Map<String, String> headers, Object body, Class<T> responseClass) {
        String json = toJson(body);
        try {
            Request request = Request.Post(url);

            Set<String> keys = headers.keySet();
            for(String key : keys) {
                request.addHeader(key, headers.get(key));
            }
            String respJson = request.bodyString(json, ContentType.APPLICATION_JSON).execute().returnContent()
                    .asString(Consts.UTF_8);
            T response = fromJson(respJson, responseClass);
            return response;
        } catch(IOException e) {
            throw new RuntimeException("Error when request to api.", e);
        }
    }

    private static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return jsonMapper.readValue(json, clazz);
        } catch(IOException e) {
            throw new RuntimeException("Error when read value from json.", e);
        }
    }

    private static String toJson(Object object) {
        try {
            return jsonMapper.writeValueAsString(object);
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Error when write value as string.", e);
        }
    }

}
