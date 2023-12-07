package org.example.ZestawA;

class Worker extends Thread {

    public Worker(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("Hello from " + this.getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Bye from " + this.getName());
    }
}

public class Exercise1 {

    public static void main(String[] args) {
        Thread t1 = new Worker("Alice");
        Thread t2 = new Worker("Bob");
        Thread t3 = new Worker("Charles");
        t1.start();
        t2.start();
        t3.start();
    }
}
