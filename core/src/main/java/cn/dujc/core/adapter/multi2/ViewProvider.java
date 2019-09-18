package cn.dujc.core.adapter.multi2;

import android.content.Context;

import androidx.annotation.LayoutRes;

import cn.dujc.core.adapter.BaseViewHolder;

public interface ViewProvider<T> {
    @LayoutRes
    public int layoutId();

    public void convert(Context context, BaseViewHolder helper, T item);
}
