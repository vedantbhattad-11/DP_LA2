package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Refined Abstraction
 *
 * Card-specific payment that delegates actual processing
 * to whichever PaymentProvider (gateway) it is connected to.
 */
public class CardPayment extends Payment {

    public CardPayment(PaymentProvider provider) {
        super(provider);
    }

    @Override
    public boolean makePayment(double amount) {
        System.out.println("  Card Payment: Initiating card transaction...");
        return provider.processPayment(amount, "Card");
    }

    @Override
    public String getMethodName() {
        return "Card";
    }
}
