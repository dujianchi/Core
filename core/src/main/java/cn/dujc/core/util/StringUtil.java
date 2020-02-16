package cn.dujc.core.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.IllegalFormatException;
import java.util.Random;

import cn.dujc.core.app.Core;

/**
 * 字符串相关工具
 * Created by du on 2017/10/16.
 */
public class StringUtil {

    private static Random sRandom = null;

    /**
     * 字符串拼接
     *
     * @param objects
     * @return
     */
    public static String concat(Object... objects) {
        return concatWithSeparate(null, objects);
    }

    /**
     * 字符串拼接
     *
     * @param objects
     * @return
     */
    public static String concatWithSeparate(Object separate, Object... objects) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (objects != null) {
            for (Object obj : objects) {
                if (obj != null) {
                    if (separate != null && stringBuilder.length() > 0) {
                        stringBuilder.append(separate);
                    }
                    stringBuilder.append(obj);
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * toString，为null时当作""返回
     */
    @NonNull
    public static String toString(Object object) {
        if (object == null) return "";
        return String.valueOf(object);
    }

    /**
     * 安全的格式化输出，如果参数对不上不会崩溃，而是输出format的内容
     */
    public static String format(String format, Object... args) {
        String result = format;
        try {
            result = String.format(format, args);
        } catch (IllegalFormatException e) {
            if (Core.DEBUG) e.printStackTrace();
        } catch (NullPointerException e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        return result;
    }

    /**
     * 剪切字符串text，直到出现最后一个sub
     */
    public static CharSequence subUntil(StringBuilder text, String sub) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(sub)) return text;
        final int index = text.lastIndexOf(sub);
        if (index > 0) {
            return text.substring(0, index);
        }
        return text;
    }

    /**
     * 将超过@param max的字符串裁掉，max小于1无效
     */
    public static CharSequence maxString(CharSequence text, int max) {
        if (TextUtils.isEmpty(text) || max < 1) return text;
        final int length = text.length();
        return length <= max ? text : text.subSequence(0, max);
    }

    /**
     * 随机生成字符串
     *
     * @param length 生成长度
     */
    public static CharSequence random(int length) {
        return random(length, false);
    }

    /**
     * 随机生成字符串
     *
     * @param length 生成长度
     */
    public static CharSequence random(int length, boolean allNumber) {
        if (length <= 0) return "";
        StringBuilder result = new StringBuilder();
        Random random = getRandom();
        for (int index = 0; index < length; index++) {
            if (allNumber || random.nextBoolean()) {
                result.append(random.nextInt(10));
            } else {
                char c = (char) (random.nextInt(26) + (random.nextBoolean() ? 65 : 97));
                result.append(c);
            }
        }
        return result;
    }

    /**
     * 重复字符串到一定长度
     *
     * @param text   字符串
     * @param length 长度
     */
    public static CharSequence repeat(CharSequence text, int length) {
        if (TextUtils.isEmpty(text)) return text;
        StringBuilder result = new StringBuilder();
        int resLength = 0;
        while (resLength < length) {
            if (length - resLength >= text.length()) {
                result.append(text);
                resLength = result.length();
            } else {
                result.append(text.subSequence(0, length - resLength));
                resLength = length;
            }
        }
        return result;
    }

    /**
     * 替代范围内的字符串
     *
     * @param text        被替代
     * @param start       起始位置，负数则从后往前属
     * @param end         结束位置，负数则从后往前属
     * @param replacement 替换
     */
    public static CharSequence replaceRange(CharSequence text, int start, int end, CharSequence replacement) {
        return replaceRange(text, start, end, replacement, true);
    }

    /**
     * 替代范围内的字符串
     *
     * @param text              被替代
     * @param start             起始位置，负数则从后往前数
     * @param end               结束位置，负数则从后往前数
     * @param replacement       替换
     * @param repeatReplacement 是否重复到与原字符串相等长度
     */
    public static CharSequence replaceRange(CharSequence text, int start, int end, CharSequence replacement, boolean repeatReplacement) {
        if (TextUtils.isEmpty(text)) return text;
        final int length = text.length();
        if (start > length) {
            start = 0;
        } else if (start < 0) {
            start = length + start;
        }
        if (end > length) {
            end = length;
        } else if (end < 0) {
            end = length + end;
        }
        if (end < start) {
            return text;
        } else if (start < 0 || end < 0 || start >= end) {
            return text;
        }
        CharSequence head, body, tail;
        head = text.subSequence(0, start);
        body = replacement == null ? "" : repeatReplacement ? repeat(replacement, end - start) : replacement;//text.subSequence(start, end);
        tail = text.subSequence(end, length);
        return new StringBuilder(head).append(body).append(tail);
    }

    /**
     * 裁切字符串
     *
     * @param text  被替代
     * @param start 起始位置，负数则从后往前数
     * @param end   结束位置，负数则从后往前数
     */
    public static CharSequence subSequence(CharSequence text, int start, int end) {
        if (TextUtils.isEmpty(text)) return text;
        final int length = text.length();
        if (start > length) {
            start = 0;
        } else if (start < 0) {
            start = length + start;
        }
        if (end > length) {
            end = length;
        } else if (end < 0) {
            end = length + end;
        }
        if (end < start) {
            return text;
        } else if (start < 0 || end < 0 || start >= end) {
            return text;
        }
        return text.subSequence(start, end);
    }

    /**
     * 判断文本中是否所有字符都一样
     *
     * @param text 文本
     * @return 文本为空或者全一样返回 true
     */
    public static boolean isAllSame(CharSequence text) {
        if (TextUtils.isEmpty(text)) return true;
        char first = text.charAt(0);
        for (int index = 0, length = text.length(); index < length; index++) {
            if (first != text.charAt(index)) return false;
        }
        return true;
    }

    /**
     * 文本是否为空
     *
     * @param text 文本
     * @return
     */
    public static boolean isBlank(CharSequence text) {
        if (TextUtils.isEmpty(text)) return true;
        return text.toString().trim().isEmpty();
    }

    private static Random getRandom() {
        if (sRandom == null) {
            synchronized (StringUtil.class) {
                if (sRandom == null) {
                    sRandom = new Random();
                }
            }
        }
        return sRandom;
    }
}
