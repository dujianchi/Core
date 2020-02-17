package cn.dujc.core.adapter.util;

import android.support.annotation.LayoutRes;

import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.adapter.BaseQuickAdapter;

/**
 * 以id作为type的delegate
 */
public abstract class TypeAsIdDelegate implements IMultiTypeDelegate {

    @Override
    public final int getDefItemViewType(List<?> data, int position) {
        Object item = data.get(position);
        return item != null ? getItemLayoutId(item) : R.layout.core_adapter_just_in_case;
    }

    @Override
    public final int getLayoutId(int viewType) {
        return viewType;
    }

    /**
     * 获取当前item对应的layoutId
     */
    @LayoutRes
    protected abstract int getItemLayoutId(Object data);

    public final void setup(BaseQuickAdapter adapter) {
        if (adapter != null) adapter.setMultiTypeDelegate(this);
    }
}
