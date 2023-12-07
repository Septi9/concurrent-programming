package org.example.ZestawA;

class Singer extends Thread{
    private int N;

    public Singer(int N) {
        this.N = N;
    }

    @Override
    public void run() {
        System.out.println(this.N + " bottles of beer on the wall, " + this.N + " bottles of beer  "
                + "\nTake one down and pass it around, " + ((this.N) - 1) + " bottles of beer on the wall");
    }
}
public class Exercise2 {

    public static void main(String[] args) {
        Singer [] threads = new Singer[100];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Singer(i+1);
        }
        for (Thread t : threads) {
            t.start();
        }
    }
}
