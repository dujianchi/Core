package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.BaseFragment;
import cn.dujc.core.util.LogUtil;
import cn.dujc.coreapp.R;

public class ViewPagerActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_view_pager;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final IFragment[] fragments = {new IFragment(), new IFragment(), new IFragment()};
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
    }

    public static class IFragment extends BaseFragment {

        @Override
        public int getViewId() {
            return 0;
        }

        @Override
        protected boolean lazyLoad() {
            return false;
        }

        @Nullable
        @Override
        public View getViewV() {
            TextView textView = new TextView(mActivity);
            textView.setGravity(Gravity.CENTER);
            textView.setText("aaa");
            return textView;
        }

        @Override
        public void initBasic(Bundle savedInstanceState) {
            LogUtil.d2("initBasic", savedInstanceState, this, mRootView);
        }
    }
}
