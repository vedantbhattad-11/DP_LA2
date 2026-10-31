package payment.model;

import payment.memento.TransactionMemento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction model class.
 * Stores all details about a payment transaction including its current status.
 * Used throughout the system by all 5 design patterns.
 */
public class Transaction {

    // Possible transaction statuses
    public static final String STATUS_CREATED    = "CREATED";
    public static final String STATUS_VALIDATING = "VALIDATING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_SUCCESS    = "SUCCESS";
    public static final String STATUS_FAILED     = "FAILED";
    public static final String STATUS_RETRYING   = "RETRYING";
    public static final String STATUS_ESCALATED  = "ESCALATED";

    private String transactionId;
    private double amount;
    private String paymentMethod;   // UPI, Card, Net Banking, Wallet
    private String provider;        // Gateway A, Gateway B, Gateway C
    private String status;
    private String failureReason;
    private LocalDateTime timestamp;

    public Transaction(String transactionId, double amount, String paymentMethod, String provider) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.provider = provider;
        this.status = STATUS_CREATED;
        this.failureReason = null;
        this.timestamp = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getProvider() {
        return provider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public TransactionMemento saveState() { return new TransactionMemento(status, failureReason); }

    public void restoreState(TransactionMemento memento) {
        if (memento == null) throw new IllegalArgumentException("Memento cannot be null.");
        this.status = memento.getStatus();
        this.failureReason = memento.getFailureReason();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("| %-10s | Rs.%-9.2f | %-12s | %-10s | %-12s | %-25s | %s |",
                transactionId, amount, paymentMethod, provider, status,
                (failureReason != null ? failureReason : "-"),
                timestamp.format(fmt));
    }
}
