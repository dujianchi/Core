package cn.dujc.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.dujc.core.app.Core;

/**
 * Created by Du JC on 2015/12/14.
 */
public class MediaUtil {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * 获取外置内存卡中公有的路径
     *
     * @param type The type of storage directory to return. Should be one of
     *             {@link Environment#DIRECTORY_MUSIC}, {@link Environment#DIRECTORY_PODCASTS},
     *             {@link Environment#DIRECTORY_RINGTONES}, {@link Environment#DIRECTORY_ALARMS},
     *             {@link Environment#DIRECTORY_NOTIFICATIONS}, {@link Environment#DIRECTORY_PICTURES},
     *             {@link Environment#DIRECTORY_MOVIES}, {@link Environment#DIRECTORY_DOWNLOADS},
     *             {@link Environment#DIRECTORY_DCIM}, or {@link Environment#DIRECTORY_DOCUMENTS}. May not be null.
     */
    @Nullable
    public static File getOutputDir(Context context, String type, String subDirName) {
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(type), subDirName);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                LogUtil.e("failed to create directory");
                mediaStorageDir = context.getExternalFilesDir(type);

                if (mediaStorageDir != null && !mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        LogUtil.e("failed to create directory");
                        return null;
                    }
                }
            }
        }

        return mediaStorageDir;
    }

    /**
     * 比较正常的使用方法
     *
     * @param subDirName
     * @return
     */
    @Nullable
    public static File getOutputMediaDir(Context context, String subDirName) {
        return getOutputDir(context, Environment.DIRECTORY_PICTURES, subDirName);
    }

    @Nullable
    public static File getOutputMediaFile(Context context, String subDir, int type) {
        File mediaStorageDir = getOutputMediaDir(context, subDir);
        if (mediaStorageDir == null) {
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Nullable
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            if (Core.DEBUG) e.printStackTrace();
        } catch (RuntimeException e) {
            if (Core.DEBUG) e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                if (Core.DEBUG) e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void refreshGallery(Context context, File file) {
        new SingleMediaScanner(context.getApplicationContext(), file);
    }

    /**
     * 将图片保存到SD卡中，并且更新缩略图
     *
     * @param context
     * @param subDir
     * @param bitmap
     */
    public static void saveOneStep(Context context, String subDir, Bitmap bitmap) {
        final File file = getOutputMediaFile(context, subDir, MEDIA_TYPE_IMAGE);
        if (file != null) {
            String path = saveImgToGallery(context, bitmap, subDir, file.getName());
            ToastUtil.showToast(context, StringUtil.concat("图片已保存到：", path));
        }
    }

    /**
     * 将图片保存到SD卡中，并且更新缩略图
     *
     * @param context
     * @param bitmap
     * @param fileName
     * @return path
     */
    public static String saveImgToGallery(Context context, Bitmap bitmap, String subDirName, String fileName) {
        File outFileDir = getOutputMediaDir(context, subDirName);
        if (outFileDir == null) {
            return null;
        }
        if (bitmap == null) {
            return null;
        }

        File jpg = new File(outFileDir + File.separator + fileName);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            BitmapUtil.saveBitmapToFile(bitmap, jpg);
            refreshGallery(context, jpg);
        } else {
            BitmapUtil.saveBitmapApi29(context, bitmap, jpg.getName());
        }
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
        //mediaScan(context, path);//Android4.4以上无系统权限不能发送Intent.ACTION_MEDIA_MOUNTED广播，所以采用此方法更新图库
        return jpg.getPath();
    }

    private static class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            if (mFile == null || !mFile.exists()) return;
            Context appContext = context.getApplicationContext();
            mMs = new MediaScannerConnection(appContext, this);
            mMs.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mMs.disconnect();
        }

    }
}
