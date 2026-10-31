package payment.factory;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Factory Method - Concrete Product
 *
 * Net Banking-specific payment processor.
 * Created by PaymentProcessorFactory when the user selects Net Banking.
 */
public class NetBankingPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("  Net Banking Processor: Processing net banking payment of Rs." +
                String.format("%.2f", transaction.getAmount()) + "...");
        System.out.println("  Net Banking Processor: Redirecting to bank portal...");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "Net Banking";
    }
}
