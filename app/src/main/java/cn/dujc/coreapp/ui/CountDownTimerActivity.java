package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.dujc.core.ui.BaseActivity;

public class CountDownTimerActivity extends BaseActivity {

    private TextView mTextView;

    @Override
    public int getViewId() {
        return 0;
    }

    @Nullable
    @Override
    public View getViewV() {
        if (mTextView == null) {
            mTextView = new TextView(mActivity);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setText("start");
        }
        return mTextView;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownHandler().start();
            }
        });
    }

    public static class CountDownHandler implements Runnable {
        private static final Handler HANDLER = new Handler();
        private long mInternal, mFuture;

        public CountDownHandler() {
            this(1000, 30);
        }

        public CountDownHandler(long internal, long future) {
            mInternal = internal;
            mFuture = future;
        }

        public void setInternal(long internal) {
            mInternal = internal;
        }

        public void setFuture(long future) {
            mFuture = future;
        }

        public long getInternal() {
            return mInternal;
        }

        public long getFuture() {
            return mFuture;
        }

        public void start() {
            HANDLER.removeCallbacks(this);
            HANDLER.post(this);
        }

        @Override
        public final void run() {
            System.out.println(" ------ " + System.currentTimeMillis());
            if ((mFuture -= mInternal) > 0) HANDLER.postDelayed(this, mInternal);
        }
    }
}
