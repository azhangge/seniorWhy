package com.nd.auxo.recommend.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.core.utils.jackson.DefaultObjectMapper;
import lombok.Data;

import java.io.IOException;

/**
 * Created by jsc on 14-7-3.
 */
public class JsonUtils extends ObjectUtils {

    // json对象处理
    private static ObjectMapper objectMapper = new DefaultObjectMapper();


    private JsonUtils() {
    }

    /**
     * 将json中的属性增量更新到object的字段
     * @param object
     * @param json
     * @throws IOException
     */
    public static void update(Object object, String json) throws IOException {
        objectMapper.readerForUpdating(object).readValue(json);
    }
}
