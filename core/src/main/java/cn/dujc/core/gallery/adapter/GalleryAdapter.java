package cn.dujc.core.gallery.adapter;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.gallery.BitmapTask;

public class GalleryAdapter extends BaseAdapter<Uri> {

    private final static int MIN_DP = 200;
    private int mScreenWidth = 0, mMinSize = 0;

    public GalleryAdapter(@Nullable List<? extends Uri> data) {
        super(data);
        mLayoutResId = R.layout.core_layout_gallery;
    }

    @Override
    protected void convert(BaseViewHolder helper, Uri item) {
        if (mScreenWidth < 0) {
            mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            mMinSize = mScreenWidth / 3;
        }
        if (mMinSize <= 0) {
            mMinSize = (int) (mContext.getResources().getDisplayMetrics().density * MIN_DP + 0.5);
        }
        ImageView imageView = helper.getView(R.id.iv_image);
        new BitmapTask(imageView).execute(item);
    }
}
