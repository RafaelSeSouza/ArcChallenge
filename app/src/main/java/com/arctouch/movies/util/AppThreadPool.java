package com.arctouch.movies.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Very simple thread Pool Singleton to be used when you need something done in the background
 */
public class AppThreadPool {

    // Call this to run whatever you want in the thread pool
    public static synchronized void execute(Runnable runnable) {
        if (mInstance == null) {
            mInstance = new AppThreadPool();
        }
        mInstance.mThreadPoolExecutor.execute(runnable);
    }

    private static AppThreadPool mInstance = null;

    private ThreadPoolExecutor mThreadPoolExecutor;

    private static final long KEEP_ALIVE_TIME = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final int MAXIMUM_POOL_MULTIPLIER = 2;

    private AppThreadPool() {
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
        int corePoolSize = Runtime.getRuntime().availableProcessors();

        mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize * MAXIMUM_POOL_MULTIPLIER, KEEP_ALIVE_TIME, TIME_UNIT, blockingQueue);
    }

}
