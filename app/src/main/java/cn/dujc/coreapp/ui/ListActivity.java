package cn.dujc.coreapp.ui;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.core.util.StringUtil;

public class ListActivity extends BaseListActivity {

    private final List<String> mList = new ArrayList<>();

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new BaseAdapter<String>(android.R.layout.simple_list_item_1, mList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
            }
        };
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void loadMore() {
        loadData(mList.size());
    }

    @Override
    public void reload() {
        loadData(0);
    }

    private void loadData(int start) {
        if (start == 0) mList.clear();
        final int size = mList.size();
        for (int index = 0; index < 20; index++) {
            mList.add(StringUtil.format("index %d", index + size));
        }
        notifyDataSetChanged(mList.size() >= 50);
    }
}
