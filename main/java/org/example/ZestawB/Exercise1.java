package org.example.ZestawB;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Exercise1 {
    public static void main(String [] args) throws InterruptedException {
        Account acc = new Account(0);
        Operator[] operators = new Operator[20];
        for (int i = 0; i < operators.length; ++i) {
            operators[i] = new Operator(acc);
        }
        for (Thread t : operators) { t.start(); }
        int totalWithdrawn = 0;
        int totalDeposited = 0;
        for (Operator o : operators) {
            o.join();
            totalDeposited += o.deposited_;
            totalWithdrawn += o.withdrawn_;
        }
        System.out.println("Całk. kwota wpłacona: " + totalDeposited);
        System.out.println("Stan konta: " + acc.getBalance());
        System.out.println("Całk. kwota wypłacona: " + totalWithdrawn);
        if (acc.getBalance() < 0) {
            System.out.println("Debet na koncie!");
        } else if (acc.getBalance() + totalWithdrawn > totalDeposited) {
            System.out.println("Za dużo pieniędzy w systemie!");
        } else if (acc.getBalance() + totalWithdrawn < totalDeposited) {
            System.out.println("Za mało pieniędzy w systemie!");
        } else {
            System.out.println("OK");
        }
    }
}

class Account {
    AtomicInteger balance_ = new AtomicInteger(0); // saldo

    public Account(int balance) {
        balance_.set(balance);
    }

    void withdraw(int amount) {
        simulateDelay();
        balance_.addAndGet(-amount);
    }

    void deposit(int amount) {
        simulateDelay();
        balance_.addAndGet(amount);
    }

    int getBalance() {
        return balance_.get();
    }

    private void simulateDelay() { // symulacja opóźnienia w dostępie do konta
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 5));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Operator extends Thread {
    public static final int N = 100; // l. transakcji
    private final Account account_;
    public int deposited_ = 0;
    public int withdrawn_ = 0;

    public Operator(Account account) {
        account_ = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < N; ++i) {
// Wybierz losowo typ operacji...
            if (ThreadLocalRandom.current().nextFloat() < 0.9) { // ...pobranie 90% czasu
                if (account_.getBalance() >= 10) {
                    account_.withdraw(10);
                    withdrawn_ += 10;
                }
            } else { // ...czasem wpłata
                account_.deposit(10);
                deposited_ += 10;
            }
        }
    }
}