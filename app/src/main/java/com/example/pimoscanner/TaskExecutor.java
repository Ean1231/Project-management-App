package com.example.pimoscanner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {
    private static final int POOL_SIZE = 3;

    private static TaskExecutor sInstance;

    private final ExecutorService mPool = Executors.newFixedThreadPool(POOL_SIZE);

    private TaskExecutor() {
    }

    public static TaskExecutor getInstance() {
        if (sInstance == null) {
            sInstance = new TaskExecutor();
        }
        return sInstance;
    }

    public ExecutorService getThreadPool() {
        return mPool;
    }
}
