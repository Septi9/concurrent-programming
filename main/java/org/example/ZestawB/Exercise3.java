package org.example.ZestawB;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Exercise3 {
    public static void main(String[] args) throws InterruptedException {
        int[] threadCounts = { 8, 16, 24, 32 };
        for (int threadCount : threadCounts) {
            System.out.println("Number of threads: " + threadCount);
            Counter counter1 = new Counter(new TestAndSetLock(), threadCount);
            Counter counter2 = new Counter(new SleepyLock(), threadCount);
            Counter counter3 = new Counter(new ReentrantLock(), threadCount);

            long startTime1 = System.nanoTime();
            counter1.performCounting();
            long endTime1 = System.nanoTime();
            System.out.printf("TestAndSetLock execution time: %.2f ms\n", (endTime1 - startTime1) * 1e-6);

            long startTime2 = System.nanoTime();
            counter2.performCounting();
            long endTime2 = System.nanoTime();
            System.out.printf("SleepyLock execution time: %.2f ms\n", (endTime2 - startTime2) * 1e-6);

            long startTime3 = System.nanoTime();
            counter3.performCounting();
            long endTime3 = System.nanoTime();
            System.out.printf("ReentrantLock execution time: %.2f ms\n", (endTime3 - startTime3) * 1e-6);

            System.out.println("-------------------------------------------");
        }
    }
}

class TestAndSetLock {
    private AtomicBoolean occupied = new AtomicBoolean(false);
    public void lock() {
        while (occupied.getAndSet(true)){

        }
    }
    public void unlock() {
        occupied.set(false);
    }
}

class SleepyLock {
    private AtomicBoolean occupied = new AtomicBoolean(false);
    private volatile int maxTime = 2;

    public void lock() throws InterruptedException {
        while (true) {
            if (!occupied.get() && !occupied.getAndSet(true)) {
                return;
            } else {
                Thread.sleep(ThreadLocalRandom.current().nextLong(maxTime));
                maxTime = Math.min(maxTime * 2, 1024);
            }
        }
    }

    public void unlock() {
        occupied.set(false);
    }
}


class Counter {
    static final int Iter = 1_000_000;
    int value = 0;
    final Object lock;
    final int threadCount;

    public Counter(Object lock, int threadCount) {
        this.lock = lock;
        this.threadCount = threadCount;
    }

    public void performCounting() throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(new Worker(this));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    public void increment() {
        if (lock instanceof TestAndSetLock) {
            TestAndSetLock testAndSetLock = (TestAndSetLock) lock;
            testAndSetLock.lock(); // Entry protocol
            try { // Critical section:
                ++value;
            } finally {
                testAndSetLock.unlock(); // Exit protocol
            }
        } else if (lock instanceof SleepyLock) {
            SleepyLock sleepyLock = (SleepyLock) lock;
            try {
                sleepyLock.lock();
                ++value;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                sleepyLock.unlock();
            }
        } else if (lock instanceof ReentrantLock) {
            ReentrantLock reentrantLock = (ReentrantLock) lock;
            reentrantLock.lock();
            try {
                ++value;
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}

class Worker implements Runnable {
    Counter counter = null;
    public Worker(Counter c) {
        counter = c;
    }

    @Override
    public void run() {
        for (int i = 0; i < Counter2.Iter; ++i) {
            counter.increment();
        }
    }
}
