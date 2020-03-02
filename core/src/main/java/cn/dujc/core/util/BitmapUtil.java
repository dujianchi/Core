package cn.dujc.core.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.dujc.core.app.Core;

public class BitmapUtil {

    @NonNull
    public static byte[] toBytes(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(compressFormat, 100, stream);
            byte[] byteArray = stream.toByteArray();
            if (!bitmap.isRecycled()) bitmap.recycle();
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
     * 压缩图片到缓存目录，依据最短边压缩，无法压缩到准确宽高，只能最接近，顺带压缩质量
     */
    @Nullable
    public static File createCompressedCacheFile(Context context, Uri uri, int minEdge) {
        return BitmapUtil.createCompressedCacheFile(context, uri, minEdge, Bitmap.CompressFormat.JPEG, null);
    }

    /**
     * 压缩图片到缓存目录，依据最短边压缩，无法压缩到准确宽高，只能最接近，顺带压缩质量
     */
    @Nullable
    public static File createCompressedCacheFile(Context context, Uri uri, int minEdge, Bitmap.CompressFormat format) {
        return BitmapUtil.createCompressedCacheFile(context, uri, minEdge, format, null);
    }

    /**
     * 压缩图片到缓存目录，依据最短边压缩，无法压缩到准确宽高，只能最接近，顺带压缩质量
     */
    @Nullable
    public static File createCompressedCacheFile(Context context, Uri uri, int minEdge, String authority) {
        return BitmapUtil.createCompressedCacheFile(context, uri, minEdge, Bitmap.CompressFormat.JPEG, authority);
    }

    /**
     * 压缩图片到缓存目录，依据最短边压缩，无法压缩到准确宽高，只能最接近，顺带压缩质量
     */
    @Nullable
    public static File createCompressedCacheFile(Context context, Uri uri, int minEdge, Bitmap.CompressFormat format, String authority) {
        return BitmapUtil.createCompressedCacheFile(context, uri, minEdge, 100, format, null);
    }

    /**
     * 压缩图片到缓存目录，依据最短边压缩，无法压缩到准确宽高，只能最接近，顺带压缩质量
     *
     * @param quality 质量大于100则为100，小于0则默认85
     */
    @Nullable
    public static File createCompressedCacheFile(Context context, Uri uri, int minEdge, int quality, Bitmap.CompressFormat format, String authority) {
        if (context == null) return null;
        if (quality <= 0) quality = 85;
        else if (quality > 100) quality = 100;
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) cacheDir = context.getCacheDir();
        File cacheFile = new File(cacheDir, System.currentTimeMillis() + (Bitmap.CompressFormat.PNG == format ? ".png" : Bitmap.CompressFormat.WEBP == format ? ".webp" : ".jpg"));
        Bitmap bitmap = BitmapUtil.decodeSmallerFromUri(context, uri, minEdge, 0);
        int degree = BitmapUtil.readPictureDegree(context, uri);
        bitmap = BitmapUtil.rotateBitmap(bitmap, degree);
        if (!TextUtils.isEmpty(authority) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            BitmapUtil.saveBitmapToFile(context, bitmap, authority, cacheFile, format, quality, true);
        } else {
            BitmapUtil.saveBitmapToFile(bitmap, cacheFile, format, quality, true);
        }
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
        /*if (context == null || uri == null) return null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
            int degree = BitmapUtil.readPictureDegree(context, uri);
            bitmap = BitmapUtil.rotateBitmap(bitmap, degree);
            return bitmap;
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        return null;*/
        return BitmapUtil.bitmapFromUri(context, uri, 0, 0);
    }

    /**
     * 从uri读取图片
     */
    @Nullable
    public static Bitmap bitmapFromUri(Context context, Uri uri, int shortEdge, int longEdge) {
        if (context == null || uri == null) return null;
        try {
            Bitmap bitmap = decodeSmallerFromUri(context, uri, shortEdge, longEdge);
            int degree = BitmapUtil.readPictureDegree(context, uri);
            bitmap = BitmapUtil.rotateBitmap(bitmap, degree);
            return bitmap;
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
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
            float zoom = (float) Math.sqrt(byteForKb * 1.0F / sizeInByte);
            if (zoom >= 0.97) zoom = 0.97F;
            else if (zoom < 0.05) zoom = 0.05F;
            LogUtil.d("------   zoom level is " + zoom);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.JPEG, 85, out);
            Matrix matrix = new Matrix();
            matrix.setScale(zoom, zoom);
            Bitmap result = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

            if (recycle && !src.isRecycled()) src.recycle();//我自己加的，源图片好像已经没用了，可以回收

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
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        saveBitmapToOutputStream(bitmap, fos, format, quality, recycle);
        return file.getPath();
    }

    /**
     * 保存bitmap到指定路径
     */
    @Nullable
    @TargetApi(Build.VERSION_CODES.N)
    public static String saveBitmapToFile(Context context, Bitmap bitmap, String authority, File file, Bitmap.CompressFormat format, int quality, boolean recycle) {
        if (context == null || bitmap == null || bitmap.isRecycled() || file == null) return null;
        Context appContext = context.getApplicationContext();
        ContentResolver contentResolver = appContext.getContentResolver();
        Uri uri = FileProvider.getUriForFile(context, authority, file);
        OutputStream fos = null;
        try {
            fos = contentResolver.openOutputStream(uri);
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        saveBitmapToOutputStream(bitmap, fos, format, quality, recycle);
        return file.getAbsolutePath();
    }

    /**
     * 保存bitmap到指定路径
     */
    @TargetApi(Build.VERSION_CODES.Q)
    public static void saveBitmapApi29(Context context, Bitmap bitmap, String name) {
        if (context == null || bitmap == null || bitmap.isRecycled()) return;
        Context appContext = context.getApplicationContext();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, name);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES /*+ File.separator + jpg.getParent()*/);
        ContentResolver contentResolver = appContext.getContentResolver();
        Uri inserted = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (inserted == null) return;
        OutputStream fos = null;
        try {
            fos = contentResolver.openOutputStream(inserted);
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        saveBitmapToOutputStream(bitmap, fos, Bitmap.CompressFormat.JPEG, 100, false);
    }

    /**
     * 保存bitmap到outputStream
     */
    @Nullable
    public static void saveBitmapToOutputStream(Bitmap bitmap, OutputStream file, Bitmap.CompressFormat format, int quality, boolean recycle) {
        if (file == null || bitmap == null || bitmap.isRecycled()) return;
        try {
            bitmap.compress(format == null ? Bitmap.CompressFormat.JPEG : format, quality, file);
            file.flush();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (recycle && !bitmap.isRecycled())//此处不能回收bitmap，否则后面会不能用
            bitmap.recycle();
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
        FileInputStream is = null;
        try {
            is = FileUtil.readUri(context, file);
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
    public static Bitmap decodeSmallerFromInputStream(FileInputStream is, int shortEdge, int longEdge) {
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
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, newOpts);
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
        //int newWidth = 0, newHeight = 0;
        if (shortEdge > 0) {
            int edge = Math.min(newOpts.outWidth, newOpts.outHeight);
            if (edge > shortEdge) {
                float scale = edge * 1f / shortEdge;
                newOpts.inSampleSize = (int) (scale + 0.5f);
                //newWidth = (int) (newOpts.outWidth * scale);
                //newHeight = (int) (newOpts.outHeight * scale);
            }
        } else if (longEdge > 0) {
            int edge = Math.max(newOpts.outWidth, newOpts.outHeight);
            if (edge > longEdge) {
                float scale = edge * 1f / longEdge;
                newOpts.inSampleSize = (int) (scale + 0.5f);
                //newWidth = (int) (newOpts.outWidth * scale);
                //newHeight = (int) (newOpts.outHeight * scale);
            }
        }
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        try {
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, newOpts);  //bitmap = BitmapFactory.decodeFile(path, newOpts);
            //bitmap = getResizedBitmap(bitmap, newHeight, newWidth, true);
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
     * 缩放图片到指定大小
     */
    @Nullable
    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth, boolean recycle) {
        if (image == null || image.isRecycled()) return image;
        if (newWidth == 0 && newHeight == newWidth) return image;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, newWidth, newHeight, false);
        if (recycle && !image.isRecycled()) image.recycle();
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @return degree旋转的角度
     */
    public static int readPictureDegree(Context context, Uri uri) {
        if (context == null || uri == null) return 0;
        int degree = 0;
        InputStream is = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                is = FileUtil.readUri(context, uri);
                degree = BitmapUtil.readPictureDegree(is);
            } else {
                File file = FileUtil.copyToTemporalFile(context, uri);
                if (file == null) degree = 0;
                else degree = BitmapUtil.readPictureDegree(file.getAbsolutePath());
            }
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
        return degree;
    }

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
        if (bmp != null && !bitmap.isRecycled()) {
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
