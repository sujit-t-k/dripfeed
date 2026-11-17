package org.ajikhoji.dripfeed.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DripFeedScheduler {

    private final ScheduledExecutorService scheduler;
    private final Runnable task;
    private int cycleCount = 0;
    private double totalElapsedTime;

    public DripFeedScheduler(final Runnable work) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        task = work;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(
            () -> {
                ++cycleCount;
                long startTime = System.nanoTime();

                try {
                    System.out.printf("Drip-feed cycle %d started!\n", cycleCount);

                    task.run();

                    long elapsed = System.nanoTime() - startTime;
                    totalElapsedTime += elapsed / 1_000_000.0D;
                    System.out.printf("Cycle %d completed in %.2f ms\nTotal elapsed duration (across all batches): %.2f ms\n\n", cycleCount, elapsed / 1_000_000.0, totalElapsedTime);
                } catch (Exception e) {
                    System.err.printf("Drip-feed cycle %d failed!\n", cycleCount);
                    e.printStackTrace();
                }
            },
            0,        // Initial delay
            15,                 // Execution interval
            TimeUnit.SECONDS
        );
    }

//    public void stop() {
//        scheduler.shutdown();
//
//        try {
//            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
//                scheduler.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            scheduler.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//    }

}