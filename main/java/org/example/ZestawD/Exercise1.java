package org.example.ZestawD;

class FooBar {
    private int n;
    private boolean fooPrints = true;

    public FooBar(int n) {
        this.n = n;
    }

    public synchronized void foo() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (!fooPrints) {
                wait();
            }
            System.out.print("foo");
            fooPrints = false;
            notifyAll();
        }
    }

    public synchronized void bar() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (fooPrints) {
                wait();
            }
            System.out.println("bar");
            fooPrints = true;
            notifyAll();
        }
    }
}

public class Exercise1 {
    public static void main(String[] args) {
        // Drukowanych będzie 40 komunikatów
        final FooBar foobar = new FooBar(20);
        new Thread(new Runnable() { // Pierwszy wątek -- wywołuje foo()
            public void run() {
                try {
                    foobar.foo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() { // Drugi wątek -- wywołuje bar()
            public void run() {
                try {
                    foobar.bar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
