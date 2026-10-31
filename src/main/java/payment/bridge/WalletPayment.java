package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Refined Abstraction
 *
 * Wallet-specific payment that delegates actual processing
 * to whichever PaymentProvider (gateway) it is connected to.
 */
public class WalletPayment extends Payment {

    public WalletPayment(PaymentProvider provider) {
        super(provider);
    }

    @Override
    public boolean makePayment(double amount) {
        System.out.println("  Wallet Payment: Initiating wallet transaction...");
        return provider.processPayment(amount, "Wallet");
    }

    @Override
    public String getMethodName() {
        return "Wallet";
    }
}
