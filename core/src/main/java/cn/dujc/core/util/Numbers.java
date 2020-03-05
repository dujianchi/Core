package cn.dujc.core.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;

import cn.dujc.core.app.Core;

/**
 * @author du
 * date 2018/7/29 下午9:26
 */
public class Numbers {

    private Numbers() {
    }

    /**
     * 字符串转int
     *
     * @return 默认值 0
     */
    public static int toInt(Object numberStr) {
        return toInt(numberStr, 0);
    }

    /**
     * 字符串转int
     */
    public static int toInt(Object numberStr, int defaultValue) {
        try {
            return numberFromString(numberStr, defaultValue * 1.0).intValue();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 字符串转long
     */
    public static long toLong(Object numberStr) {
        return toLong(numberStr, 0);
    }

    /**
     * 字符串转long
     */
    public static long toLong(Object numberStr, long defaultValue) {
        try {
            return numberFromString(numberStr, defaultValue * 1.0).longValue();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 字符串转double
     *
     * @return 默认值 0
     */
    public static double toDouble(Object numberStr) {
        return toDouble(numberStr, 0);
    }

    /**
     * 字符串转double
     */
    public static double toDouble(Object numberStr, double defaultValue) {
        try {
            return numberFromString(numberStr, defaultValue).doubleValue();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 字符串转float
     *
     * @return 默认值 0
     */
    public static float toFloat(Object numberStr) {
        return toFloat(numberStr, 0);
    }

    /**
     * 字符串转float
     */
    public static float toFloat(Object numberStr, float defaultValue) {
        try {
            return numberFromString(numberStr, (double) defaultValue).floatValue();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 两个数相乘
     *
     * @param multiplicand 被乘数
     * @param multiplier   乘数
     */
    public static String multiply(Object multiplicand, Object multiplier, int scale) {
        if (scale < 0) scale = 0;
        try {
            final BigDecimal multiplicandB = numberFromString(multiplicand, null);
            final BigDecimal multiplierB = numberFromString(multiplier, null);
            return multiplicandB.multiply(multiplierB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return "";
        }
    }

    /**
     * 两个数相减
     *
     * @param minuend    被减数
     * @param subtrahend 减数
     */
    public static String subtract(Object minuend, Object subtrahend, int scale) {
        if (scale < 0) scale = 0;
        try {
            final BigDecimal minuendB = numberFromString(minuend, null);
            final BigDecimal subtrahendB = numberFromString(subtrahend, null);
            return minuendB.subtract(subtrahendB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return "";
        }
    }

    /**
     * 两个数相加
     *
     * @param augend 被加数
     * @param addend 加数
     */
    public static String add(Object augend, Object addend) {
        return add(augend, addend, 2);
    }

    /**
     * 两个数相加
     *
     * @param augend 被加数
     * @param addend 加数
     */
    public static String add(Object augend, Object addend, int scale) {
        if (scale < 0) scale = 0;
        try {
            final BigDecimal augendB = numberFromString(augend, null);
            final BigDecimal addendB = numberFromString(addend, null);
            return augendB.add(addendB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return "";
        }
    }

    /**
     * 两个数相除
     *
     * @param dividend 被除数
     * @param divisor  除数
     */
    public static String divide(Object dividend, Object divisor) {
        return divide(dividend, divisor, 2);
    }

    /**
     * 两个数相除
     *
     * @param dividend 被除数
     * @param divisor  除数
     */
    public static String divide(Object dividend, Object divisor, int scale) {
        if (scale < 0) scale = 0;
        try {
            final BigDecimal dividendB = numberFromString(dividend, null);
            final BigDecimal divisorB = numberFromString(divisor, 1.0);
            return dividendB.divide(divisorB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return "";
        }
    }

    /**
     * 格式化float格式
     */
    @NonNull
    public static String formatFloat(float price, int scale) {
        return new BigDecimal(price).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 格式化double格式
     */
    @NonNull
    public static String formatDouble(double price, int scale) {
        return new BigDecimal(price).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 格式化double格式
     */
    @Nullable
    public static String formatDouble(Object number, int scale) {
        BigDecimal decimal = numberFromString(number, 0.0);
        return decimal != null ? decimal.setScale(scale, BigDecimal.ROUND_HALF_UP).toString() : null;
    }

    /**
     * 将字符串转为BigDecimal高精度浮点数
     */
    @Nullable
    public static BigDecimal numberFromString(Object valueStr, Double defaultIfError) {
        BigDecimal result = defaultIfError != null ? new BigDecimal(defaultIfError) : null;
        return toNumber(valueStr, result);
    }

    /**
     * 将字符串转为BigDecimal高精度浮点数
     */
    @Nullable
    public static BigDecimal toNumber(Object valueStr, @Nullable BigDecimal defaultIfError) {
        BigDecimal result = defaultIfError;
        if (valueStr == null || "".equals(valueStr)) return result;
        try {
            String number = String.valueOf(valueStr);
            if (number.contains(",")) number = number.replace(",", "");
            if (number.contains(" ")) number = number.replace(" ", "");
            result = new BigDecimal(number);
        } catch (NumberFormatException e) {
            if (Core.DEBUG) e.printStackTrace();
        } catch (NullPointerException e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        return result;
    }
}
