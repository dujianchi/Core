package cn.dujc.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.dujc.widget.R;
import cn.dujc.widget.abstraction.IDuBannerCore;
import cn.dujc.widget.abstraction.IDuBannerIndicator;

/**
 * 基于recyclerView的banner
 * Created by jc199 on 2017/6/13.
 */
public class DuBannerCore extends FrameLayout implements IDuBannerCore {

    private static final int OFFSET_SCALE = 10000;//因为将RecyclerView设置了int.max为数据的长度，所以需要一个默认的偏移量倍数
    private static final int TIME_DEFAULT = 3500;//默认滚动时间
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private final ViewPager.OnPageChangeListener mPageChangeListener;

    protected OnBannerClickListener mOnBannerClickListener;
    protected final List mList = new ArrayList();

    private ViewPager mViewPager;
    private PagerAdapter mBannerAdapter;
    private IDuBannerIndicator mIndicator;
    private int mCurrent, mActual;//当前position和实际position
    private int mTimeInterval = TIME_DEFAULT;
    private int mIndicatorMarginLayout = 10;
    private float mHeightScale = 0f;//9/16
    private boolean mAutoScroll = true;

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager != null) {
                if (isShown()) {
                    mViewPager.setCurrentItem(++mActual);
                }
                onStop();
                onStart();
            }
        }
    };

    public DuBannerCore(@NonNull Context context) {
        this(context, null, 0);
    }

    public DuBannerCore(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DuBannerCore(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    onStart();
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    onStop();
                }
            }

            @Override
            public void onPageSelected(int position) {
                mActual = position;
                if (mActual <= 10 || mActual > Integer.MAX_VALUE - 10) {
                    mActual = calcOffset() + mActual;
                    mViewPager.setCurrentItem(mActual);
                }
                mCurrent = getRealPosition(mActual);

                refreshIndicator();
            }
        };
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightScale != 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            float height = width * mHeightScale;
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        int indicatorMarginBetween = 0, indicatorEdge = 10;
        Drawable drawableDefault = null, drawableSelected = null;
        int colorDefault = 0xff2222, colorSelected = 0xcccccc;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DuBanner);
            mTimeInterval = array.getInt(R.styleable.DuBanner_widget_time_interval, mTimeInterval);
            drawableDefault = array.getDrawable(R.styleable.DuBanner_widget_drawable_default);
            drawableSelected = array.getDrawable(R.styleable.DuBanner_widget_drawable_selected);
            if (drawableDefault == null && drawableSelected == null) {
                colorDefault = array.getColor(R.styleable.DuBanner_widget_color_default, colorDefault);
                colorSelected = array.getColor(R.styleable.DuBanner_widget_color_selected, colorSelected);
            }
            mAutoScroll = array.getBoolean(R.styleable.DuBanner_widget_auto_scroll, mAutoScroll);
            indicatorMarginBetween = array.getDimensionPixelOffset(R.styleable.DuBanner_widget_indicator_margin_between, indicatorMarginBetween);
            mIndicatorMarginLayout = array.getDimensionPixelOffset(R.styleable.DuBanner_widget_indicator_margin_layout, mIndicatorMarginLayout);
            indicatorEdge = array.getDimensionPixelOffset(R.styleable.DuBanner_widget_indicator_edge, indicatorEdge);
            mHeightScale = array.getFloat(R.styleable.DuBanner_widget_height_scale, mHeightScale);
            array.recycle();
        }
        if (indicatorMarginBetween == 0) indicatorMarginBetween = indicatorEdge;

        mViewPager = new ViewPager(context);
        mViewPager.setFocusableInTouchMode(false);
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mIndicator = new DefaultIndicator(context, drawableDefault, drawableSelected, colorDefault, colorSelected, indicatorMarginBetween/*, mIndicatorMarginLayout*/, indicatorEdge);
        LayoutParams indicatorParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        indicatorParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        indicatorParams.bottomMargin = mIndicatorMarginLayout;
        addView(mIndicator.getView(), indicatorParams);

        mViewPager.removeOnPageChangeListener(mPageChangeListener);
        mViewPager.addOnPageChangeListener(mPageChangeListener);

        onStart();
    }

    private LayoutParams getIndicatorParams() {
        LayoutParams indicatorParams = (LayoutParams) mIndicator.getView().getLayoutParams();
        if (indicatorParams == null) {
            indicatorParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            indicatorParams.bottomMargin = mIndicatorMarginLayout;
        }
        return indicatorParams;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        //当指示器不在本控件内时，则设置本控件的显示和隐藏的同时，指示器也同时设置
        if (mIndicator != null && mIndicator.getView().getParent() != this) {
            mIndicator.getView().setVisibility(visibility);
        }
    }

    /**
     * 替换指示器布局；用于显示一个在banner外部的指示器
     *
     * @param indicator IDuBannerIndicator
     */
    @Override
    public void replaceIndicatorLayout(IDuBannerIndicator indicator) {
        if (mIndicator != null && mIndicator.getView().getParent() == this) {
            removeView(mIndicator.getView());
        }
        mIndicator = indicator;
        refreshIndicator();
    }

    /**
     * 设置指示器布局对应的位置
     *
     * @param gravity
     */
    @Override
    public void setIndicatorLayoutGravity(int gravity) {
        if (mIndicator != null) {
            LayoutParams indicatorParams = getIndicatorParams();
            indicatorParams.gravity = gravity;
            mIndicator.getView().setLayoutParams(indicatorParams);
        }
    }

    /**
     * 设置指示器布局对外的间距
     *
     * @param margin
     */
    @Override
    public void setIndicatorLayoutMargin(int margin) {
        setIndicatorLayoutMargin(margin, margin, margin, margin);
    }

    /**
     * 设置指示器布局对外的间距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    public void setIndicatorLayoutMargin(int left, int top, int right, int bottom) {
        if (mIndicator != null) {
            LayoutParams indicatorParams = getIndicatorParams();
            if (left >= 0) indicatorParams.leftMargin = left;
            if (top >= 0) indicatorParams.topMargin = top;
            if (right >= 0) indicatorParams.rightMargin = right;
            if (bottom >= 0) indicatorParams.bottomMargin = bottom;
            mIndicator.getView().setLayoutParams(indicatorParams);
        }
    }

    @Override
    public void setBannerAdapter(PagerAdapter bannerAdapter) {
        mBannerAdapter = bannerAdapter;
        if (mBannerAdapter != null) {
            mViewPager.setAdapter(mBannerAdapter);
        }
    }

    @Override
    public void setData(List list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
            onStart();
        } else {
            onStop();
        }
        mActual = calcOffset();
        mCurrent = getRealPosition(mActual);
        if (mBannerAdapter != null) mBannerAdapter.notifyDataSetChanged();
        refreshIndicator();
        mViewPager.setCurrentItem(mActual, false);
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener bannerClickListener) {
        mOnBannerClickListener = bannerClickListener;
    }

    /**
     * 开始滚动，不考虑其他因素，一旦调用此方法，无论如何都会在handler里面开始计算时间，同时mAutoScroll会设置成true
     */
    @Override
    public void start() {
        mAutoScroll = true;
        sHandler.postDelayed(mRunnable, mTimeInterval);
    }

    /**
     * 停止滚动，不考虑其他因素，一旦调用此方法，将直接移除handler内的callback
     */
    @Override
    public void stop() {
        sHandler.removeCallbacks(mRunnable);
    }

    /**
     * 开始滚动，此方法可以与生命周期配合使用，调用此方法会先判断是否开启了自动滚动，或者是否正在滚动，并且可滚动的数量是否大于1才会执行滚动
     */
    @Override
    public void onStart() {
        if (mAutoScroll && getRealItemCount() > 1) {
            start();
        }
    }

    /**
     * 停止滚动，此方法可以与生命周期配合使用，调用此方法会先判断是否在滚动中（mAutoScroll等同于滚动状态）才会执行
     */
    @Override
    public void onStop() {
        if (mAutoScroll) {
            stop();
        }
    }

    @Override
    public int getRealPosition(int position) {
        int realItemCount = getRealItemCount();
        return realItemCount == 0 ? position : position % realItemCount;
    }

    @Override
    public int getRealItemCount() {
        return mList.size();
    }

    @Override
    public int getItemCount() {
        final int realItemCount = getRealItemCount();
        return realItemCount > 1 ? Integer.MAX_VALUE : realItemCount;
    }

    private int calcOffset() {
        return getRealItemCount() * OFFSET_SCALE;
    }

    private void refreshIndicator() {
        if (mIndicator != null) {
            final int size = getRealItemCount();
            mIndicator.updateIndex(mCurrent, size);
        }
    }

}
