package payment.factory;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Factory Method - Concrete Product
 *
 * UPI-specific payment processor. Created by PaymentProcessorFactory
 * when the user selects UPI as the payment method.
 */
public class UPIPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("  UPI Processor: Processing UPI payment of Rs." +
                String.format("%.2f", transaction.getAmount()) + "...");
        System.out.println("  UPI Processor: Sending request to UPI handle...");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "UPI";
    }
}
