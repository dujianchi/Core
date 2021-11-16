package cn.dujc.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具
 */
public class ThreadPool {

    private static ExecutorService sThreadPool = Executors.newCachedThreadPool();

    public static void execute(Runnable command) {
        if (sThreadPool == null || sThreadPool.isShutdown() || sThreadPool.isTerminated()) {
            synchronized (ThreadPool.class) {
                if (sThreadPool == null || sThreadPool.isShutdown() || sThreadPool.isTerminated()) {
                    sThreadPool = Executors.newCachedThreadPool();
                }
            }
        }
        synchronized (ThreadPool.class) {
            sThreadPool.execute(command);
        }
    }

    public static void shutdownNow() {
        if (sThreadPool != null) {
            synchronized (ThreadPool.class) {
                if (sThreadPool != null) {
                    try {
                        sThreadPool.shutdownNow();
                    } catch (Exception ignored) {
                    }
                    sThreadPool = null;
                }
            }
        }
    }

    private static Handler getHandler() {
        if (sHandler == null) {
            synchronized (ThreadPool.class) {
                if (sHandler == null) {
                    sHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sHandler;
    }

    /**
     * 回到主线程执行
     */
    public static void runInMainThread(Runnable runnable) {
        if (runnable == null) return;
        getHandler().post(runnable);
    }

    /**
     * 回到主线程执行并按照队列执行，即当有多个的时候，会一一排队执行
     */
    public static void runInMainThreadQueue(Runnable runnable) {
        if (runnable == null) return;
        getHandler().postAtFrontOfQueue(runnable);
    }

}
