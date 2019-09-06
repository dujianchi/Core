package cn.dujc.core.ui.impl;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import cn.dujc.core.initializer.refresh.IRefresh;
import cn.dujc.core.initializer.refresh.IRefreshListener;
import cn.dujc.core.initializer.refresh.IRefreshSetup;
import cn.dujc.core.initializer.refresh.IRefreshSetupHandler;
import cn.dujc.core.ui.BaseFragment;

/**
 * 可刷新的activity
 * Created by du on 2017/9/27.
 */
public abstract class BaseRefreshableFragment extends BaseFragment implements IRefresh, IRefreshListener {

    private IRefresh mRefresh;

    @Override
    public View createRootView(View contentView) {
        if (mRefresh == null) {
            final IRefreshSetup refreshSetup = IRefreshSetupHandler.getRefresh(mActivity);
            mRefresh = refreshSetup == null ? null : refreshSetup.create();
            if (mRefresh != null) mRefresh.setOnRefreshListener(this);
        }
        return createRefreshRootView(super.createRootView(contentView));
    }

    private View createRefreshRootView(View rootView) {
        if (mRefresh == null) return rootView;
        SwipeRefreshLayout srlLoader = mRefresh.getSwipeRefreshLayout();
        if (srlLoader == null) {
            srlLoader = new SwipeRefreshLayout(mActivity);
            srlLoader.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    BaseRefreshableFragment.this.onRefresh();
                }
            });
            mRefresh.initRefresh(srlLoader);
        }
        final View childAtTwo = srlLoader.getChildAt(1);
        if (childAtTwo != null) srlLoader.removeView(childAtTwo);
        srlLoader.addView(rootView);
        return srlLoader;
    }

    @Override
    public void initRefresh(View refresh) {
        if (mRefresh != null) mRefresh.initRefresh(refresh);
    }

    @Override
    public <T extends View> T getSwipeRefreshLayout() {
        return mRefresh != null ? (T) mRefresh.getSwipeRefreshLayout() : null;
    }

    @Override
    public void refreshDone() {
        if (mRefresh != null) mRefresh.refreshDone();
    }

    @Override
    public void showRefreshing() {
        if (mRefresh != null) mRefresh.showRefreshing();
    }

    @Override
    public void refreshEnable(boolean enable) {
        if (mRefresh != null) mRefresh.refreshEnable(enable);
    }

    @Override
    public void setOnRefreshListener(IRefreshListener onRefreshListener) {
        if (mRefresh != null) mRefresh.setOnRefreshListener(onRefreshListener);
    }

}
