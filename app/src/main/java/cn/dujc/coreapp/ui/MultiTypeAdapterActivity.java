package cn.dujc.coreapp.ui;

import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.multi2.MultiTypeAdapter;
import cn.dujc.core.adapter.multi2.ProviderDelegate;
import cn.dujc.core.adapter.multi2.ViewProvider;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.coreapp.R;

public class MultiTypeAdapterActivity extends BaseListActivity {

    private final List<String> mList = Arrays.asList("0"
            , "1"
            , "2"
            , "3"
            , "4"
            , "5"
            , "..."
            , "..."
            , "..."
            , "..."
            , "..."
            , "..."
            , "..."
            , "..."
    );

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new MyAdapter(mList);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void loadMore() {
        reload();
    }

    @Override
    public void reload() {
        refreshDone();
        loadDone(true, false);
    }

    private static class Provider implements ViewProvider<String> {
        private final int mLayoutId;

        public Provider(int layoutId) {
            mLayoutId = layoutId;
        }

        @Override
        public int layoutId() {
            return mLayoutId;
        }

        @Override
        public void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }
    }

    private static class MyAdapter extends MultiTypeAdapter<String> {

        public MyAdapter(@Nullable List<String> data) {
            super(data);
        }

        @Override
        public ProviderDelegate<String> delegate() {
            return new ProviderDelegate<String>() {
                @Override
                public ViewProvider<String> getProvider(List<String> data, int position) {
                    return new Provider(position % 2 == 0 ? R.layout.item_type_0 : R.layout.item_type_1);
                }
            };
        }
    }
}