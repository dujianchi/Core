package cn.dujc.zxing.impl;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.dujc.core.ui.BaseFragment;
import cn.dujc.zxing.open.ICaptureHandler;
import cn.dujc.zxing.open.ICaptureResult;
import cn.dujc.zxing.open.ICaptureView;
import cn.dujc.zxing.view.ViewfinderView;

public class ZxingFragment extends BaseFragment implements ICaptureHandler {

    private ICaptureView mCaptureView;
    private ICaptureResult mCaptureResult;

    public ICaptureResult getCaptureResult() {
        return mCaptureResult;
    }

    public void setCaptureResult(ICaptureResult captureResult) {
        mCaptureResult = captureResult;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mCaptureView == null) {
            mCaptureView = new CaptureFragmentImpl(this, this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (mCaptureView != null) mCaptureView._onCreateAfterSetupView();
        return rootView;
    }

    @Override
    public int getViewId() {
        if (mCaptureView != null) return mCaptureView._getViewId();
        return 0;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
    }

    @Override
    public ViewfinderView getViewfinderView() {
        if (mCaptureView != null) return mCaptureView.getViewfinderView();
        return null;
    }

    @Override
    public void drawViewfinder() {
        if (mCaptureView != null) mCaptureView.drawViewfinder();
    }

    @Override
    public Handler getHandler() {
        if (mCaptureView != null) return mCaptureView.getHandler();
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCaptureView != null) mCaptureView._onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCaptureView != null) mCaptureView._onPause();
    }

    @Override
    public void onDestroy() {
        if (mCaptureView != null) mCaptureView._onDestroyBefore();
        super.onDestroy();
    }

    @Override
    public boolean handleDecode(String s) {
        if (mCaptureResult != null) return mCaptureResult.handleDecode(s);
        return false;
    }

    public void resetScanMode() {
        if (mCaptureView != null) {
            mCaptureView._onPause();
            mCaptureView._onResume();
        }
    }
}
