package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;

import cn.dujc.core.adapter.BaseAdapterL;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.StringUtil;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;
import cn.dujc.widget.fake.DuListView;
import cn.dujc.widget.fake.OnItemClickListener;

public class GridViewTestActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_grid_test;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        DuListView gridView = findViewById(R.id.dgv_grids);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, int index, View child) {
                ToastUtil.showToast(mActivity, StringUtil.concat("onclick at ", index));
            }
        });
        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "onclick at view");
            }
        });
        gridView.setAdapter(new BaseAdapterL<Object>(Arrays.asList("", "", "", "", "", "", "", "", "")) {

            @Override
            public View createView(ViewGroup parent) {
                ImageView imageView = new ImageView(parent.getContext());
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }

            @Override
            public void convert(Object item, int position, ViewHolder holder) {
                ((ImageView) holder.getConvertView()).setImageResource(R.mipmap.test);
            }
        });
    }
}
