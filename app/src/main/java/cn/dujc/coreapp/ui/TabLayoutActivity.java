package cn.dujc.coreapp.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;
import cn.dujc.widget.tablayout.TabFactory;
import cn.dujc.widget.tablayout.TabLayout;

public class TabLayoutActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_tab_layout;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        final List<TabFactory.ViewDataImpl> list = Arrays.asList(
                new TabFactory.ViewDataImpl("123", R.mipmap.ic_launcher)
                , new TabFactory.ViewDataImpl("234", R.mipmap.ic_launcher)
                , new TabFactory.ViewDataImpl("345", R.mipmap.ic_launcher)
                , new TabFactory.ViewDataImpl("456", R.mipmap.ic_launcher)
        );
        ViewPager2 vp2Above = findViewById(R.id.vp2_above);
        vp2Above.setAdapter(new Adapter(list));

        TabLayout<TabFactory.ViewDataImpl> tlBottom = findViewById(R.id.tl_bottom);
        TabFactory<TabFactory.ViewDataImpl> tabFactory = tlBottom.getTabFactory();
        tabFactory.setInstaller(new TabFactory.IndexTabImpl<TabFactory.ViewDataImpl>());

        tlBottom.setDataAndViewPage(list, vp2Above);
    }

    public static class Adapter extends BaseAdapter<TabFactory.ViewDataImpl> {

        public Adapter(@Nullable List<? extends TabFactory.ViewDataImpl> data) {
            super(data);
            mLayoutResId = R.layout.item_viewpage2;
        }

        @Override
        protected void convert(BaseViewHolder helper, TabFactory.ViewDataImpl item) {
            helper.setText(android.R.id.text1, item.text());
        }
    }
}
