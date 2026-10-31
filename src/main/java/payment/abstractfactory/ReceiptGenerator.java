package payment.abstractfactory;

/**
 * DESIGN PATTERN: Abstract Factory - Abstract Receipt Product
 *
 * Interface for generating payment receipts.
 * Each payment method generates receipts in its own format.
 */
public interface ReceiptGenerator {

    /**
     * Generates a receipt for a completed payment.
     * @param transactionId the unique transaction ID
     * @param amount the payment amount
     */
    void generateReceipt(String transactionId, double amount);
}
