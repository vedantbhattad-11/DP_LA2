package payment.proxy;

import payment.bridge.Payment;
import payment.model.Transaction;
import payment.factory.PaymentProcessor;

/**
 * DESIGN PATTERN: Proxy - Real Subject
 *
 * This is the actual payment processing service.
 * It uses the Bridge pattern internally to connect
 * the payment type with the gateway provider.
 *
 * The PaymentProxy controls access to this service.
 */
public class RealPaymentService implements PaymentService {

    private final Payment payment;  // Bridge: payment abstraction connected to a provider
    private final PaymentProcessor processor; // Factory Method product

    public RealPaymentService(Payment payment) { this(payment, null); }

    public RealPaymentService(Payment payment, PaymentProcessor processor) {
        this.payment = payment;
        this.processor = processor;
    }

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("\n[PAYMENT PROCESSING]");
        System.out.println("  Real Payment Service: Processing Rs." +
                String.format("%.2f", transaction.getAmount()) + " via " +
                transaction.getPaymentMethod() + "...");

        transaction.setStatus(Transaction.STATUS_PROCESSING);

        // Factory Method product performs payment-method-specific preparation.
        if (processor != null && !processor.processPayment(transaction)) {
            transaction.setStatus(Transaction.STATUS_FAILED);
            transaction.setFailureReason("PROCESSOR_REJECTED");
            return false;
        }

        // Bridge delegates the actual gateway interaction (Payment -> PaymentProvider).
        boolean success = payment.makePayment(transaction.getAmount());

        if (success) {
            transaction.setStatus(Transaction.STATUS_SUCCESS);
            System.out.println("  Real Payment Service: [OK] Payment processed successfully.");
        } else {
            transaction.setStatus(Transaction.STATUS_FAILED);
            System.out.println("  Real Payment Service: [X] Payment processing failed.");
        }

        return success;
    }
}
