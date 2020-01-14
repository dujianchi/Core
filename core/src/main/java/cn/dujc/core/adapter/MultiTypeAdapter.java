package cn.dujc.core.adapter;

import androidx.annotation.Nullable;

import java.util.List;

import cn.dujc.core.adapter.multi2.ProviderDelegate;
import cn.dujc.core.adapter.multi2.ViewProvider;
import cn.dujc.core.adapter.util.IMultiTypeDelegate;

public abstract class MultiTypeAdapter<T> extends BaseAdapter<T> {

    //protected final SparseArray<ViewProvider> mProviderArray = new SparseArray<>();

    public MultiTypeAdapter(@Nullable List<T> data) {
        super(data);
        setMultiTypeDelegate(delegate());
    }

    @Override
    protected int getDefItemViewType(int position) {
        final IMultiTypeDelegate delegate = getMultiTypeDelegate();
        if (delegate instanceof ProviderDelegate) {
            ViewProvider provider = ((ProviderDelegate) delegate).getProvider(mData, position);
            //mProviderArray.put(position, provider);
            return provider.layoutId();
        }
        return super.getDefItemViewType(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        final IMultiTypeDelegate delegate = getMultiTypeDelegate();
        if (delegate instanceof BaseQuickAdapter) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) delegate;
            if (adapter.mContext == null) adapter.mContext = mContext;
        }
        if (delegate instanceof ProviderDelegate) {
            ((ProviderDelegate) delegate).getProvider(mData, helper.getAdapterPosition()).convert(mContext, helper, item);
        }
    }

    public abstract ProviderDelegate delegate();

}
