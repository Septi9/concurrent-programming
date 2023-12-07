package org.example.ZestawA;

public class Exercise5 {

    public static void main(String[] args) {
        Bob bob = new Bob();
        Alice alice = new Alice(bob);

        bob.start();
        alice.start();
    }
}

class Alice extends Thread {
    private final Bob bob;

    public Alice(Bob bob) {
        this.bob = bob;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("[Alice] Sending to Bob: " + i);
            bob.setInput(i);
            bob.interrupt();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bob.setInput(0);
        bob.interrupt();
    }
}

class Bob extends Thread {
    private int input;

    public void setInput(int input) {
        this.input = input;
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                if (input == 0) {
                    System.out.println("[Bob] Finishing work.");
                    break;
                }
                else {
                    System.out.println("[Bob] The result is: " + (2 * input));
                }
            }
        }
    }
}