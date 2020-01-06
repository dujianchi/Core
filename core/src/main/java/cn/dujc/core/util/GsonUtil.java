package cn.dujc.core.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

import cn.dujc.core.app.Core;

/**
 * json转换工具
 * Created by du on 2017/9/22.
 */
public class GsonUtil {
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()//排除transient标记的字段
            .create();

    private GsonUtil() {
    }

    @NonNull
    public static String toJsonString(Object o) {
        return GSON.toJson(o);
    }

    @NonNull
    public static String toJsonString(Object o, Type type) {
        return GSON.toJson(o, type);
    }

    @Nullable
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return GSON.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            if (Core.DEBUG) e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static <T> T fromJson(String json, Type type) {
        try {
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            if (Core.DEBUG) e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static <T> T fromJson(JsonElement json, Class<T> clazz) {
        try {
            return GSON.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            if (Core.DEBUG) e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static <T> T fromJson(JsonElement json, Type type) {
        try {
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            if (Core.DEBUG) e.printStackTrace();
            return null;
        }
    }

    @NonNull
    public static JsonElement toJsonTree(Object o) {
        return GSON.toJsonTree(o);
    }

    @NonNull
    public static JsonElement toJsonTree(Object o, Type type) {
        return GSON.toJsonTree(o, type);
    }
}
