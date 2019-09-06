package cn.dujc.core.initializer.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

public interface IRefresh {

    public void initRefresh(View refresh);

    public <T extends View> T getSwipeRefreshLayout();

    public void refreshDone();

    public void showRefreshing();

    public void refreshEnable(boolean enable);

    public void setOnRefreshListener(IRefreshListener onRefreshListener);

    public static class Impl implements IRefresh {
        private IRefreshListener mRefreshListener;
        private SwipeRefreshLayout mSrlLoader;

        @Override
        public void initRefresh(View refresh) {
            mSrlLoader = (SwipeRefreshLayout) refresh;
        }

        @Override
        public <T extends View> T getSwipeRefreshLayout() {
            return (T) mSrlLoader;
        }

        @Override
        public void refreshDone() {
            if (mSrlLoader != null) {
                mSrlLoader.setRefreshing(false);
            }
        }

        @Override
        public void showRefreshing() {
            if (mSrlLoader != null) {
                mSrlLoader.setRefreshing(true);
            }
        }

        @Override
        public void refreshEnable(boolean enable) {
            if (mSrlLoader != null) {
                mSrlLoader.setEnabled(enable);
            }
        }

        @Override
        public void setOnRefreshListener(IRefreshListener onRefreshListener) {
            mRefreshListener = onRefreshListener;
        }
    }
}
