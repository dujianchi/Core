package cn.dujc.core.adapter.multi2;

import android.support.annotation.LayoutRes;

import cn.dujc.core.adapter.BaseViewHolder;

public interface ViewProvider<T> {
    @LayoutRes
    public int layoutId();

    public void convert(BaseViewHolder helper, T item);
}
