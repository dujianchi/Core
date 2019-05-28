package cn.dujc.coreapp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.dujc.coreapp.R;

public class RatingBar2 extends LinearLayout {

    private int mMax = 5, mCurrent = 0;
    private int mWidth = 50, mHeight = 50, mMargin = 30;
    private Drawable mSelected, mDefault;

    public RatingBar2(Context context) {
        this(context, null, 0);
    }

    public RatingBar2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        reset();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float index, length;
        if (getOrientation() == HORIZONTAL) {
            index = ev.getX();
            length = getWidth();
        } else {
            index = ev.getY();
            length = getHeight();
        }
        mCurrent = (int) (mMax * index / length + 0.5);
        calcPosition(mCurrent);
        return ev.getAction() != MotionEvent.ACTION_UP || super.dispatchTouchEvent(ev);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar2);
            mMax = array.getInteger(R.styleable.RatingBar2_rating_max, mMax);
            mCurrent = array.getInteger(R.styleable.RatingBar2_rating_current, mCurrent);
            mWidth = array.getDimensionPixelOffset(R.styleable.RatingBar2_star_width, mWidth);
            mHeight = array.getDimensionPixelOffset(R.styleable.RatingBar2_star_height, mHeight);
            mMargin = array.getDimensionPixelOffset(R.styleable.RatingBar2_star_margin, mMargin);
            int selected = array.getResourceId(R.styleable.RatingBar2_star_selected, 0);
            try {
                mSelected = ContextCompat.getDrawable(getContext(), selected);
            } catch (Exception e) {
                mSelected = new ColorDrawable(Color.RED);
            }
            int _default = array.getResourceId(R.styleable.RatingBar2_star_default, 0);
            try {
                mDefault = ContextCompat.getDrawable(getContext(), _default);
            } catch (Exception e) {
                mDefault = new ColorDrawable(Color.GRAY);
            }
            array.recycle();
        }
    }

    private void reset() {
        removeAllViews();
        LayoutParams params = new LayoutParams(mWidth, mHeight);
        for (int index = 0; index < mMax; index++) {
            ImageView child = new ImageView(getContext());
            child.setImageDrawable(mDefault);
            params.leftMargin = index == 0 ? 0 : mMargin;
            addView(child, params);
        }
    }

    public void calcPosition(int current) {
        for (int index = mMax - 1; index >= current; index--) {
            View child = getChildAt(index);
            if (child instanceof ImageView) ((ImageView) child).setImageDrawable(mDefault);
        }
        for (int index = current - 1; index >= 0; index--) {
            View child = getChildAt(index);
            if (child instanceof ImageView) ((ImageView) child).setImageDrawable(mSelected);
        }
    }

}
