package cn.dujc.zxing.impl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import cn.dujc.core.ui.BaseFragment;
import cn.dujc.zxing.open.CaptureViewImpl;
import cn.dujc.zxing.open.ICaptureResult;
import cn.dujc.zxing.open.IVew;

public class CaptureFragmentImpl extends CaptureViewImpl {

    public CaptureFragmentImpl(final BaseFragment baseFragment, @NonNull ICaptureResult captureResult) {
        super(new IVew() {
            @NonNull
            public Activity getActivity() {
                return baseFragment.getActivity();
            }

            public <T extends View> T findViewById(int resId) {
                return baseFragment.findViewById(resId);
            }
        }, captureResult);
    }

    @Override
    public void onCustomResult() { }
}
