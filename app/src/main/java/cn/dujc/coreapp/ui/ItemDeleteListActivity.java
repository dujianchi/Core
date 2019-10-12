package cn.dujc.coreapp.ui;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.ui.impl.BaseListActivity;
import cn.dujc.coreapp.R;

public class ItemDeleteListActivity extends BaseListActivity {

    private final List<String> mList = Arrays.asList("aaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa"
            , "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa"
    );

    @Override
    public int getViewId() {
        return R.layout.activity_item_delete;
    }

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new ItemDeleteAdapter(mList);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void loadMore() {
        notifyDataSetChanged(true);
    }

    @Override
    public void reload() {
        notifyDataSetChanged(true);
    }
}
