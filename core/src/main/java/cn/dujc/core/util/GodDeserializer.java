package cn.dujc.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

import cn.dujc.core.gson.GodTypeAdapterFactory;

/**
 * 神级的gson反序列化处理类，会处理错误的json类型，取到类型错误也不报错，会继续取到剩余正确的值
 * 用法：Gson gson = new GsonBuilder().registerTypeAdapter(BaseResult.class, new GodDeserializer<BaseResult>()).create()
 *
 * @author du
 * date 2018/7/14 上午11:21
 */
public class GodDeserializer<T> implements JsonDeserializer<T> {

    private final Gson originGson, godGson;

    public GodDeserializer() {
        this(null, false);
    }

    public GodDeserializer(boolean useMinPrimitive) {
        this(null, useMinPrimitive);
    }

    public GodDeserializer(Gson gson, boolean useMinPrimitive) {
        originGson = gson != null ? gson : new Gson();
        godGson = GodTypeAdapterFactory.createBuilder(useMinPrimitive).create();
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            return originGson.fromJson(json, typeOfT);
        } catch (Exception e) {
            try {
                return godGson.fromJson(json, typeOfT);
            } catch (Exception ie) {
                return null;
            }
        }
    }
}
