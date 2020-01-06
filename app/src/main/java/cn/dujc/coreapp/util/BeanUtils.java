package cn.dujc.coreapp.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BeanUtils {

    public static void copyFiledFromMap(Object obj, Map<String, ?> map) {
        if (obj == null) return;
        List<Field> fields = getFields(obj.getClass());
        for (Field field : fields) {
            SerializedName serializedName = field.getAnnotation(SerializedName.class);
            if (serializedName != null && !TextUtils.isEmpty(serializedName.value())) {
                setFieldValue(field, map.get(serializedName.value()));
            } else {
                setFieldValue(field, map.get(field.getName()));
            }
        }
    }

    public static void setFieldValue(Field field, Object value) {
        if (value == null) return;
        field.setAccessible(true);
        //todo
    }

    @NonNull
    public static List<Field> getFields(Class clazz) {
        List<Field> result = new ArrayList<>();
        if (clazz == null) return result;
        Field[] fields = null;
        try {
            fields = clazz.getDeclaredFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fields != null) result.addAll(Arrays.asList(fields));
        result.addAll(getFields(clazz.getSuperclass()));
        return result;
    }
}
