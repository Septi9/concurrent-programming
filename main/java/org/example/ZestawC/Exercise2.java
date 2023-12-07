package org.example.ZestawC;

import java.util.concurrent.Semaphore;

public class Exercise2 {
    public static void main(String [] args) {
// Drukowanych będzie 40 komunikatów
        final FooBar foobar = new FooBar(40);
// Pierwszy wątek -- wywołuje foo()
        new Thread(new Runnable() {
            public void run() {
                try {
                    foobar.foo();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
// Drugi wątek -- wywołuje bar()
        new Thread(new Runnable() {
            public void run() {
                try {
                    foobar.bar();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}

class FooBar {
    private int n;
    public FooBar(int n) { this.n = n; }

    private final Semaphore dataEmpty = new Semaphore(1);
    private final Semaphore dataReady = new Semaphore(0);

    public void foo() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            dataEmpty.acquire();
            System.out.print("foo");
            dataReady.release();
        }

    }
    public void bar() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            dataReady.acquire();
            System.out.println("bar");
            dataEmpty.release();
        }
    }
}
