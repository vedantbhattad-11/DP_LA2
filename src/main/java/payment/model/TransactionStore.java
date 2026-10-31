package payment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory transaction store.
 * Uses a simple ArrayList to store all transactions.
 * Generates auto-incrementing transaction IDs: TXN1001, TXN1002, etc.
 */
public class TransactionStore {

    private static final TransactionStore INSTANCE = new TransactionStore();

    private final List<Transaction> transactions;
    private int nextId;

    private TransactionStore() {
        this.transactions = new ArrayList<>();
        this.nextId = 1001;
    }

    public static TransactionStore getInstance() {
        return INSTANCE;
    }

    /**
     * Creates a new transaction with an auto-generated ID and stores it.
     */
    public Transaction createTransaction(double amount, String paymentMethod, String provider) {
        String txnId = "TXN" + nextId++;
        Transaction txn = new Transaction(txnId, amount, paymentMethod, provider);
        transactions.add(txn);
        return txn;
    }

    /**
     * Returns all stored transactions.
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Prints all transactions in a formatted table.
     */
    public void printAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("\n  No transactions found.\n");
            return;
        }

        System.out.println();
        System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(13) + "+" + "-".repeat(14)
                + "+" + "-".repeat(12) + "+" + "-".repeat(14) + "+" + "-".repeat(27)
                + "+" + "-".repeat(21) + "+");
        System.out.printf("| %-10s | %-11s | %-12s | %-10s | %-12s | %-25s | %-19s |%n",
                "TXN ID", "AMOUNT", "METHOD", "PROVIDER", "STATUS", "FAILURE REASON", "TIMESTAMP");
        System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(13) + "+" + "-".repeat(14)
                + "+" + "-".repeat(12) + "+" + "-".repeat(14) + "+" + "-".repeat(27)
                + "+" + "-".repeat(21) + "+");

        for (Transaction txn : transactions) {
            System.out.println(txn);
        }

        System.out.println("+" + "-".repeat(12) + "+" + "-".repeat(13) + "+" + "-".repeat(14)
                + "+" + "-".repeat(12) + "+" + "-".repeat(14) + "+" + "-".repeat(27)
                + "+" + "-".repeat(21) + "+");
        System.out.println();
    }
}
