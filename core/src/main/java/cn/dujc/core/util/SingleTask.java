package cn.dujc.core.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 后台任务工具类，类似AsyncTask
 *
 * @param <P> 参数类型
 * @param <R> 返回数据类型
 */
public abstract class SingleTask<P, R> {

    protected static final Handler HANDLER = new Handler(Looper.getMainLooper());
    protected static final Executor EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    public final void execute(final P... params) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final R result = onBackground(params);
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        done(result);
                    }
                });
            }
        });
    }

    public abstract R onBackground(P... params);

    public void done(R result) {
    }
}
