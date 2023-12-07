package org.example.ZestawD;

import java.util.concurrent.*;
import java.util.*;

class Worker extends Thread {
    private final BlockingQueue<Task> queue;

    public Worker(BlockingQueue<Task> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task task = queue.take();
                if (task.input < 0) {
                    queue.put(task);
                    break;
                }
                task.output = Fib.calc(task.input);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Task {
    int input;
    int output = 0;
    public Task(int in) { input = in; }
}

class Fib {
    public static int calc(int n) {
        if (n == 1 || n == 0) {
            return n;
        }
        return Fib.calc(n-1) + Fib.calc(n-2);
    }
}

public class Exercise3 {
    public static void main(String [] args) throws InterruptedException {
// Tworzymy wątki robocze
        Worker [] workers = new Worker[8];
        BlockingQueue<Task> workQueue = new LinkedBlockingQueue<>();
        for (int i = 0; i < workers.length; ++i) {
            workers[i] = new Worker(workQueue);
            workers[i].start();
        }
// A teraz lista zadań do obliczenia
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            int n = i % 42;
            Task t = new Task(n); // Znajdź n-ty wyraz ciągu Fibonacciego
            tasks.add(t);
        }
// Dodaj zadania do workQueue aby mogły zostać przetworzone
        workQueue.addAll(tasks);
        workQueue.put(new Task(-1)); // Sygnalizuje koniec pracy
// Czekaj na zakończenie pracy wątków
        for (Thread t : workers) { t.join(); }
// Oblicz sumę kontrolną wyników
        int controlSum = 0;
        for (Task t : tasks) {
            controlSum ^= t.output;
        }
        System.out.println("XOR of the task results: " + controlSum);
    }
}
