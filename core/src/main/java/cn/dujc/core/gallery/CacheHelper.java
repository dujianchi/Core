package cn.dujc.core.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import java.io.File;

import cn.dujc.core.cache.glide.DiskCache;
import cn.dujc.core.cache.glide.DiskLruCacheWrapper;
import cn.dujc.core.cache.glide.ObjectKey;
import cn.dujc.core.util.BitmapUtil;

/**
 * 本地图片缓存工具
 */
public class CacheHelper {

    private LruCache<Uri, Bitmap> mMemoryCache = new LruCache<>(1024 * 1024 * 15);
    private DiskCache mDiskCache;

    private CacheHelper(Context context) {
        mDiskCache = DiskLruCacheWrapper.create(new File(context.getCacheDir(), "my_cache"), 1024 * 1024 * 200);
    }

    /**
     * 获取缓存的bitmap
     */
    public Bitmap getBitmap(Uri uri) {
        Bitmap bitmap = mMemoryCache.get(uri);
        if (bitmap == null) {
            File file = mDiskCache.get(new ObjectKey(uri));
            if (file != null && file.exists())
                bitmap = BitmapUtil.decodeSmallerFromFile(file, 0, 0);
        }
        return bitmap;
    }

    /**
     * 保存图片到缓存文件
     */
    public void setBitmap(Uri uri, Bitmap bitmap) {
        mMemoryCache.put(uri, bitmap);
        mDiskCache.put(new ObjectKey(uri), new BitmapWriter(bitmap));
    }

    private static CacheHelper sHelper;

    public static CacheHelper get(Context context) {
        if (sHelper == null) {
            synchronized (CacheHelper.class) {
                if (sHelper == null) {
                    sHelper = new CacheHelper(context.getApplicationContext());
                }
            }
        }
        return sHelper;
    }

    /**
     * 图片保存
     */
    static class BitmapWriter implements DiskCache.Writer {
        Bitmap mBitmap;

        public BitmapWriter(Bitmap bitmap) {
            mBitmap = bitmap;
        }

        @Override
        public boolean write(@NonNull File file) {
            BitmapUtil.saveBitmapToFile(mBitmap, file, Bitmap.CompressFormat.PNG, 100, false);
            return mBitmap != null && !mBitmap.isRecycled();
        }
    }

}
