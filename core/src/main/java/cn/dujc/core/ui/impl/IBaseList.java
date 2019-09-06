package cn.dujc.core.ui.impl;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;

import cn.dujc.core.R;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.initializer.baselist.IBaseListSetup;
import cn.dujc.core.initializer.baselist.IBaseListSetupHandler;

/**
 * @author du
 * date 2018/11/1 4:33 PM
 */
public interface IBaseList {

    public static final boolean DEFAULT_END_GONE = true;

    int getViewId();

    void initBasic(Bundle savedInstanceState);

    void onDestroy_();

    /**
     * 协调刷新和Appbar
     */
    void coordinateRefreshAndAppbar();

    /**
     * 加载第一次数据，默认同reload方法
     */
    void loadAtFirst();

    /**
     * 加载失败（部分刷新）
     */
    void loadFailure();

    /**
     * 刷新结束
     */
    void refreshDone();

    /**
     * 加载结束
     *
     * @param dataDone 数据加载结束
     * @param endGone  adapter的尾部是否隐藏
     */
    void loadDone(boolean dataDone, boolean endGone);

    /**
     * 全部刷新
     */
    void notifyDataSetChanged(boolean done);

    /**
     * 全部刷新
     */
    void notifyDataSetChanged(boolean dataDone, boolean endGone);

    @Nullable
    <T> T getItem(int position);

    /**
     * 关于recyclerView的一些其他设置
     */
    void recyclerViewOtherSetup();

    /**
     * 双击标题回到顶部
     */
    void doubleClickTitleToTop();

    /**
     * 默认的RecyclerView.LayoutManager是LinearLayoutManager
     *
     * @return LinearLayoutManager
     */
    @Nullable
    RecyclerView.LayoutManager initLayoutManager();

    @Nullable
    public BaseQuickAdapter getAdapter();

    @Nullable
    public RecyclerView getRecyclerView();

    @Nullable
    SwipeRefreshLayout getSwipeRefreshLayout();

    void showRefreshing();

    void refreshEnable(boolean enable);

    public static interface UI extends IBaseList {

        @Nullable
        BaseQuickAdapter initAdapter();

        void onItemClick(int position);

        void loadMore();

        void reload();
    }

    public static abstract class AbsImpl implements IBaseList {

        private final UI mUI;
        private SwipeRefreshLayout mRefreshLayout;
        private RecyclerView mListView;
        private BaseQuickAdapter mQuickAdapter;
        private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
        private AppBarLayout mAppbarLayout;
        private long mLastDoubleTap = 0;
        private boolean mEndGone = DEFAULT_END_GONE;

        public AbsImpl(UI UI) {
            mUI = UI;
        }

        public abstract View findViewById(int id);

        public abstract Context context();

        @Override
        public int getViewId() {
            return R.layout.core_base_list_layout;
        }

        @Override
        public void initBasic(Bundle savedInstanceState) {
            mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.core_list_refresh_id);
            mListView = (RecyclerView) findViewById(R.id.core_list_view_id);
            mUI.doubleClickTitleToTop();

            if (mRefreshLayout != null) {
                //mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
                mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (mQuickAdapter != null) {
                            mQuickAdapter.resetLoadMore();
                        }
                        mUI.reload();
                    }
                });
            }

            mQuickAdapter = mUI.initAdapter();
            final RecyclerView.LayoutManager layoutManager = mUI.initLayoutManager();

            mListView.setLayoutManager(layoutManager);
            if (mQuickAdapter != null) {
                mListView.setAdapter(mQuickAdapter);

                mQuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        mUI.loadMore();
                    }
                }, mListView);

                mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mUI.onItemClick(position);
                    }
                });

                //initAdapter.disableLoadMoreIfNotFullPage();
                mQuickAdapter.openLoadAnimation();
            }
            mUI.recyclerViewOtherSetup();

            mUI.loadAtFirst();
            mUI.coordinateRefreshAndAppbar();

            IBaseListSetup listSetup = IBaseListSetupHandler.getSetup(context());
            if (listSetup != null) mEndGone = listSetup.endGone();
        }

        @Override
        public void onDestroy_() {
            if (mQuickAdapter != null) mQuickAdapter.onRecycled();
            if (mAppbarLayout != null && mOnOffsetChangedListener != null) {
                mAppbarLayout.removeOnOffsetChangedListener(mOnOffsetChangedListener);
            }
        }

        @Override
        public void coordinateRefreshAndAppbar() {
            View appbarLayout = findViewById(R.id.core_toolbar_appbar_layout);
            if (mRefreshLayout != null && appbarLayout instanceof AppBarLayout) {
                mAppbarLayout = (AppBarLayout) appbarLayout;
                mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        mRefreshLayout.setEnabled(verticalOffset >= 0);
                    }
                };
                mAppbarLayout.addOnOffsetChangedListener(mOnOffsetChangedListener);
            }
        }

        @Override
        public void loadAtFirst() {
            mUI.reload();
        }

        @Override
        public void loadFailure() {
            mUI.refreshDone();
            if (mQuickAdapter != null) {
                mQuickAdapter.loadMoreFail();
            }
        }

        @Override
        public void refreshDone() {
            if (mRefreshLayout != null) {
                mRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void loadDone(boolean dataDone, boolean endGone) {
            if (mQuickAdapter != null) {
                if (dataDone) {
                    mQuickAdapter.loadMoreEnd(endGone);
                } else {
                    mQuickAdapter.loadMoreComplete();
                }
            }
        }

        @Override
        public void notifyDataSetChanged(boolean done) {
            mUI.notifyDataSetChanged(done, mEndGone);
        }

        @Override
        public void notifyDataSetChanged(boolean dataDone, boolean endGone) {
            mUI.refreshDone();
            if (mQuickAdapter != null) {
                mUI.loadDone(dataDone, endGone);
                mQuickAdapter.notifyDataSetChanged();
            }
        }

        @Nullable
        @Override
        public <T> T getItem(int position) {
            return mQuickAdapter != null ? (T) mQuickAdapter.getItem(position) : null;
        }

        @Override
        public void recyclerViewOtherSetup() {
            IBaseListSetupHandler.setup(context(), mListView, mQuickAdapter);
        }

        @Override
        public void doubleClickTitleToTop() {
            final View view = findViewById(R.id.core_toolbar_title_id);
            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final long current = System.currentTimeMillis();
                        if (mListView != null && current - mLastDoubleTap < 500)
                            mListView.smoothScrollToPosition(0);
                        mLastDoubleTap = current;
                    }
                });
            }
        }

        @Nullable
        @Override
        public RecyclerView.LayoutManager initLayoutManager() {
            return new LinearLayoutManager(context());
        }

        @Nullable
        @Override
        public BaseQuickAdapter getAdapter() {
            return mQuickAdapter;
        }

        @Nullable
        @Override
        public RecyclerView getRecyclerView() {
            return mListView;
        }

        @Nullable
        @Override
        public SwipeRefreshLayout getSwipeRefreshLayout() {
            return mRefreshLayout;
        }

        @Override
        public void showRefreshing() {
            if (mRefreshLayout != null) {
                mRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        public void refreshEnable(boolean enable) {
            if (mRefreshLayout != null) {
                mRefreshLayout.setEnabled(enable);
            }
        }
    }

}
