package cn.dujc.core.adapter.multi2;

import androidx.annotation.Nullable;

import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.util.IMultiTypeDelegate;

public abstract class MultiTypeAdapter<T> extends BaseAdapter<T> {

    //protected final SparseArray<ViewProvider<T>> mProviderArray = new SparseArray<>();

    public MultiTypeAdapter(@Nullable List<T> data) {
        super(data);
        setMultiTypeDelegate(delegate());
    }

    @Override
    protected int getDefItemViewType(int position) {
        final IMultiTypeDelegate<T> delegate = getMultiTypeDelegate();
        if (delegate instanceof ProviderDelegate) {
            ViewProvider<T> provider = ((ProviderDelegate<T>) delegate).getProvider(mData, position);
            //mProviderArray.put(position, provider);
            return provider.layoutId();
        }
        return super.getDefItemViewType(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        delegate().getProvider(mData, helper.getAdapterPosition()).convert(mContext, helper, item);
    }

    public abstract ProviderDelegate<T> delegate();
}
