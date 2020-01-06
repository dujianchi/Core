package cn.dujc.core.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import cn.dujc.core.ui.BaseFragment;

/**
 * 自带倒计时的Fragment，通常用来做启动页
 */
public abstract class SingleTaskFragment extends BaseFragment {

    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (getUserVisibleHint() || !cancelTaskWhenFinish()) onTaskExecute();
            cancelTask();
        }
    };

    @Override
    public void initBasic(Bundle savedInstanceState) {
        arrangeTask();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cancelTaskWhenPause()) cancelTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTask();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cancelTaskWhenFinish()) cancelTask();
    }

    public final Handler getHandler() {
        return sHandler;
    }

    /**
     * 安排任务，调用次方法，将取消之前的任务，然后重新按照时间安排一个任务
     */
    public final void arrangeTask() {
        cancelTask();
        sHandler.postDelayed(mRunnable, executeTaskTimeMillis());
    }

    /**
     * 取消任务
     */
    public final void cancelTask() {
        sHandler.removeCallbacks(mRunnable);
    }

    /**
     * 执行任务时间，毫秒
     */
    public long executeTaskTimeMillis() {
        return 1500L;
    }

    /**
     * 是否在界面pause的时候取消任务，默认false
     */
    public boolean cancelTaskWhenPause() {
        return false;
    }

    /**
     * 是否在界面关闭的时候取消任务，默认true
     */
    public boolean cancelTaskWhenFinish() {
        return true;
    }

    /**
     * 执行任务
     */
    public abstract void onTaskExecute();
}
