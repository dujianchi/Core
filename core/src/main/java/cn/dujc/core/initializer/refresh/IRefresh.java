package cn.dujc.core.initializer.refresh;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface IRefresh {

    /**
     * 构建刷新布局，在此处构建布局，该new的new，该findViewById的就find，然后构造完的刷新控件需要给{@link #getSwipeRefreshLayout()}用
     */
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
