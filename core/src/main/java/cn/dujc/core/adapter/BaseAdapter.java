package cn.dujc.core.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import cn.dujc.core.ui.IBaseUI;

/**
 * 使用BaseViewHolder的adapter，默认使用都是这个
 * Created by du on 2018/1/26.
 */
public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseAdapter(int layoutResId, @Nullable List<? extends T> data) {
        super(layoutResId, data);
    }

    public BaseAdapter(@Nullable List<? extends T> data) {
        this(0, data);
    }

    public BaseAdapter(int layoutResId) {
        this(layoutResId, null);
    }

    public BaseAdapter() {
        this(0, null);
    }

    @Override
    public BaseViewHolder createBaseViewHolder(View view) {
        return new BaseViewHolder(view);
    }

    @Nullable
    public IBaseUI.IStarter starter() {
        if (mContext instanceof IBaseUI.WithToolbar)
            return ((IBaseUI.WithToolbar) mContext).starter();
        return null;
    }

}
