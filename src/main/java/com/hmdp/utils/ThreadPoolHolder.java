package com.hmdp.utils;

import java.util.concurrent.*;

/**
 * @author tankaiwen
 */
public class ThreadPoolHolder {

    /** Cache rebuild thread pool */
    public static final ExecutorService CACHE_REFRESH_EXECUTOR = new ThreadPoolExecutor(
            // corePoolSize and maximumPoolSize
            10, 10,
            // keep-alive time for idle threads
            60L, TimeUnit.SECONDS,
            // task queue with capacity 1000
            new LinkedBlockingQueue<>(1000),
            // custom thread factory
            new ThreadFactory() {
                private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
                private int count = 0;

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = defaultFactory.newThread(r);
                    t.setName("cache-refresh-" + count++);
                    t.setDaemon(true);
                    return t;
                }
            },
            // rejection policy: throw exception if queue is full
            new ThreadPoolExecutor.AbortPolicy()
    );

    /** Shutdown all thread pools */
    public static void shutdownAll() {
        shutdownExecutor(CACHE_REFRESH_EXECUTOR, "CACHE_REFRESH_EXECUTOR");
    }

    private static void shutdownExecutor(ExecutorService executor, String name) {
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            System.out.println(name + " has been shut down");
        }
    }

    /** JVM shutdown hook: automatically close thread pools when the program exits */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ThreadPoolHolder::shutdownAll));
    }
}