package payment.proxy;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Proxy - Subject Interface
 *
 * This interface defines the contract for the payment service.
 * Both the RealPaymentService and the PaymentProxy implement this interface,
 * so the client doesn't know whether it's talking to the real service or the proxy.
 */
public interface PaymentService {

    /**
     * Process a payment for the given transaction.
     * @param transaction the transaction to process
     * @return true if the payment was processed successfully
     */
    boolean processPayment(Transaction transaction);
}
