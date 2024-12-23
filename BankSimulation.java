import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

class Account {
    private AtomicInteger balance;

    public Account(int initialAmount) {
        balance = new AtomicInteger(initialAmount);
    }

    public void addFunds(int amount) { // DEPOST METHOD THAT IS THREAD-SAFE
        balance.addAndGet(amount);
    }

    public boolean removeFunds(int amount) { // WITHDRAWAL METHOD THAT IS THREAD-SAFE
        int currentBalance = balance.get();
        if (currentBalance >= amount) {
            balance.addAndGet(-amount); // DEDEUCTING AMOUNT
            return true;
        }
        return false;// IF THERE ARE INSUFFICIENT FUNDS
    }

    public int checkBalance() {
        return balance.get(); // returning current balance
    }
}

class CustomerThread extends Thread {
    private Account account;
    private Random transactionRandomizer;

    public CustomerThread(Account account) {
        this.account = account;
        this.transactionRandomizer = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            boolean isDeposit = transactionRandomizer.nextBoolean();
            int transactionAmount = transactionRandomizer.nextInt(100) + 1;

            if (isDeposit) {
                account.addFunds(transactionAmount); // Add funds to the account
                System.out.println(Thread.currentThread().getName() + " deposited: " + transactionAmount);
            } else {
                if (account.removeFunds(transactionAmount)) { // Attempt withdrawal
                    System.out.println(Thread.currentThread().getName() + " withdrew: " + transactionAmount);
                } else {
                    System.out.println(Thread.currentThread().getName() + " failed to withdraw: " + transactionAmount
                            + " (Insufficient funds)");
                }
            }

            try {
                Thread.sleep(transactionRandomizer.nextInt(100)); // Simulate some delay
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

public class BankSimulation {
    public static void main(String[] args) {
        Account userAccount = new Account(1000); // Initialize account with an initial balance of 1000

        CustomerThread customer1 = new CustomerThread(userAccount);
        CustomerThread customer2 = new CustomerThread(userAccount);
        CustomerThread customer3 = new CustomerThread(userAccount);
        CustomerThread customer4 = new CustomerThread(userAccount);

        // Start tthreads
        customer1.start();
        customer2.start();
        customer3.start();
        customer4.start();

        // Wait for all threads to finish
        try {
            customer1.join();
            customer2.join();
            customer3.join();
            customer4.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        // Print final account balance after all transactions
        System.out.println("Final Account Balance: " + userAccount.checkBalance());
    }
}
