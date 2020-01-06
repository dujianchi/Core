package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;

import cn.dujc.core.adapter.BaseAdapterL;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;
import cn.dujc.widget.fake.DuGridView;

public class GridViewTestActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_grid_test;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        DuGridView gridView = findViewById(R.id.dgv_grids);
        /*gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, int index, View child) {
                ToastUtil.showToast(mActivity, StringUtil.concat("onclick at ", index));
            }
        });*/
        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "onclick at view");
            }
        });
        gridView.setAdapter(new BaseAdapterL<Object>(Arrays.asList("")) {

            @Override
            public View createView(ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
            }

            @Override
            public void convert(Object item, int position, ViewHolder holder) {
                ImageView iv_image = holder.getView(R.id.iv_image);
                iv_image.setImageResource(R.mipmap.test);
            }
        });
    }
}
