package cn.dujc.core.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.dujc.core.R;


class ToastX {

    private static final long DURATION_SHORT_TIMEOUT = 2000;
    private static final long DURATION_LONG_TIMEOUT = 4000;

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    private static @interface Duration {
    }

    private final Context mContext;
    private final Handler mHandler;
    private final Runnable mRunnable;
    private final Dialog mDialog;

    private TextView mTextView;

    @Duration
    private int mDuration = Toast.LENGTH_SHORT;

    ToastX(Context context) {
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        mDialog = new Dialog(mContext, R.style.core_toast_x_dialog);
        //mDialog.setCanceledOnTouchOutside(false);

        mDialog.setContentView(createView());

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mDialog.isShowing()) mDialog.dismiss();
            }
        };

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mHandler.removeCallbacks(mRunnable);
            }
        });
    }

    private View createView() {
        Context appContext = mContext.getApplicationContext();
        FrameLayout layout = new FrameLayout(appContext);
        mTextView = new TextView(appContext);

        mTextView.setBackgroundResource(R.drawable.core_toast_x_background);
        mTextView.setId(R.id.core_toast_x_message);
        mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.core_xtoast_text_color));

        layout.addView(mTextView);
        return layout;
    }

    public static ToastX makeText(Context context, CharSequence message, @Duration int duration) {
        ToastX toastX = new ToastX(context);
        toastX.mDuration = duration;
        toastX.mTextView.setText(message);
        return toastX;
    }

    public ToastX setDuration(@Duration int duration) {
        mDuration = duration;
        return this;
    }

    public void show() {
        try {
            if (!mDialog.isShowing()) mDialog.show();
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, mDuration == Toast.LENGTH_LONG ? DURATION_LONG_TIMEOUT : DURATION_SHORT_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
