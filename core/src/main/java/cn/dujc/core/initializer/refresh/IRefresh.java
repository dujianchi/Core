package cn.dujc.core.initializer.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import cn.dujc.core.R;

public interface IRefresh {

    /**
     * 构建刷新布局，在此处构建布局，该new的new，该findViewById的就find，然后构造完的刷新控件需要给{@link #getSwipeRefreshLayout()}用
     */
    public <T extends View> T initRefresh(View innerView);

    public <T extends View> T getSwipeRefreshLayout();

    /**
     * 刷新结束
     */
    public void refreshDone();

    public void showRefreshing();

    public void refreshEnable(boolean enable);

    public void setOnRefreshListener(IRefreshListener onRefreshListener);

    public static class Impl implements IRefresh {

        IRefreshListener mRefreshListener;
        SwipeRefreshLayout mSrlLoader;

        @Override
        public <T extends View> T initRefresh(View innerView) {
            mSrlLoader = new SwipeRefreshLayout(innerView.getContext());
            mSrlLoader.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (mRefreshListener != null) mRefreshListener.onRefresh();
                }
            });
            return (T) mSrlLoader;
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

    public static class ListImpl extends Impl {
        @Override
        public <T extends View> T initRefresh(View innerView) {
            mSrlLoader = innerView.findViewById(R.id.core_list_refresh_id);
            mSrlLoader.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (mRefreshListener != null) mRefreshListener.onRefresh();
                }
            });
            return (T) mSrlLoader;
        }
    }
}
