class Counter {
    private int value = 0;

    public synchronized void incrementValue() {
        value++;
    }

    public synchronized int getValue() {
        return value;
    }
}

class IncrementThread extends Thread {
    private final Counter counter;

    public IncrementThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            counter.incrementValue(); // increment the shared counter
        }
    }
}

public class ThreadSynchronizationExample {
    public static void main(String[] args) {
        Counter counter = new Counter();
        // creating threads
        IncrementThread thread1 = new IncrementThread(counter);
        IncrementThread thread2 = new IncrementThread(counter);
        IncrementThread thread3 = new IncrementThread(counter);

        thread1.start();
        thread2.start();
        thread3.start();

        // Wait for all threads to finish
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e);
        }

        // Print the final value of the counter
        System.out.println("Final value of counter: " + counter.getValue());
    }
}
