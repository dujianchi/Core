package cn.dujc.coreapp.ui;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.MultiTypeAdapter;
import cn.dujc.core.adapter.multi2.ProviderDelegate;
import cn.dujc.core.adapter.multi2.ViewProvider;
import cn.dujc.core.ui.impl.BaseListActivity;
import cn.dujc.core.util.StringUtil;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;
import cn.dujc.widget.resizeable.ResizeableTextView;

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
        ToastUtil.showToast(mActivity, "refreshed");
    }

    public static class Adapter extends MultiTypeAdapter<String> {

        private final ViewProvider mRedProvider, mGreenProvider;

        public Adapter(@Nullable List<String> data) {
            super(data);
            mRedProvider = new RedProvider();
            mGreenProvider = new GreenProvider();
        }

        @Override
        public ProviderDelegate delegate() {
            return new ProviderDelegate() {
                @Override
                public ViewProvider getProvider(List<?> data, int position) {
                    return position % 2 == 0 ? mRedProvider : mGreenProvider;
                }
            };
        }
    }

    public static class RedProvider implements ViewProvider {

        @Override
        public int layoutId() {
            return R.layout.item_type_0;
        }

        @Override
        public void convert(Context context, BaseQuickAdapter adapter, BaseViewHolder helper, Object item) {
            ResizeableTextView textView = helper.getView(R.id.text);
            //textView.updateScale(2F);
            textView.setText(StringUtil.concat(item));
        }
    }

    public static class GreenProvider implements ViewProvider {

        @Override
        public int layoutId() {
            return R.layout.item_type_1;
        }

        @Override
        public void convert(Context context, BaseQuickAdapter adapter, BaseViewHolder helper, Object item) {
            ResizeableTextView textView = helper.getView(R.id.text);
            //textView.updateScale(4F);
            textView.setText(StringUtil.concat(item));
        }
    }

}
