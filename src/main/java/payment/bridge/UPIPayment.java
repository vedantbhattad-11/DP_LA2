package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Refined Abstraction
 *
 * UPI-specific payment that delegates actual processing
 * to whichever PaymentProvider (gateway) it is connected to.
 */
public class UPIPayment extends Payment {

    public UPIPayment(PaymentProvider provider) {
        super(provider);
    }

    @Override
    public boolean makePayment(double amount) {
        System.out.println("  UPI Payment: Initiating UPI transaction...");
        // Delegate to the gateway provider (Bridge!)
        return provider.processPayment(amount, "UPI");
    }

    @Override
    public String getMethodName() {
        return "UPI";
    }
}
