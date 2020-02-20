package cn.dujc.core.adapter.multi2;

import android.content.Context;

import androidx.annotation.LayoutRes;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;

public interface ViewProvider {
    @LayoutRes
    public int layoutId();

    public void convert(Context context, BaseQuickAdapter adapter, BaseViewHolder helper, Object data);
}
