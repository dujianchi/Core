package cn.dujc.coreapp.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ScrollingView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.dujc.core.util.LogUtil;

public class LayerLayout extends FrameLayout {

    private View mBelowView, mAboveView;
    private float mDownX;
    private float mDownY;

    public LayerLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public LayerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBelowView = getChildAt(0);
        mAboveView = getChildAt(1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN
                || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            mDownX = ev.getX();
            mDownY = ev.getY();
        }
        LogUtil.d("dispatchTouchEvent");
        int[] xy = new int[]{-1, -1};
        LogUtil.d2("ev.getRawX() = " + mDownX, " ev.getRawY() = " + mDownY);
        findOutFirstTop(mAboveView, xy);
        LogUtil.d2("xy[0] = " + xy[0], " xy[1] = " + xy[1]);
        LogUtil.d2("mAboveView.getScrollY() = " + mAboveView.getScrollY());
        LogUtil.d2("getAboveViewScrollY() = " + getAboveViewScrollY());
        if (mDownY < xy[1] + getTopOnScreen() - getAboveViewScrollY()) {
            return mBelowView.dispatchTouchEvent(ev);
        } else {
            return mAboveView.dispatchTouchEvent(ev);
        }
    }

    public int getTopOnScreen() {
        int[] xy = new int[2];
        this.getLocationOnScreen(xy);
        LogUtil.d("getTopOnScreen = " + xy[1]);
        return xy[1];
    }

    public int getAboveViewScrollY() {
        if (mAboveView == null) return 0;
        if (mAboveView instanceof ScrollingView)
            return ((ScrollingView) mAboveView).computeVerticalScrollOffset();
        return mAboveView.getScrollY();
    }

    private void findOutFirstTop(View child, int[] xy) {
        if (child == null) return;
        if (xy[1] <= 0) {
            if (child instanceof ViewGroup) {
                View grandChild = ((ViewGroup) child).getChildAt(0);
                findOutFirstTop(grandChild, xy);
            } else {
                child.getLocationOnScreen(xy);
            }
        }
    }
}
