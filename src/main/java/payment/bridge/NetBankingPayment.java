package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Refined Abstraction
 *
 * Net Banking-specific payment that delegates actual processing
 * to whichever PaymentProvider (gateway) it is connected to.
 */
public class NetBankingPayment extends Payment {

    public NetBankingPayment(PaymentProvider provider) {
        super(provider);
    }

    @Override
    public boolean makePayment(double amount) {
        System.out.println("  Net Banking Payment: Initiating net banking transaction...");
        return provider.processPayment(amount, "Net Banking");
    }

    @Override
    public String getMethodName() {
        return "Net Banking";
    }
}
