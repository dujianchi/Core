package cn.dujc.core.initializer.baselist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.ui.impl.IBaseList;

public interface IBaseListSetup {

    public void recyclerViewOtherSetup(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter);

    public void recyclerViewSetupBeforeAdapter(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter);

    public void recyclerViewSetupBeforeLayoutManager(Context context, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager);

    public boolean endGone();

    public static class DefaultImpl implements IBaseListSetup {

        @Override
        public void recyclerViewOtherSetup(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter) {
        }

        @Override
        public void recyclerViewSetupBeforeAdapter(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter) {
        }

        @Override
        public void recyclerViewSetupBeforeLayoutManager(Context context, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        }

        @Override
        public boolean endGone() {
            return IBaseList.DEFAULT_END_GONE;
        }
    }
}
