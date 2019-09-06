package cn.dujc.widget.abstraction;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * 自定义指示器
 */
public interface IDuBannerIndicator {

    @NonNull
    ViewGroup getView();

    void updateIndex(int current, int count);
}
