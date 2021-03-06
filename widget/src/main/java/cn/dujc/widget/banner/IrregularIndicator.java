package cn.dujc.widget.banner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import cn.dujc.widget.R;
import cn.dujc.widget.abstraction.IDuBannerIndicator;

/**
 * 不规则的指示器
 */
public class IrregularIndicator extends LinearLayout implements IDuBannerIndicator {

    private SparseArray<View> mChildren = new SparseArray<>();
    private final int mShortEdge;
    private final int mLongEdge;
    private Drawable mSelectedDrawable, mDefaultDrawable;
    private int mCurrent = -1;

    public IrregularIndicator(Context context) {
        this(context, null);
    }

    public IrregularIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        final Resources resources = context.getResources();
        mLongEdge = resources.getDimensionPixelOffset(R.dimen.widget_banner_irregular_indicator_long);
        mShortEdge = resources.getDimensionPixelOffset(R.dimen.widget_banner_irregular_indicator_short);
        mSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.widget_banner_irregular_selected);
        mDefaultDrawable = ContextCompat.getDrawable(context, R.drawable.widget_banner_irregular_default);
    }

    @Override
    protected void onDetachedFromWindow() {
        mChildren.clear();
        super.onDetachedFromWindow();
    }

    @NonNull
    @Override
    public ViewGroup getView() {
        return this;
    }

    @Override
    public IrregularIndicator setSelectedDrawable(Drawable selectedDrawable) {
        mSelectedDrawable = selectedDrawable;
        return this;
    }

    @Override
    public IrregularIndicator setDefaultDrawable(Drawable defaultDrawable) {
        mDefaultDrawable = defaultDrawable;
        return this;
    }

    @Override
    public IrregularIndicator setSelectedColor(int selectedColor) {
        mSelectedDrawable = new ColorDrawable(selectedColor);
        return this;
    }

    @Override
    public IrregularIndicator setDefaultColor(int defaultColor) {
        mDefaultDrawable = new ColorDrawable(defaultColor);
        return this;
    }

    @Override
    public void updateIndex(int current, int count) {
        if (count <= 1) setVisibility(GONE);
        else {
            setVisibility(VISIBLE);
            if (mCurrent != current || getChildCount() != count) {
                mCurrent = current;
                removeAllViews();
                for (int index = 0; index < count; index++) {
                    View indicator = mChildren.get(index);
                    if (indicator == null) {
                        indicator = new View(getContext());
                        mChildren.put(index, indicator);
                    }
                    final LayoutParams params;
                    if (current == index) {
                        ViewCompat.setBackground(indicator, mSelectedDrawable);
                        params = new LayoutParams(mLongEdge, mShortEdge);
                        params.leftMargin = index == 0 ? 0 : mShortEdge;
                    } else {
                        ViewCompat.setBackground(indicator, mDefaultDrawable);
                        params = new LayoutParams(mShortEdge, mShortEdge);
                        params.leftMargin = index == 0 ? 0 : mShortEdge;
                    }
                    addView(indicator, params);
                }
            }
        }
    }
}
