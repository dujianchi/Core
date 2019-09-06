package cn.dujc.coreapp.impl;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.initializer.baselist.IBaseListSetup;

public class ListSetupHelper implements IBaseListSetup {

    @Override
    public void recyclerViewOtherSetup(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter) { }

    @Override
    public boolean endGone() {
        return false;
    }
}
