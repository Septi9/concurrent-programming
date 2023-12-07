package org.example.ZestawB;

import java.util.concurrent.atomic.AtomicBoolean;

public class Exercise2 {
    public static void main(String [] args) throws InterruptedException {
        Counter2 counter2 = new Counter2(); // Single counter
        Thread [] threads = new Thread[10];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(new Worker2(counter2));
        }
        long startTime = System.nanoTime();
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long endTime = System.nanoTime();
// Now only the main thread prints the final value:
        System.out.println("Counter value is: " + counter2.value);
        System.out.println("The expected value is: " + (threads.length * Counter2.Iter));
        System.out.printf("The execution took: %.2f ms\n", (endTime - startTime) * 1e-6);
    }
}

class TestAndSetLock2 {
    private AtomicBoolean occupied = new AtomicBoolean(false);
    public void lock() {
        while (occupied.getAndSet(true)){

        }
    }
    public void unlock() {
        occupied.set(false);
    }
}

class Counter2 {
    static final int Iter = 1_000_000;
    int value = 0;
    final TestAndSetLock2 lock = new TestAndSetLock2();
    public void increment() {
        lock.lock(); // Entry protocol
        try { // Critical section:
            ++value;
        } finally {
            lock.unlock(); // Exit protocol
        }
    }
}

class Worker2 implements Runnable {
    Counter2 counter2 = null;
    public Worker2(Counter2 c) {
        counter2 = c;
    }

    @Override
    public void run() {
        for (int i = 0; i < Counter2.Iter; ++i) {
            counter2.increment();
        }
    }
}
