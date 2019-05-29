package cn.dujc.widget.banner;

import android.view.View;
import android.widget.ImageView;

import cn.dujc.widget.abstraction.IDuBanner;

/**
 * 默认加载器
 * Created by lucky on 2017/11/30.
 */
public class DuBannerDefaultLoader implements IDuBanner.ImageLoader {

    @Override
    public void loadImage(View parentView, ImageView imageView, String url) {
        //ImageLoadHelper.loadImage(GlideApp.with(view), imageView, url, new RoundedOptions(60));
    }
}
