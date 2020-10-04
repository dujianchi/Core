package cn.dujc.coreapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.BitmapUtil;
import cn.dujc.core.util.FileUtil;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.BuildConfig;
import cn.dujc.coreapp.R;

public class BitmapActivity extends BaseActivity {

    private Button mBtnGet;
    private ImageView mIvReceive;

    public static void getResult(final Context context, Intent data, @NonNull final OnCompressedCallback callback) {
        List<Uri> paths = Matisse.obtainResult(data);
        if (paths != null) {
            Uri[] strings = new Uri[paths.size()];
            new AsyncTask<Uri, Void, List<File>>() {
                @Override
                protected List<File> doInBackground(Uri... lists) {
                    if (lists != null && lists.length > 0) {
                        List<File> result = new ArrayList<>(lists.length);
                        for (Uri uri : lists) {
                            File cacheFile = BitmapUtil.createCompressedCacheFile(context, uri, 960, BuildConfig.APPLICATION_ID + ".file_provider");
                            if (cacheFile != null) result.add(cacheFile);
                        }
                        return result;
                    }
                    return Collections.emptyList();
                }

                @Override
                protected void onPostExecute(List<File> strings) {
                    callback.onCompressed(strings);
                }
            }.execute(paths.toArray(strings));
        }
    }

    public static void pickImage(Activity activity, int count, boolean capture, int requestCode) {
        Matisse.from(activity)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(count)
                .capture(capture)
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .captureStrategy(new CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".file_provider"))
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(requestCode);
    }

    public static void pickImage(Fragment fragment, int count, boolean capture, int requestCode) {
        Matisse.from(fragment)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(count)
                .capture(capture)
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(requestCode);
    }

    @Override
    public int getViewId() {
        return R.layout.activity_bitmap;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mBtnGet = findViewById(R.id.btn_get);
        mIvReceive = findViewById(R.id.iv_receive);

        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionKeeper().requestPermissionsNormal(321
                        , Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                );
            }
        });

        File cacheDir = getExternalCacheDir();
        if (cacheDir == null) cacheDir = getCacheDir();
        if (cacheDir != null) {
            ToastUtil.showToast(mActivity, FileUtil.size(cacheDir) / 1024F / 1024F + " MB");
        }
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
        pickImage(mActivity, 1, true, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            getResult(mActivity, data, new OnCompressedCallback() {
                @Override
                public void onCompressed(List<File> result) {
                    if (result.isEmpty()) {
                        ToastUtil.showToast(mActivity, "empty");
                    } else {
                        mIvReceive.setImageBitmap(BitmapUtil.decodeSmallerFromFile(result.get(0), 100, 100));
                    }
                }
            });
        }
    }

    public static interface OnCompressedCallback {
        void onCompressed(List<File> result);
    }
}
