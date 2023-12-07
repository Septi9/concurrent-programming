package org.example.ZestawA;

class Singer2 extends Thread{
    private int N;
    private Singer2 next;

    public Singer2(int N) {
        this.N = N;
    }

    public void setNext(Singer2 next){
        this.next = next;
    }

    @Override
    public void run() {
        if (next != null) {
            try {
                next.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(this.N + " bottles of beer on the wall, " + this.N + " bottles of beer  "
                + "\nTake one down and pass it around, " + ((this.N) - 1) + " bottles of beer on the wall");
    }
}

public class Exercise3 {

    public static void main(String [] args) {
        Singer2 [] threads = new Singer2[100];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Singer2(i+1);
            if (i >= 1) {
                threads[i-1].setNext(threads[i]);
            }
        }
        for (Thread t : threads) {
            t.start();
        }
    }
}
