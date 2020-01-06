package cn.dujc.coreapp.ui;

import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.coreapp.R;

public class ItemDeleteAdapter extends BaseAdapter<String> {

    public ItemDeleteAdapter(List<String> list) {
        super(list);
        mLayoutResId = R.layout.item_item_delete;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
