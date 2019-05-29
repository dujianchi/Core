package cn.dujc.widget.abstraction;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * 自定义指示器
 */
public interface IDuBannerIndicator {

    @NonNull
    ViewGroup getView();

    void updateIndex(int current, int count);
}
