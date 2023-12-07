package org.example.ZestawA;

class Numbers implements Runnable {

    private long n;

    public Numbers(long n) {
        this.n = n;
    }

    @Override
    public void run() {
        boolean is_prime = (n > 1 && n % 2 == 1);
        for (long d = 3; d * d <= n; d += 2) {
            if (n % d == 0) {
                is_prime = false;
                break;
            }
        }
        if (is_prime) {
            System.out.println(n + " jest liczbą pierwszą.");
        } else {
            System.out.println(n + " nie jest liczbą pierwszą.");
        }
    }
}

public class Exercise4 {

    public static void main(String[] args) {
        long a = 1000000000000000003L;
        long b = 1000000000000000009L;

        Thread threadA = new Thread(new Numbers(a));
        Thread threadB = new Thread(new Numbers(b));

        threadA.start();
        threadB.start();

    }
}
