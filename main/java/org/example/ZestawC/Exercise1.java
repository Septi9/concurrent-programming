package org.example.ZestawC;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Exercise1 {
    public static void main(String [] args) throws InterruptedException {
        Parking parking = new Parking(4);
        int parkAttempts = 5;
        Thread [] cars = new Thread[6];
        for (int i = 0; i < cars.length; ++i) {
            cars[i] = new Car(parking, i+1, parkAttempts);
        }
        System.out.println("Simulation started.");
        for (Thread t : cars) {
            t.start();
        }
        for (Thread t : cars) {
            t.join();
        }
        System.out.println("Simulation finished.");
    }
}

class Parking {
    private Semaphore gate;
    private AtomicInteger waitingCount;
    private int[] carsParked;

    public Parking(int capacity) {
        gate = new Semaphore(capacity, true);
        waitingCount = new AtomicInteger(0);
        carsParked = new int[capacity];
    }
    public void enter(int carId) throws InterruptedException {
        System.out.printf("[Car %-3d] is waiting to enter the parking\n", carId);
        waitingCount.incrementAndGet();
        printInfo();
        gate.acquire();
        addCar(carId);
        waitingCount.decrementAndGet();
        printInfo();
    }
    public void leave(int carId) throws InterruptedException {
        removeCar(carId);
        gate.release();
    }
    private synchronized void addCar(int carId) {
        for (int i = 0; i < carsParked.length; i++) {
            if (carsParked[i] == 0) {
                carsParked[i] = carId;
                break;
            }
        }
    }
    private synchronized void removeCar(int carId) {
        for (int i = 0; i < carsParked.length; i++) {
            if (carsParked[i] == carId) {
                carsParked[i] = 0;
                break;
            }
        }
    }
    public synchronized void printInfo() {
        StringBuffer out = new StringBuffer();
        out.append(String.format("[Parking] # of cars waiting: %d\t Cars parked: ",
                waitingCount.get()));
        for (int i = 0; i < carsParked.length; ++i) {
            if (carsParked[i] != 0) {
                out.append(String.format("%d ", carsParked[i]));
            }
        }
        out.append("\n");
        System.out.print(out.toString());
    }
}

class Car extends Thread {
    private Parking parking;
    private int id;
    private int attempts;
    public Car(Parking parking, int id, int attempts) {
        this.parking = parking;
        this.id = id;
        this.attempts = attempts;
    }
    private void rest() throws InterruptedException {
        Thread.sleep(1000 * (1 + ThreadLocalRandom.current().nextInt(2)));
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < attempts; ++i) {
                rest();
                System.out.printf("[Car %-3d] arrived\n", id);
                parking.enter(id);
                rest();
                parking.leave(id);
                System.out.printf("[Car %-3d] left (parked: %d times)\n", id, i+1);
            }
        } catch (InterruptedException e) {
        }
    }
}
