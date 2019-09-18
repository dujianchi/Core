package cn.dujc.coreapp.impl;

import android.view.View;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import cn.dujc.core.initializer.refresh.IRefresh;
import cn.dujc.core.initializer.refresh.IRefreshListener;
import cn.dujc.core.initializer.refresh.IRefreshSetup;

public class RefreshImpl implements IRefreshSetup {

    @Override
    public IRefresh create() {
        return new IRefreshImpl();
    }

    public static class IRefreshImpl implements IRefresh {

        private IRefreshListener mRefreshListener;
        private QMUIPullRefreshLayout mRefreshLayout;

        @Override
        public <T extends View> T initRefresh(View innerView) {
            mRefreshLayout = new QMUIPullRefreshLayout(innerView.getContext());
            mRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
                @Override
                public void onMoveTarget(int offset) { }

                @Override
                public void onMoveRefreshView(int offset) { }

                @Override
                public void onRefresh() {
                    if (mRefreshListener != null) mRefreshListener.onRefresh();
                }
            });
            mRefreshLayout.addView(innerView);
            return (T) mRefreshLayout;
        }

        @Override
        public <T extends View> T getSwipeRefreshLayout() {
            return (T) mRefreshLayout;
        }

        @Override
        public void refreshDone() {
            if (mRefreshLayout != null) mRefreshLayout.finishRefresh();
        }

        @Override
        public void showRefreshing() {
            //if (mRefreshLayout != null) mRefreshLayout.
        }

        @Override
        public void refreshEnable(boolean enable) {
            if (mRefreshLayout != null) mRefreshLayout.setEnabled(enable);
        }

        @Override
        public void setOnRefreshListener(IRefreshListener onRefreshListener) {
            mRefreshListener = onRefreshListener;
        }
    }
}
