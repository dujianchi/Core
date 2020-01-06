package cn.dujc.core.ui.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.dujc.core.R;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.initializer.baselist.IBaseListSetup;
import cn.dujc.core.initializer.baselist.IBaseListSetupHandler;
import cn.dujc.core.initializer.refresh.IRefresh;
import cn.dujc.core.initializer.refresh.IRefreshListener;
import cn.dujc.core.initializer.refresh.IRefreshSetup;
import cn.dujc.core.initializer.refresh.IRefreshSetupHandler;
import cn.dujc.core.ui.IBaseUI;

/**
 * @author du
 * date 2018/11/1 4:33 PM
 */
public interface IBaseList extends IBaseUI, IRefresh {

    public static final boolean DEFAULT_END_GONE = true;

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
     * setAdapter之前的方法，介于{@link #recyclerViewOtherSetup()}和{@link #recyclerViewSetupBeforeLayoutManager()}
     */
    void recyclerViewSetupBeforeAdapter();

    /**
     * setLayoutManager之前的方法
     */
    void recyclerViewSetupBeforeLayoutManager();

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

    @Override
    @Deprecated
    View getViewV();

    @Override
    @Deprecated
    void rootViewSetup(View rootView);

    public static interface UI extends IBaseList {

        @Nullable
        BaseQuickAdapter initAdapter();

        void onItemClick(int position);

        void loadMore();

        void reload();
    }

    public static abstract class AbsImpl implements IBaseList {

        private final UI mUI;
        private IRefresh mRefresh;
        private RecyclerView mListView;
        private BaseQuickAdapter mQuickAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
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
        @Deprecated
        public View getViewV() {
            return null;
        }

        @Override
        @Deprecated
        public void rootViewSetup(View rootView) {
        }

        @Override
        public void initBasic(Bundle savedInstanceState) {
            if (mRefresh == null) {
                final IRefreshSetup refreshSetup = IRefreshSetupHandler.getRefresh(context());
                mRefresh = refreshSetup == null ? new ListImpl() : refreshSetup.createList();
                if (mRefresh != null) {
                    mRefresh.initRefresh(getRootView());
                    mRefresh.setOnRefreshListener(new IRefreshListener() {
                        @Override
                        public void onRefresh() {
                            if (mQuickAdapter != null) {
                                mQuickAdapter.resetLoadMore();
                            }
                            mUI.reload();
                        }
                    });
                }
            }
            mListView = (RecyclerView) findViewById(R.id.core_list_view_id);
            mUI.doubleClickTitleToTop();

            mQuickAdapter = mUI.initAdapter();
            mLayoutManager = mUI.initLayoutManager();

            mUI.recyclerViewSetupBeforeLayoutManager();
            mListView.setLayoutManager(mLayoutManager);
            if (mQuickAdapter != null) {
                mUI.recyclerViewSetupBeforeAdapter();
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
            if (mRefresh != null && mRefresh.getSwipeRefreshLayout() != null && appbarLayout instanceof AppBarLayout) {
                mAppbarLayout = (AppBarLayout) appbarLayout;
                mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        mRefresh.refreshEnable(verticalOffset >= 0);
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
        public <T extends View> T initRefresh(View innerView) {
            if (mRefresh != null) return mRefresh.initRefresh(innerView);
            return null;
        }

        @Override
        public void setOnRefreshListener(IRefreshListener onRefreshListener) {
            if (mRefresh != null) mRefresh.setOnRefreshListener(onRefreshListener);
        }

        @Override
        public void refreshDone() {
            if (mRefresh != null) {
                mRefresh.refreshDone();
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
        public void recyclerViewSetupBeforeAdapter() {
            IBaseListSetupHandler.setupBeforeAdapter(context(), mListView, mQuickAdapter);
        }

        @Override
        public void recyclerViewSetupBeforeLayoutManager() {
            IBaseListSetupHandler.setupBeforeLayoutManager(context(), mListView, mLayoutManager);
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
        public <T extends View> T getSwipeRefreshLayout() {
            return mRefresh != null ? (T) mRefresh.getSwipeRefreshLayout() : null;
        }

        @Override
        public void showRefreshing() {
            if (mRefresh != null) {
                mRefresh.showRefreshing();
            }
        }

        @Override
        public void refreshEnable(boolean enable) {
            if (mRefresh != null) {
                mRefresh.refreshEnable(enable);
            }
        }
    }

}
