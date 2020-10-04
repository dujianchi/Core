//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.dujc.core.cache.glide;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public final class Util {
    private static final int HASH_MULTIPLIER = 31;
    private static final int HASH_ACCUMULATOR = 17;
    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] SHA_256_CHARS = new char[64];

    private Util() {
    }

    @NonNull
    public static String sha256BytesToHex(@NonNull byte[] bytes) {
        synchronized (SHA_256_CHARS) {
            return bytesToHex(bytes, SHA_256_CHARS);
        }
    }

    @NonNull
    private static String bytesToHex(@NonNull byte[] bytes, @NonNull char[] hexChars) {
        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_CHAR_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHAR_ARRAY[v & 15];
        }

        return new String(hexChars);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static int getSize(@NonNull Bitmap bitmap) {
        return getBitmapByteSize(bitmap);
    }

    @TargetApi(19)
    public static int getBitmapByteSize(@NonNull Bitmap bitmap) {
        if (bitmap.isRecycled()) {
            throw new IllegalStateException("Cannot obtain size for recycled Bitmap: " + bitmap + "[" + bitmap.getWidth() + "x" + bitmap.getHeight() + "] " + bitmap.getConfig());
        } else {
            if (VERSION.SDK_INT >= 19) {
                try {
                    return bitmap.getAllocationByteCount();
                } catch (NullPointerException var2) {
                }
            }

            return bitmap.getHeight() * bitmap.getRowBytes();
        }
    }

    public static int getBitmapByteSize(int width, int height, @Nullable Config config) {
        return width * height * getBytesPerPixel(config);
    }

    private static int getBytesPerPixel(@Nullable Config config) {
        if (config == null) {
            config = Config.ARGB_8888;
        }

        byte bytesPerPixel;
        switch (config) {
            case ALPHA_8:
                bytesPerPixel = 1;
                break;
            case RGB_565:
            case ARGB_4444:
                bytesPerPixel = 2;
                break;
            case RGBA_F16:
                bytesPerPixel = 8;
                break;
            case ARGB_8888:
            default:
                bytesPerPixel = 4;
        }

        return bytesPerPixel;
    }

    public static boolean isValidDimensions(int width, int height) {
        return isValidDimension(width) && isValidDimension(height);
    }

    private static boolean isValidDimension(int dimen) {
        return dimen > 0 || dimen == -2147483648;
    }

    public static void assertMainThread() {
        if (!isOnMainThread()) {
            throw new IllegalArgumentException("You must call this method on the main thread");
        }
    }

    public static void assertBackgroundThread() {
        if (!isOnBackgroundThread()) {
            throw new IllegalArgumentException("You must call this method on a background thread");
        }
    }

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

    @NonNull
    public static <T> Queue<T> createQueue(int size) {
        return new ArrayDeque(size);
    }

    @NonNull
    public static <T> List<T> getSnapshot(@NonNull Collection<T> other) {
        List<T> result = new ArrayList(other.size());
        Iterator<T> var2 = other.iterator();

        while (var2.hasNext()) {
            T item = var2.next();
            if (item != null) {
                result.add(item);
            }
        }

        return result;
    }

    public static boolean bothNullOrEqual(@Nullable Object a, @Nullable Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public static boolean bothModelsNullEquivalentOrEquals(@Nullable Object a, @Nullable Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a instanceof Model ? ((Model) a).isEquivalentTo(b) : a.equals(b);
        }
    }

    public static int hashCode(int value) {
        return hashCode(value, 17);
    }

    public static int hashCode(int value, int accumulator) {
        return accumulator * 31 + value;
    }

    public static int hashCode(float value) {
        return hashCode(value, 17);
    }

    public static int hashCode(float value, int accumulator) {
        return hashCode(Float.floatToIntBits(value), accumulator);
    }

    public static int hashCode(@Nullable Object object, int accumulator) {
        return hashCode(object == null ? 0 : object.hashCode(), accumulator);
    }

    public static int hashCode(boolean value, int accumulator) {
        return hashCode(value ? 1 : 0, accumulator);
    }

    public static int hashCode(boolean value) {
        return hashCode(value, 17);
    }
}
