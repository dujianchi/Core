package cn.dujc.widget.abstraction;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * 自定义指示器
 */
public interface IDuBannerIndicator {

    @NonNull
    ViewGroup getView();

    void updateIndex(int current, int count);

    IDuBannerIndicator setSelectedDrawable(Drawable selectedDrawable);

    IDuBannerIndicator setDefaultDrawable(Drawable defaultDrawable);

    IDuBannerIndicator setSelectedColor(int selectedColor);

    IDuBannerIndicator setDefaultColor(int defaultColor);
}
