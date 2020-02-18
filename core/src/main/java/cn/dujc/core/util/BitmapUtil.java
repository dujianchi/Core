package cn.dujc.core.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.dujc.core.app.Core;

public class BitmapUtil {

    @NonNull
    public static byte[] toBytes(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(compressFormat, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();
            return byteArray;
        }
        return new byte[0];
    }

    @Nullable
    public static Bitmap fromBytes(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    /**
     * 获取bitmap的真正大小
     *
     * @param bitmap 要测量的bitmap
     * @return 真正大小 in byte
     */
    public static int getBitmapSizeInByte(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) return 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * 压缩图片到缓存目录
     */
    @Nullable
    public static File createCompressedCacheFile(Context context, Uri uri, int sizeInKb) {
        return BitmapUtil.createCompressedCacheFile(context, uri, sizeInKb, Bitmap.CompressFormat.JPEG);
    }

    /**
     * 压缩图片到缓存目录
     */
    @Nullable
    public static File createCompressedCacheFile(Context context, Uri uri, int sizeInKb, Bitmap.CompressFormat format) {
        if (context == null) return null;
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) cacheDir = context.getCacheDir();
        File cacheFile = new File(cacheDir, System.currentTimeMillis() + (Bitmap.CompressFormat.PNG == format ? ".png" : Bitmap.CompressFormat.WEBP == format ? ".webp" : ".jpg"));
        Bitmap bitmap = bitmapFromUri(context, uri);
        bitmap = BitmapUtil.shrinkImage(bitmap, sizeInKb, true);
        BitmapUtil.saveBitmapToFile(bitmap, cacheFile, format, 100, true);
        return cacheFile;
    }

    /**
     * 获取图片后缀
     */
    public static String extSuffix(InputStream input) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            return options.outMimeType.replace("image/", ".");
        } catch (Exception e) {
            return ".jpg";
        }
    }

    /**
     * 从uri读取图片
     */
    @Nullable
    public static Bitmap bitmapFromUri(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
            InputStream stream = readUri(context, uri);
            int orientation = getOrientation(stream);
            bitmap = rotateBitmap(bitmap, orientation);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 收缩bitmap大小
     *
     * @param src      原bitmap
     * @param sizeInKb 微信限制32k
     * @return 新的bitmap
     */
    @Nullable
    public static Bitmap shrinkImage(Bitmap src, int sizeInKb, boolean recycle) {
        if (src == null || src.isRecycled()) return null;

        final int sizeInByte = getBitmapSizeInByte(src);
        final int byteForKb = sizeInKb * 1024;
        LogUtil.d("------   zoom before is " + sizeInByte);
        if (sizeInByte > byteForKb) {//若图片大小大于目的大小，则压缩大小97%，压缩质量85%
            float zoom = (float) Math.sqrt(byteForKb * 1.0 / sizeInByte);
            if (zoom >= 0.97) zoom = 0.97F;
            LogUtil.d("------   zoom level is " + zoom);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.JPEG, 85, out);
            Matrix matrix = new Matrix();
            matrix.setScale(zoom, zoom);
            Bitmap result = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

            if (recycle) src.recycle();//我自己加的，源图片好像已经没用了，可以回收

            out.reset();
            matrix.reset();
            try {
                out.close();
            } catch (Exception e) {
                if (Core.DEBUG) e.printStackTrace();
            }
            return shrinkImage(result, sizeInKb, true);//中间产生的过渡图片都可以回收
        }
        return src;
    }

    /**
     * 保存bitmap到指定路径
     */
    @NonNull
    public static String saveBitmapToFile(Bitmap bitmap, File file) {
        return saveBitmapToFile(bitmap, file, Bitmap.CompressFormat.JPEG, 100, true);
    }

    /**
     * 保存bitmap到指定路径
     */
    @NonNull
    public static String saveBitmapToFile(Bitmap bitmap, String file, Bitmap.CompressFormat format, int quality, boolean recycle) {
        return saveBitmapToFile(bitmap, new File(file), format, quality, recycle);
    }

    /**
     * 保存bitmap到指定路径
     */
    @NonNull
    public static String saveBitmapToFile(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality, boolean recycle) {
        if (file.exists())
            file.delete();
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            if (bitmap != null)
                bitmap.compress(format, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            if (Core.DEBUG) e.printStackTrace();
        } catch (IOException e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        if (recycle && bitmap != null)//此处不能回收bitmap，否则后面会不能用
            bitmap.recycle();
        return file.getPath();
    }

    /**
     * 图片处理
     * 从路径中解析bitmap并压缩图片大小，优先判断短边，当图片最短边大于shortEdge则缩小图片，否则当最长边大于longEdge则压缩图片，再否则就不压大小
     */
    @Nullable
    public static Bitmap decodeSmallerFromFile(String path, int shortEdge, int longEdge) {
        return decodeSmallerFromFile(new File(path), shortEdge, longEdge);
    }

    /**
     * 图片处理
     * 从路径中解析bitmap并压缩图片大小，优先判断短边，当图片最短边大于shortEdge则缩小图片，否则当最长边大于longEdge则压缩图片，再否则就不压大小
     */
    @Nullable
    public static Bitmap decodeSmallerFromFile(File file, int shortEdge, int longEdge) {
        LogUtil.d("---------------   path =     " + file);
        if (file == null) {
            LogUtil.e("decode bitmap error, course path is null");
            return null;
        }
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return decodeSmallerFromInputStream(is, shortEdge, longEdge);
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    if (Core.DEBUG) e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 图片处理
     * 从路径中解析bitmap并压缩图片大小，优先判断短边，当图片最短边大于shortEdge则缩小图片，否则当最长边大于longEdge则压缩图片，再否则就不压大小
     */
    @Nullable
    public static Bitmap decodeSmallerFromUri(Context context, Uri file, int shortEdge, int longEdge) {
        LogUtil.d("---------------   path =     " + file);
        if (file == null || context == null) {
            LogUtil.e("decode bitmap error, course path is null");
            return null;
        }
        InputStream is = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            is = resolver.openInputStream(file);
            return decodeSmallerFromInputStream(is, shortEdge, longEdge);
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    if (Core.DEBUG) e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 图片处理
     * 从路径中解析bitmap并压缩图片大小，优先判断短边，当图片最短边大于shortEdge则缩小图片，否则当最长边大于longEdge则压缩图片，再否则就不压大小
     */
    @Nullable
    public static Bitmap decodeSmallerFromInputStream(InputStream is, int shortEdge, int longEdge) {
        if (is == null) {
            LogUtil.e("decode bitmap error, course path is null");
            return null;
        }
        //------------- decode now --------------
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        try {
            if (is instanceof FileInputStream) {
                bitmap = BitmapFactory.decodeFileDescriptor(((FileInputStream) is).getFD(), null, newOpts);
            } else {
                bitmap = BitmapFactory.decodeStream(is, null, newOpts);
            }
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            newOpts.inPurgeable = true;
            newOpts.inInputShareable = true;
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//4.4以下手机降低bitmap色彩，降低oom发生概率 by djc 2015年11月30日 10:41:19
        }
        newOpts.inDither = false;
        newOpts.inJustDecodeBounds = false;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (shortEdge > 0) {
            int edge = Math.min(newOpts.outWidth, newOpts.outHeight);
            if (edge > shortEdge) {
                newOpts.inSampleSize = (int) (edge * 1f / shortEdge + 0.5f);
            }
        } else if (longEdge > 0) {
            int edge = Math.max(newOpts.outWidth, newOpts.outHeight);
            if (edge > longEdge) {
                newOpts.inSampleSize = (int) (edge * 1f / longEdge + 0.5f);
            }
        }
        if (bitmap != null)
            bitmap.recycle();
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        try {
            if (is instanceof FileInputStream) {
                bitmap = BitmapFactory.decodeFileDescriptor(((FileInputStream) is).getFD(), null, newOpts);  //bitmap = BitmapFactory.decodeFile(path, newOpts);
            } else {
                bitmap = BitmapFactory.decodeStream(is, null, newOpts);  //bitmap = BitmapFactory.decodeFile(path, newOpts);
            }
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                if (Core.DEBUG) e.printStackTrace();
            }
        }
        newOpts = null;
        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param uri 图片
     * @return degree旋转的角度
     */
    @Nullable
    public static InputStream readUri(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        return null;
    }

    // -------------------------- 来自Luban --------------------------

    /**
     * 读取图片属性：旋转的角度
     *
     * @return degree旋转的角度
     */
    public static int getOrientation(InputStream is) {
        return getOrientation(toByteArray(is));
    }

    private static byte[] toByteArray(InputStream is) {
        if (is == null) {
            return new byte[0];
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int read;
        byte[] data = new byte[4096];

        try {
            while ((read = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
        } catch (Exception ignored) {
            return new byte[0];
        } finally {
            try {
                buffer.close();
            } catch (IOException ignored) {
            }
        }

        return buffer.toByteArray();
    }

    private static int getOrientation(byte[] jpeg) {
        if (jpeg == null) {
            return 0;
        }

        int offset = 0;
        int length = 0;

        // ISO/IEC 10918-1:1993(E)
        while (offset + 3 < jpeg.length && (jpeg[offset++] & 0xFF) == 0xFF) {
            int marker = jpeg[offset] & 0xFF;

            // Check if the marker is a padding.
            if (marker == 0xFF) {
                continue;
            }
            offset++;

            // Check if the marker is SOI or TEM.
            if (marker == 0xD8 || marker == 0x01) {
                continue;
            }
            // Check if the marker is EOI or SOS.
            if (marker == 0xD9 || marker == 0xDA) {
                break;
            }

            // Get the length and check if it is reasonable.
            length = pack(jpeg, offset, 2, false);
            if (length < 2 || offset + length > jpeg.length) {
                LogUtil.e("Invalid length");
                return 0;
            }

            // Break if the marker is EXIF in APP1.
            if (marker == 0xE1 && length >= 8
                    && pack(jpeg, offset + 2, 4, false) == 0x45786966
                    && pack(jpeg, offset + 6, 2, false) == 0) {
                offset += 8;
                length -= 8;
                break;
            }

            // Skip other markers.
            offset += length;
            length = 0;
        }

        // JEITA CP-3451 Exif Version 2.2
        if (length > 8) {
            // Identify the byte order.
            int tag = pack(jpeg, offset, 4, false);
            if (tag != 0x49492A00 && tag != 0x4D4D002A) {
                LogUtil.e("Invalid byte order");
                return 0;
            }
            boolean littleEndian = (tag == 0x49492A00);

            // Get the offset and check if it is reasonable.
            int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
            if (count < 10 || count > length) {
                LogUtil.e("Invalid offset");
                return 0;
            }
            offset += count;
            length -= count;

            // Get the count and go through all the elements.
            count = pack(jpeg, offset - 2, 2, littleEndian);
            while (count-- > 0 && length >= 12) {
                // Get the tag and check if it is orientation.
                tag = pack(jpeg, offset, 2, littleEndian);
                if (tag == 0x0112) {
                    int orientation = pack(jpeg, offset + 8, 2, littleEndian);
                    switch (orientation) {
                        case 1:
                            return 0;
                        case 3:
                            return 180;
                        case 6:
                            return 90;
                        case 8:
                            return 270;
                    }
                    LogUtil.e("Unsupported orientation");
                    return 0;
                }
                offset += 12;
                length -= 12;
            }
        }

        LogUtil.e("Orientation not found");
        return 0;
    }

    private static int pack(byte[] bytes, int offset, int length, boolean littleEndian) {
        int step = 1;
        if (littleEndian) {
            offset += length - 1;
            step = -1;
        }

        int value = 0;
        while (length-- > 0) {
            value = (value << 8) | (bytes[offset] & 0xFF);
            offset += step;
        }
        return value;
    }
    // -------------------------- 来自Luban --------------------------

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            degree = 0;
        }
        return degree;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param inputStream 图片
     * @return degree旋转的角度
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static int readPictureDegree(InputStream inputStream) {
        if (inputStream == null) {
            return 0;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(inputStream);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            degree = 0;
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    @Nullable
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bmp != null) {
            bitmap.recycle();
        } else {
            return bitmap;
        }
        return bmp;
    }

    /**
     * 把一个view保存成图片，比如用在地图的自定义marker
     */
    @Nullable
    public static Bitmap viewToBitmap(View view) {
        if (view == null) return null;
        //build bitmap
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        view.layout(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height
                , Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT
                        ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.ARGB_4444);//只有8888可以选了，4444的只有4.4以下可用，565没有透明度，8没有颜色
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}
