package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;
import cn.dujc.widget.abstraction.IDuBanner;
import cn.dujc.widget.banner.DuBanner;

public class BannerActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_banner;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        DuBanner duBanner = findViewById(R.id.db_banner);
        duBanner.setImageLoader(new IDuBanner.ImageLoader() {

            @Override
            public void loadImage(View parentView, ImageView imageView, String url) {
                imageView.setImageResource(R.mipmap.splash);
            }
        });
        duBanner.setData(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
    }
}
