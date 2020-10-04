package cn.dujc.core.ui.dialog;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import cn.dujc.core.util.ViewUtils;

public class OnRootViewClick implements View.OnTouchListener {

    private final IDialog mDialog;
    private final View mRootView, mInsideView;
    private final Handler mHandler = new Handler();
    private final OnRootViewClick.VRunnable mRunnable = new OnRootViewClick.VRunnable() {
        @Override
        public void run() {
            if (mView != null && mTouched) {
                mLongClicked = mView.performLongClick();
            }
        }
    };

    public OnRootViewClick(IDialog dialog, View rootView, View insideView) {
        mDialog = dialog;
        mRootView = rootView;
        mInsideView = insideView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mRootView != null && mInsideView != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunnable.mTouched = true;
                mRunnable.mLongClicked = false;
                mHandler.removeCallbacks(mRunnable);
                mRunnable.mView = v;
                mHandler.postDelayed(mRunnable, 3000);
            } else if (mRunnable.mLongClicked) {
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mRunnable.mTouched = false;
                mRunnable.mLongClicked = false;
                mHandler.removeCallbacks(mRunnable);
                mRunnable.mView = null;
                if (mDialog != null) {
                    boolean dismissible = mDialog._dismissible();
                    if (dismissible) {
                        if (ViewUtils.isViewUnderEvent(mInsideView, event)) {
                            mDialog._dismiss();
                            return true;
                        }
                    }
                }
                return v.performClick();
            }
        }
        return mRunnable.mTouched || (event.getAction() == MotionEvent.ACTION_UP && v.performClick());
    }

    private static abstract class VRunnable implements Runnable {
        public View mView;
        public boolean mTouched = false, mLongClicked = false;
    }
}
