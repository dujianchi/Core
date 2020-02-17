package cn.dujc.widget.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.dujc.widget.abstraction.IDuBanner;

/**
 * 基于recyclerView的banner
 * Created by jc199 on 2017/6/13.
 */
public class DuBanner extends DuBannerCore implements IDuBanner {

    private BannerAdapter mBannerAdapter;

    public DuBanner(@NonNull Context context) {
        super(context);
        init();
    }

    public DuBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DuBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBannerAdapter(mBannerAdapter = new BannerAdapter(this));
    }

    @Override
    public void setImageLoader(ImageLoader imageLoader) {
        if (mBannerAdapter != null) {
            mBannerAdapter.mImageLoader = imageLoader;
        }
    }

    private static class BannerAdapter extends PagerAdapter {

        private final DuBanner mBanner;
        private ImageLoader mImageLoader = new DuBannerDefaultLoader();

        public BannerAdapter(DuBanner banner) {
            mBanner = banner;
        }

        @Override
        public int getCount() {
            return mBanner.getItemCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int realPosition = mBanner.getRealPosition(position);
            final ImageView imageView = new ImageView(container.getContext());

            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mBanner.mOnBannerClickListener != null) {
                        mBanner.mOnBannerClickListener.onBannerClicked(imageView, realPosition);
                    }
                }
            });

            if (mImageLoader != null) {
                mImageLoader.loadImage(container, imageView, mBanner.mList.get(realPosition));
            }

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof View) {
                container.removeView((View) object);
            }
        }
    }

}
