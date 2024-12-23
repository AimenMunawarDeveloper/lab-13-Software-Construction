class NumberPrinter extends Thread {
    private final Object sharedLock; // Shared lock for synchronization

    public NumberPrinter(Object sharedLock) {
        this.sharedLock = sharedLock;
    }

    @Override
    public void run() {
        for (int number = 1; number <= 10; number++) {
            synchronized (sharedLock) {
                // Print the number
                System.out.println("Number: " + number);
                sharedLock.notify(); // Notify the other thread to proceed

                try {
                    if (number < 10) {
                        // Wait for the other thread to print the square
                        sharedLock.wait();
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}

class SquarePrinter implements Runnable {
    private final Object sharedLock; // Shared lock for synchronization

    public SquarePrinter(Object sharedLock) {
        this.sharedLock = sharedLock;
    }

    @Override
    public void run() {
        for (int number = 1; number <= 10; number++) {
            synchronized (sharedLock) {
                // Print the square of the number
                System.out.println("Square of " + number + ": " + (number * number));
                sharedLock.notify(); // Notify the other thread to proceed

                try {
                    if (number < 10) {
                        // Wait for the other thread to print the next number
                        sharedLock.wait();
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}

public class MultithreadingExample {
    public static void main(String[] args) {
        Object sharedLock = new Object(); // Shared lock for both threads

        // Create and start the NumberPrinter thread
        NumberPrinter numberPrinterThread = new NumberPrinter(sharedLock);
        numberPrinterThread.start();

        // Create and start the SquarePrinter thread
        SquarePrinter squarePrinterTask = new SquarePrinter(sharedLock);
        Thread squarePrinterThread = new Thread(squarePrinterTask);
        squarePrinterThread.start();

        // Optional: Ensure the main thread waits for both threads to finish
        try {
            numberPrinterThread.join();
            squarePrinterThread.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("Threads have finished.");
    }
}
