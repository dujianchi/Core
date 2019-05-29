package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;
import cn.dujc.widget.abstraction.IDuBanner;
import cn.dujc.widget.banner.DuBanner;
import cn.dujc.widget.banner.IrregularIndicator;

public class BannerActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_banner;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        DuBanner duBanner = (DuBanner) findViewById(R.id.db_banner);
        IrregularIndicator indicator = (IrregularIndicator) findViewById(R.id.ii_indicator);
        duBanner.setImageLoader(new IDuBanner.ImageLoader() {

            @Override
            public void loadImage(View parentView, ImageView imageView, String url) {
                imageView.setImageResource(R.mipmap.splash);
            }
        });
        duBanner.setData(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        duBanner.replaceIndicatorLayout(indicator);
    }
}
