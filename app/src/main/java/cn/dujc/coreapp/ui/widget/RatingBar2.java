package cn.dujc.coreapp.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RatingBar2 extends LinearLayout {

    private int mMax = 5, mCurrent = 1;
    private int mWidth = 50, mHeight = 50, mMargin = 30;
    private boolean mTouching = false;

    public RatingBar2(Context context) {
        this(context, null, 0);
    }

    public RatingBar2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mTouching = ev.getAction() != MotionEvent.ACTION_UP;
        final float index, length;
        if (getOrientation() == HORIZONTAL) {
            index = ev.getX();
            length = getWidth();
        } else {
            index = ev.getY();
            length = getHeight();
        }
        final int position = (int) (mMax * index / length + 0.5);
        System.out.println(position);
        calcPosition(position);
        return mTouching || super.dispatchTouchEvent(ev);
    }

    private void init() {
        removeAllViews();
        LayoutParams params = new LayoutParams(mWidth, mHeight);
        for (int index = 0; index < mMax; index++) {
            ImageView child = new ImageView(getContext());
            child.setBackgroundColor(Color.GRAY);
            params.leftMargin = index == 0 ? 0 : mMargin;
            addView(child, params);
        }
    }

    public void calcPosition(int position) {
        for (int index = mMax - 1; index >= position; index--) {
            View child = getChildAt(index);
            if (child instanceof ImageView) child.setBackgroundColor(Color.GRAY);
        }
        for (int index = position - 1; index >= 0; index--) {
            View child = getChildAt(index);
            if (child instanceof ImageView) child.setBackgroundColor(Color.RED);
        }
    }

}
