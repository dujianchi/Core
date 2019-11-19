package cn.dujc.core.impls;

import android.view.View;

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

    public Object getTag() {
        return mTag;
    }

    public OnTagClickListener setTag(Object tag) {
        mTag = tag;
        return this;
    }
}
