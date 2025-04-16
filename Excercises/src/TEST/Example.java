package TEST;

import java.util.concurrent.Semaphore;

public class Example {

    public static void main(String[] args) throws InterruptedException {
        ThreadClass thread = new ThreadClass();
        thread.start();
        //thread.join();
        System.out.println(thread.getCount());
        thread.wait(100000);
        System.out.println(thread.getCount());
    }
}
class ThreadClass extends Thread{
    Semaphore semaphore = new Semaphore(1);
    static int count =0;
    public void increment() throws InterruptedException {
        semaphore.acquire();
        count++;
        semaphore.release();
    }
    public int getCount(){
        return count;
    }
    @Override
    public void run(){
        for (int i=0;i<50;i++){
            try {
                increment();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

