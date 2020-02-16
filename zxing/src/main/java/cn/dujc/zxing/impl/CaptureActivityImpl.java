package cn.dujc.zxing.impl;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import cn.dujc.zxing.open.CaptureViewImpl;
import cn.dujc.zxing.open.ICaptureResult;
import cn.dujc.zxing.open.IVew;

public class CaptureActivityImpl extends CaptureViewImpl {

    public CaptureActivityImpl(final Activity activity, @NonNull ICaptureResult captureResult) {
        super(new IVew() {
            @NonNull
            public Activity getActivity() {
                return activity;
            }

            public <T extends View> T findViewById(int resId) {
                return activity.findViewById(resId);
            }
        }, captureResult);
    }

    @Override
    public void onCustomResult() {
    }
}
