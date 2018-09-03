package com.nd.auxo.recommend.core.util;

import com.nd.gaea.core.utils.ObjectUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jsc on 15-2-6.
 */
public class RedisTemplateUtil<T> extends StringRedisTemplate {

    public RedisTemplateUtil(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public void set(String key, T value) {
        opsForValue().set(key, ObjectUtils.toJson(value));
    }

    public void set(String key, T value, long timeout, TimeUnit unit){
        opsForValue().set(key, ObjectUtils.toJson(value), timeout, unit);
    }

    public T get(String key, Class vClass) {
        String value = opsForValue().get(key);
        return (T) ObjectUtils.fromJson(value, vClass);
    }

    public void hMSet(String key, Map<String, Object> value) {
        if (value != null && value.isEmpty()) {
            Map<String, String> valueMap = new LinkedHashMap<String, String>();
            for (Map.Entry<String, Object> entry : value.entrySet()) {
                valueMap.put(entry.getKey(), ObjectUtils.toJson(entry.getValue()));
            }
            opsForHash().putAll(key, valueMap);
        }
    }

    public Map<String, T> hGetAll(String key, Class vClass) {
        Map<String, T> valueMap = new LinkedHashMap<String, T>();
        for (Map.Entry<Object, Object> entry : opsForHash().entries(key).entrySet()) {
            valueMap.put(entry.getKey().toString(), (T) ObjectUtils.fromJson(entry.getValue().toString(), vClass));
        }
        return valueMap;
    }

    public void hSet(String key, String hashKey, T value) {
        opsForHash().put(key, hashKey, ObjectUtils.toJson(value));
    }

    public T hGet(String key, String hashKey, Class vClass) {
        String value = (String) opsForHash().get(key, hashKey);
        return (T) ObjectUtils.fromJson(value, vClass);
    }

}
