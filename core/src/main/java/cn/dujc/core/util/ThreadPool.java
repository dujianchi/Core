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

}
