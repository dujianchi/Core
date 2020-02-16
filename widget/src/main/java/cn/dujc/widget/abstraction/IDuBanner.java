package cn.dujc.widget.abstraction;

import android.view.View;
import android.widget.ImageView;

public interface IDuBanner extends IDuBannerCore {

    /**
     * 图片加载工具
     */
    public interface ImageLoader {
        void loadImage(View parentView, ImageView imageView, Object item);
    }

    /**
     * 设置图片加载工具
     */
    public void setImageLoader(ImageLoader imageLoader);

}
