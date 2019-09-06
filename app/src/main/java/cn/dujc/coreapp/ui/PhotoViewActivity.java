package cn.dujc.coreapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.Collections;
import java.util.List;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;
import cn.dujc.coreapp.ui.widget.ScalableViewPager;
import cn.dujc.widget.photoview.OnViewTapListener;
import cn.dujc.widget.photoview.PhotoView;

public class PhotoViewActivity extends BaseActivity {

    private ScalableViewPager mViewPager;

    @Override
    public int getViewId() {
        return R.layout.activity_photo_view;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mViewPager = (ScalableViewPager) findViewById(R.id.svp_images);
        mViewPager.setAdapter(new SamplePagerAdapter(mActivity,
                //Arrays.asList(R.mipmap.splash, R.mipmap.splash, R.mipmap.splash)
                Collections.singletonList(R.mipmap.splash)
        ));
    }

    static class SamplePagerAdapter extends PagerAdapter {

        private Activity mActivity;
        private List<Integer> mList;

        public SamplePagerAdapter(Activity activity, List<Integer> list) {
            mActivity = activity;
            mList = list;
        }

        public List<Integer> getList() {
            return mList;
        }

        public void setList(List<Integer> list) {
            mList = list;
        }

        public Integer getItem(int position) {
            if (0 <= position && position < getCount()) return mList.get(position);
            return -1;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setImageResource(getItem(position));
            photoView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (mActivity != null && !mActivity.isFinishing()) mActivity.finish();
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
