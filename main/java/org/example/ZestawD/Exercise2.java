package org.example.ZestawD;

public class Exercise2 {
    public static void main(String[] args) {
        final var foo = new Foo();

        Thread thread1 = new Thread(() -> {
            try {
                foo.third();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                foo.second();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                foo.first();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Foo {
    private int order;

    public Foo() {
        order = 1;
    }

    public synchronized void first() throws InterruptedException {
        while (order != 1) {
            wait();
        }
        System.out.println("first");
        order = 2;
        notifyAll();
    }

    public synchronized void second() throws InterruptedException {
        while (order != 2) {
            wait();
        }
        System.out.println("second");
        order = 3;
        notifyAll();
    }

    public synchronized void third() throws InterruptedException {
        while (order != 3) {
            wait();
        }
        System.out.println("third");
        notifyAll();
    }
}
