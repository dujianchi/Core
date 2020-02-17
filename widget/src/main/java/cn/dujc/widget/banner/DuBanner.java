package cn.dujc.widget.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

    private static class BannerHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public BannerHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView;
        }
    }

    private static class BannerAdapter extends RecyclerView.Adapter<BannerHolder> {

        private final DuBanner mBanner;
        private ImageLoader mImageLoader = new DuBannerDefaultLoader();

        public BannerAdapter(DuBanner banner) {
            mBanner = banner;
        }

        @Override
        @NonNull
        public BannerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ImageView imageView = new ImageView(parent.getContext());
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return new BannerHolder(imageView);
        }

        @Override
        public void onBindViewHolder(BannerHolder holder, int position) {
            if (mImageLoader != null) {
                mImageLoader.loadImage(holder.itemView, holder.mImageView, mBanner.mList.get(mBanner.getRealPosition(position)));
            }
        }

        @Override
        public int getItemCount() {
            return mBanner.getItemCount();
        }
    }

}
