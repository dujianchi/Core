package cn.dujc.core.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import cn.dujc.core.R;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.TitleCompat;

public class ToastXHandlerActivity extends BaseActivity {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void start(Context context, CharSequence text) {
        Intent starter = new Intent(context, ToastXHandlerActivity.class);
        starter.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(starter);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };
    private Dialog mDialog;
    private TextView mTextView;

    @Override
    public int getViewId() {
        return 0;
    }

    @Nullable
    @Override
    public View getViewV() {
        return new FrameLayout(mActivity);
    }

    @Nullable
    @Override
    public View initToolbar(ViewGroup parent) {
        return null;
    }

    @Nullable
    @Override
    public TitleCompat initTransStatusBar() {
        return null;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mDialog = new Dialog(mActivity, R.style.core_toast_x_dialog);
        mDialog.setContentView(createView());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        mTextView.setText(extras().get(Intent.EXTRA_TEXT, (CharSequence) ""));

        HANDLER.removeCallbacks(mRunnable);
        HANDLER.postDelayed(mRunnable, 2000);

        mDialog.show();
    }

    @Override
    protected void initAnimEnter() {
        overridePendingTransition(R.anim.core_layout_fade_in, R.anim.core_layout_fade_out);
    }

    @Override
    protected void initAnimExit() {
        overridePendingTransition(R.anim.core_layout_fade_in, R.anim.core_layout_fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog.isShowing()) mDialog.dismiss();
        mDialog = null;
    }

    private View createView() {
        Context appContext = getApplicationContext();
        FrameLayout layout = new FrameLayout(appContext);
        mTextView = new TextView(appContext);

        mTextView.setBackgroundResource(R.drawable.core_toast_x_background);
        mTextView.setId(R.id.core_toast_x_message);
        mTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.core_xtoast_text_color));

        layout.addView(mTextView);
        return layout;
    }
}
