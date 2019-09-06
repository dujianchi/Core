package cn.dujc.coreapp.ui;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.multi2.MultiTypeAdapter;
import cn.dujc.core.adapter.multi2.ProviderDelegate;
import cn.dujc.core.adapter.multi2.ViewProvider;
import cn.dujc.core.ui.impl.BaseListActivity;
import cn.dujc.core.util.StringUtil;
import cn.dujc.coreapp.R;

public class ListActivity extends BaseListActivity {

    private final List<String> mList = new ArrayList<>();

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new Adapter(mList);
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
        notifyDataSetChanged(mList.size() >= 150);
    }

    public static class Adapter extends MultiTypeAdapter<String> {

        public Adapter(@Nullable List<String> data) {
            super(data);
        }

        @Override
        public ProviderDelegate<String> delegate() {
            return new ProviderDelegate<String>() {
                @Override
                public ViewProvider<String> getProvider(List<String> data, int position) {
                    ViewProvider<String> provider = mProviderArray.get(position);
                    if (provider == null) {
                        provider = position % 2 == 0 ? new RedProvider() : new GreenProvider();
                    }
                    return provider;
                }
            };
        }
    }

    public static class RedProvider implements ViewProvider<String> {

        @Override
        public int layoutId() {
            return R.layout.item_type_0;
        }

        @Override
        public void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }
    }

    public static class GreenProvider implements ViewProvider<String> {

        @Override
        public int layoutId() {
            return R.layout.item_type_1;
        }

        @Override
        public void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }
    }

}
