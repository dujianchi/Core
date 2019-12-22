package cn.dujc.core.impls;

import android.view.View;

import androidx.annotation.Nullable;

import cn.dujc.core.app.Core;

/**
 * 携带tag的点击事件
 */
public abstract class OnTagClickListener implements View.OnClickListener {

    protected Object mTag;

    public OnTagClickListener() {
    }

    public OnTagClickListener(Object tag) {
        mTag = tag;
    }

    /**
     * 数据本善为null 或者 类型对不上返回null
     */
    @Nullable
    public <T> T getTag() {
        T t = null;
        try {
            t = (T) mTag;
        } catch (ClassCastException e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        return t;
    }

    public OnTagClickListener setTag(Object tag) {
        mTag = tag;
        return this;
    }
}
