package cn.dujc.core.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.dujc.core.R;
import cn.dujc.core.initializer.content.IRootViewSetupHandler;
import cn.dujc.core.ui.dialog.IDialog;
import cn.dujc.core.ui.dialog.OnRootViewClick;

public abstract class BaseDialog extends Dialog implements IBaseUI {

    protected Context mContext;
    protected View mRootView;
    private boolean mCancelable_ = true;

    public BaseDialog(Context context) {
        this(context, R.style.core_base_dialog_theme);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _onCreateView();
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable_ = flag;
    }

    @Override
    public View getViewV() {
        return null;
    }

    @Nullable
    public final <T extends View> T findViewById(int resId) {
        return mRootView != null ? (T) mRootView.findViewById(resId) : null;
    }

    @Override
    public void rootViewSetup(View rootView) {
        IRootViewSetupHandler.setup(mContext, this, rootView);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    public boolean isCancelable() {
        return mCancelable_;
    }

    public void _onCreateView() {
        final int vid = getViewId();
        mRootView = getViewV();

        if (vid != 0 || mRootView != null) {
            if (vid != 0) {
                mRootView = new FrameLayout(mContext);
                final View insideView = LayoutInflater.from(mContext).inflate(vid, (ViewGroup) mRootView, false);
                ((ViewGroup) mRootView).addView(insideView);
                mRootView.setOnTouchListener(new OnRootViewClick(new IDialog.BaseDialogImpl(this), mRootView, insideView));
            }
            setContentView(mRootView);
            Window window = getWindow();
            //window.setBackgroundDrawable(_getBackgroundDrawable(mContext));
            window.setLayout(_getWidth(), _getHeight());
            rootViewSetup(mRootView);
            initBasic(null);
        }
    }

    public int _getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public int _getHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    /*public int _getBackgroundColor(Context context) {
        return Color.argb(128, 0, 0, 0);
    }*/

    /*public Drawable _getBackgroundDrawable(Context context) {
        return new ColorDrawable(_getBackgroundColor(context));
    }*/
}
