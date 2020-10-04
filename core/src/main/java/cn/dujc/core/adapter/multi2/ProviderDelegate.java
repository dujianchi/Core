package cn.dujc.core.adapter.multi2;

import java.util.List;

import cn.dujc.core.adapter.MultiTypeAdapter;
import cn.dujc.core.adapter.util.IMultiTypeDelegate;

public abstract class ProviderDelegate implements IMultiTypeDelegate {

    /**
     * ProviderDelegate需要与{@link MultiTypeAdapter}配合使用，但{@link MultiTypeAdapter}
     *
     * @deprecated 不会调用到这个方法，而是直接调用底下的
     * {@link #getProvider(List, int)}，但如果是一般adapter也不影响
     */
    @Deprecated
    @Override
    public int getDefItemViewType(List<?> data, int position) {
        return getProvider(data, position).layoutId();
    }

    @Override
    public int getLayoutId(int viewType) {
        return viewType;
    }

    public abstract ViewProvider getProvider(List<?> data, int position);

}
