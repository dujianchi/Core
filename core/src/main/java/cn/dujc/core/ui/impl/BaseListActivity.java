package cn.dujc.core.ui.impl;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.initializer.refresh.IRefreshListener;
import cn.dujc.core.ui.BaseActivity;

/**
 * 基本列表activity
 * Created by du on 2017/9/27.
 */
public abstract class BaseListActivity extends BaseActivity implements IBaseList.UI {

    private static class ActivityImpl extends AbsImpl {

        private final BaseListActivity mActivity;
        View mRootView;

        ActivityImpl(BaseListActivity activity) {
            super(activity);
            mActivity = activity;
        }

        @Override
        public View findViewById(int id) {
            return mActivity.findViewById(id);
        }

        @Override
        public Context context() {
            return mActivity;
        }

        @Override
        public View getRootView() {
            return mRootView;
        }
    }

    private IBaseList mBaseList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (mBaseList == null) mBaseList = new ActivityImpl(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getViewId() {
        return mBaseList.getViewId();
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        if (mBaseList instanceof ActivityImpl) ((ActivityImpl) mBaseList).mRootView = mRootView;
        mBaseList.initBasic(savedInstanceState);
    }

    @Override
    public void onDestroy_() {
        mBaseList.onDestroy_();
    }

    @Override
    public void coordinateRefreshAndAppbar() {
        mBaseList.coordinateRefreshAndAppbar();
    }

    @Override
    public void loadAtFirst() {
        mBaseList.loadAtFirst();
    }

    @Override
    public void loadFailure() {
        mBaseList.loadFailure();
    }

    @Override
    public void refreshDone() {
        mBaseList.refreshDone();
    }

    @Override
    public void loadDone(boolean dataDone, boolean endGone) {
        mBaseList.loadDone(dataDone, endGone);
    }

    @Override
    public void notifyDataSetChanged(boolean done) {
        mBaseList.notifyDataSetChanged(done);
    }

    @Override
    public void notifyDataSetChanged(boolean dataDone, boolean endGone) {
        mBaseList.notifyDataSetChanged(dataDone, endGone);
    }

    @Nullable
    @Override
    public <T> T getItem(int position) {
        return mBaseList.getItem(position);
    }

    @Override
    public void recyclerViewOtherSetup() {
        mBaseList.recyclerViewOtherSetup();
    }

    @Override
    public void recyclerViewSetupBeforeAdapter() {
        mBaseList.recyclerViewSetupBeforeAdapter();
    }

    @Override
    public void doubleClickTitleToTop() {
        mBaseList.doubleClickTitleToTop();
    }

    @Nullable
    @Override
    public RecyclerView.LayoutManager initLayoutManager() {
        return mBaseList.initLayoutManager();
    }

    @Nullable
    @Override
    public BaseQuickAdapter getAdapter() {
        return mBaseList.getAdapter();
    }

    @Nullable
    @Override
    public RecyclerView getRecyclerView() {
        return mBaseList.getRecyclerView();
    }

    @Nullable
    @Override
    public <T extends View> T getSwipeRefreshLayout() {
        return mBaseList.getSwipeRefreshLayout();
    }

    @Override
    public void showRefreshing() {
        mBaseList.showRefreshing();
    }

    @Override
    public void refreshEnable(boolean enable) {
        mBaseList.refreshEnable(enable);
    }

    @Override
    public <T extends View> T initRefresh(View innerView) {
        return mBaseList.initRefresh(innerView);
    }

    @Override
    public void setOnRefreshListener(IRefreshListener onRefreshListener) {
        mBaseList.setOnRefreshListener(onRefreshListener);
    }

}
