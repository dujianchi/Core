package cn.dujc.widget.banner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.dujc.widget.R;
import cn.dujc.widget.abstraction.IDuBannerIndicator;

public class DefaultIndicator extends LinearLayout implements IDuBannerIndicator {

    private final SparseArray<CircleView> mIndicators = new SparseArray<>();
    private Drawable mDefaultDrawable, mSelectedDrawable;
    private int mDefaultColor, mSelectedColor;
    private int mIndicatorMarginBetween = 0,/* mIndicatorMarginLayout = 10, */
            mIndicatorEdge = 10;

    public DefaultIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        final Resources resources = context.getResources();
        mIndicatorEdge = resources.getDimensionPixelOffset(R.dimen.widget_banner_irregular_indicator_long);
        mIndicatorMarginBetween = resources.getDimensionPixelOffset(R.dimen.widget_banner_irregular_indicator_short);
        mSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.widget_banner_irregular_selected);
        mDefaultDrawable = ContextCompat.getDrawable(context, R.drawable.widget_banner_irregular_default);
    }

    public DefaultIndicator(Context context
            , Drawable defaultDrawable
            , Drawable selectedDrawable
            , int defaultColor
            , int selectedColor
            , int indicatorMarginBetween
                            //, int indicatorMarginLayout
            , int indicatorEdge) {
        super(context);
        mDefaultDrawable = defaultDrawable;
        mSelectedDrawable = selectedDrawable;
        mDefaultColor = defaultColor;
        mSelectedColor = selectedColor;
        mIndicatorMarginBetween = indicatorMarginBetween;
        //mIndicatorMarginLayout = indicatorMarginLayout;
        mIndicatorEdge = indicatorEdge;
    }

    @Override
    protected void onDetachedFromWindow() {
        mIndicators.clear();
        super.onDetachedFromWindow();
    }

    @NonNull
    @Override
    public ViewGroup getView() {
        return this;
    }

    @Override
    public DefaultIndicator setSelectedDrawable(Drawable selectedDrawable) {
        mSelectedDrawable = selectedDrawable;
        return this;
    }

    @Override
    public DefaultIndicator setDefaultDrawable(Drawable defaultDrawable) {
        mDefaultDrawable = defaultDrawable;
        return this;
    }

    @Override
    public DefaultIndicator setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
        return this;
    }

    @Override
    public DefaultIndicator setDefaultColor(int defaultColor) {
        mDefaultColor = defaultColor;
        return this;
    }

    @Override
    public void updateIndex(int current, int count) {
        if (count <= 1) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);

            LayoutParams params = new LayoutParams(mIndicatorEdge, mIndicatorEdge);
            params.leftMargin = params.rightMargin = mIndicatorMarginBetween / 2;
            removeAllViews();
            for (int index = 0; index < count; index++) {
                CircleView indicator = mIndicators.get(index);
                if (indicator == null) {
                    indicator = new CircleView(getContext(), mIndicatorEdge);
                    mIndicators.put(index, indicator);
                }
                final boolean isSelected = index == current;
                if (mSelectedDrawable == null && mDefaultDrawable == null) {
                    indicator.setColor(isSelected ? mSelectedColor : mDefaultColor);
                } else {
                    ViewCompat.setBackground(indicator, isSelected ? mSelectedDrawable : mDefaultDrawable);
                }
                addView(indicator, params);
            }
        }
    }

    private static class CircleView extends View {

        final int mEdge;
        final Paint mPaint;
        int mColor = Color.TRANSPARENT;

        public CircleView(Context context, int edge) {
            super(context);
            mEdge = edge;
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
        }

        public void setColor(int color) {
            mColor = color;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int radius = mEdge / 2;
            mPaint.setColor(mColor);
            canvas.drawCircle(radius, radius, radius, mPaint);
            super.onDraw(canvas);
        }
    }
}
