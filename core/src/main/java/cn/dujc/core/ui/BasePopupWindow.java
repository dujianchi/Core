package cn.dujc.core.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import cn.dujc.core.R;
import cn.dujc.core.initializer.content.IRootViewSetupHandler;
import cn.dujc.core.ui.dialog.IDialog;
import cn.dujc.core.ui.dialog.OnRootViewClick;

public abstract class BasePopupWindow extends PopupWindow implements IBaseUI {

    protected Context mContext;
    protected View mRootView;

    public BasePopupWindow(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getViewV() {
        return null;
    }

    @Override
    public void rootViewSetup(View rootView) {
        IRootViewSetupHandler.setup(mContext, this, rootView);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    public void showAtLocation(View parent) {
        showAtLocation(parent, Gravity.CENTER);
    }

    public void showAtLocation(View parent, int gravity) {
        showAtLocation(parent, gravity, 0, 0);
    }

    @Override
    public boolean isShowing() {
        createView();//每次show的时候，系统都会调用此方法判断popupWindow是否已经在showing中，所以在此处创建view，是最合适的
        return super.isShowing();
    }

    @Override
    public void showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        showAsDropDownFix24(anchor);
        super.showAsDropDown(anchor, xOff, yOff, gravity);
    }

    @Nullable
    public final <T extends View> T findViewById(int resId) {
        return mRootView != null ? (T) mRootView.findViewById(resId) : null;
    }

    public void showAsDropDownFix24(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
    }

    public void _onCreateView() {
        final int vid = getViewId();
        mRootView = getViewV();

        if (vid != 0 || mRootView != null) {
            if (vid != 0) {
                mRootView = new FrameLayout(mContext);
                final View insideView = LayoutInflater.from(mContext).inflate(vid, (ViewGroup) mRootView, false);
                ((ViewGroup) mRootView).addView(insideView);
                mRootView.setOnTouchListener(new OnRootViewClick(new IDialog.PopupWindowImpl(this), mRootView, insideView));
            }
            setContentView(mRootView);
            setWidth(_getWidth());
            setHeight(_getHeight());
            setBackgroundDrawable(_getBackgroundDrawable(mContext));
            setAnimationStyle(_getAnimationStyle());
            setOutsideTouchable(_getOutsideTouchable());
            setFocusable(_getFocusable());
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

    public boolean _getOutsideTouchable() {
        return true;
    }

    public boolean _getFocusable() {
        return true;
    }

    public int _getAnimationStyle() {
        return R.style.core_popup_animation;
    }

    public int _getBackgroundColor(Context context) {
        return Color.argb(128, 0, 0, 0);
    }

    public Drawable _getBackgroundDrawable(Context context) {
        return new ColorDrawable(_getBackgroundColor(context));
    }

    /**
     * 每次判断{@link #isShowing()}的时候，都会调用此方法，然后当mRootView为空时，
     * 走{@link #_onCreateView()}方法，此时会初始化界面元素
     */
    private void createView() {
        if (mRootView == null) {
            synchronized (BasePopupWindow.class) {
                if (mRootView == null) _onCreateView();
            }
        }
    }
}
