package cn.dujc.widget.abstraction;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

public interface IDuBannerCore {

    /**
     * 轮播图
     */
    public interface OnBannerClickListener {
        void onBannerClicked(View view, int position);
    }

    /**
     * 替换指示器布局；用于显示一个在banner外部的指示器
     */
    public void replaceIndicatorLayout(IDuBannerIndicator indicator);

    /**
     * 设置指示器布局对应的位置
     */
    public void setIndicatorLayoutGravity(int gravity);

    /**
     * 设置指示器布局对外的间距
     */
    public void setIndicatorLayoutMargin(int margin);

    /**
     * 设置指示器布局对外的间距
     */
    public void setIndicatorLayoutMargin(int left, int top, int right, int bottom);

    /**
     * 设置view的工厂类
     */
    public void setBannerAdapter(PagerAdapter bannerAdapter);

    /**
     * 设置数据
     */
    public void setData(List list);

    /**
     * 设置轮播图点击事件
     */
    public void setOnBannerClickListener(OnBannerClickListener bannerClickListener);

    /**
     * 开始滚动，不考虑其他因素，一旦调用此方法，无论如何都会在handler里面开始计算时间，同时mAutoScroll会设置成true
     */
    public void start();

    /**
     * 停止滚动，不考虑其他因素，一旦调用此方法，将直接移除handler内的callback
     */
    public void stop();

    /**
     * 开始滚动，此方法可以与生命周期配合使用，调用此方法会先判断是否开启了自动滚动，或者是否正在滚动，并且可滚动的数量是否大于1才会执行滚动
     */
    public void onStart();

    /**
     * 停止滚动，此方法可以与生命周期配合使用，调用此方法会先判断是否在滚动中（mAutoScroll等同于滚动状态）才会执行
     */
    public void onStop();

    /**
     * 计算真实的position
     *
     * @param position 扩大的无限的数量下的position
     */
    int getRealPosition(int position);

    /**
     * 获取真实的数据数量
     */
    int getRealItemCount();

    /**
     * 获取数据数量，如果要显示无限循环的数量则处理这个方法
     */
    int getItemCount();
}
