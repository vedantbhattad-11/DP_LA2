package payment.factory;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Factory Method - Concrete Product
 *
 * Card-specific payment processor (Credit/Debit Card).
 * Created by PaymentProcessorFactory when the user selects Card.
 */
public class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("  Card Processor: Processing card payment of Rs." +
                String.format("%.2f", transaction.getAmount()) + "...");
        System.out.println("  Card Processor: Verifying card details with issuing bank...");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "Card";
    }
}
