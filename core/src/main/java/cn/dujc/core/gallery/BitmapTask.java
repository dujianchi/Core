package cn.dujc.core.gallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import cn.dujc.core.util.BitmapUtil;
import cn.dujc.core.util.ContextUtil;

public class BitmapTask extends AsyncTask<Uri, Void, Bitmap> {
    private final WeakReference<ImageView> mImageView;
    private final WeakReference<Activity> mActivity;

    public BitmapTask(ImageView imageView) {
        mImageView = new WeakReference<>(imageView);
        if (imageView != null) {
            mActivity = new WeakReference<>(ContextUtil.getActivity(imageView.getContext()));
        } else {
            mActivity = new WeakReference<>(null);
        }
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        if (uris != null && uris.length > 0) {
            final ImageView imageView = mImageView.get();
            final Activity activity;
            if (imageView != null
                    && (activity = mActivity.get()) != null
                    && !activity.isFinishing()
            ) {
                final Uri uri = uris[0];
                Bitmap bitmap = CacheHelper.get(activity).getBitmap(uri);
                if (bitmap == null) {
                    final int shortEdge = activity.getResources().getDisplayMetrics().widthPixels / 4;
                    bitmap = BitmapUtil.bitmapFromUri(activity, uri, shortEdge, 0);
                    CacheHelper.get(activity).setBitmap(uri, bitmap);
                }
                return bitmap;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        final ImageView imageView;
        if (bitmap != null && (imageView = mImageView.get()) != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
