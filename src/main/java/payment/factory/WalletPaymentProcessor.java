package payment.factory;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Factory Method - Concrete Product
 *
 * Wallet-specific payment processor (e.g., Paytm, PhonePe wallet).
 * Created by PaymentProcessorFactory when the user selects Wallet.
 */
public class WalletPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("  Wallet Processor: Processing wallet payment of Rs." +
                String.format("%.2f", transaction.getAmount()) + "...");
        System.out.println("  Wallet Processor: Deducting from wallet balance...");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "Wallet";
    }
}
