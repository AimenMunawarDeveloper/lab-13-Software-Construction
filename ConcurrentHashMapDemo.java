import java.util.concurrent.ConcurrentHashMap;

class WriterThread extends Thread {
    private final ConcurrentHashMap<Integer, String> sharedMap;

    public WriterThread(ConcurrentHashMap<Integer, String> sharedMap) {
        this.sharedMap = sharedMap;
    }

    @Override
    public void run() {
        for (int index = 0; index < 10; index++) { // writing to map
            sharedMap.put(index, "Entry " + index);
            System.out.println(Thread.currentThread().getName() + " wrote: " + index);
            try {
                Thread.sleep(100); // Simulating delay
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

class ReaderThread extends Thread {
    private final ConcurrentHashMap<Integer, String> sharedMap;

    public ReaderThread(ConcurrentHashMap<Integer, String> sharedMap) {
        this.sharedMap = sharedMap;
    }

    @Override
    public void run() {
        for (int index = 0; index < 10; index++) { // Reading from the map
            System.out.println(Thread.currentThread().getName() + " read: " + sharedMap);
            try {
                Thread.sleep(50); // Simulating delay
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, String> sharedMap = new ConcurrentHashMap<>();

        // Creating threads
        Thread writerThread1 = new WriterThread(sharedMap);
        Thread writerThread2 = new WriterThread(sharedMap);
        Thread readerThread1 = new ReaderThread(sharedMap);
        Thread readerThread2 = new ReaderThread(sharedMap);

        // Starting threads
        writerThread1.start();
        writerThread2.start();
        readerThread1.start();
        readerThread2.start();

        try {
            writerThread1.join();
            writerThread2.join();
            readerThread1.join();
            readerThread2.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        // Final map
        System.out.println("Final map: " + sharedMap);
    }
}
