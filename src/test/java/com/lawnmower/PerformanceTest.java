package com.lawnmower;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PerformanceTest {
    private LawnMower lawnMower;
    private LawnGrid lawnGrid;
    private MowerMediator mediator;
    private static final int DEFAULT_SIZE = 10;

    @BeforeEach
    void setUp() {
        lawnMower = new LawnMower(DEFAULT_SIZE, DEFAULT_SIZE);
        lawnGrid = new LawnGrid(DEFAULT_SIZE, DEFAULT_SIZE);
        mediator = new MowerMediator(lawnGrid, lawnMower);
    }

    @ParameterizedTest
    @DisplayName("Should handle multiple observers efficiently")
    @ValueSource(ints = {10, 100, 1000})
    void testMultipleObservers(int numObservers) {
        List<TestObserver> observers = new ArrayList<>();
        
        // Add observers
        for (int i = 0; i < numObservers; i++) {
            TestObserver observer = new TestObserver();
            observers.add(observer);
            lawnMower.addObserver(observer);
        }

        // Start mowing
        long startTime = System.nanoTime();
        lawnMower.start();
        try {
            Thread.sleep(1100); // One mowing cycle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();
        long endTime = System.nanoTime();

        // Verify all observers were notified
        for (TestObserver observer : observers) {
            assertTrue(observer.getCallCount() > 0, "Each observer should be notified");
        }

        // Verify performance (should complete within reasonable time)
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        assertTrue(duration < 5000, "Operation should complete within 5 seconds");
    }

    @Test
    @DisplayName("Should handle concurrent grid updates efficiently")
    void testConcurrentGridUpdates() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        // Start multiple threads updating the grid
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        mediator.updateLawn(j % DEFAULT_SIZE, j % DEFAULT_SIZE);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to complete
        boolean completed = latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        assertTrue(completed, "All updates should complete within timeout");
    }

    @Test
    @DisplayName("Should handle rapid state transitions efficiently")
    void testRapidStateTransitions() {
        long startTime = System.nanoTime();
        
        // Perform many rapid state transitions
        for (int i = 0; i < 1000; i++) {
            MowerState idleState = new IdleState(lawnMower);
            idleState.execute();
            
            MowerState mowingState = new MowingState(lawnMower);
            mowingState.execute();
            
            MowerState pausedState = new PausedState(lawnMower);
            pausedState.execute();
            
            MowerState finishedState = new FinishedState(lawnMower);
            finishedState.execute();
        }
        
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        
        assertTrue(duration < 5000, "State transitions should complete within 5 seconds");
    }

    @Test
    @DisplayName("Should handle large grid sizes efficiently")
    void testLargeGridPerformance() {
        int size = 100; // Large grid
        long startTime = System.nanoTime();
        
        LawnMower largeMower = new LawnMower(size, size);
        LawnGrid largeGrid = new LawnGrid(size, size);
        MowerMediator largeMediator = new MowerMediator(largeGrid, largeMower);
        
        // Perform operations on large grid
        largeMower.start();
        try {
            Thread.sleep(1100); // One mowing cycle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        largeMower.stop();
        
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        
        assertTrue(duration < 5000, "Large grid operations should complete within 5 seconds");
    }

    private static class TestObserver implements MowerObserver {
        private int callCount = 0;

        @Override
        public void onCellMowed(int row, int col) {
            callCount++;
        }

        public int getCallCount() {
            return callCount;
        }
    }
} 