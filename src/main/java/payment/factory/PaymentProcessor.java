package payment.factory;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Factory Method - Product Interface
 *
 * This interface defines the contract for all payment processors.
 * Each concrete payment processor (UPI, Card, Net Banking, Wallet)
 * implements this interface.
 *
 * The Factory Method pattern uses PaymentProcessorFactory to decide
 * WHICH concrete implementation to instantiate at runtime.
 */
public interface PaymentProcessor {

    /**
     * Process a payment for the given transaction.
     * @param transaction the transaction to process
     * @return true if processing logic completes (actual success/failure is on the transaction)
     */
    boolean processPayment(Transaction transaction);

    /**
     * @return the name of this payment method (e.g., "UPI", "Card")
     */
    String getPaymentMethodName();
}
