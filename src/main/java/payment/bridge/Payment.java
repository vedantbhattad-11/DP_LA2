package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Abstraction
 *
 * This abstract class represents the ABSTRACTION side of the Bridge pattern.
 * It holds a reference to a PaymentProvider (the implementor) via composition.
 *
 * Refined abstractions (UPIPayment, CardPayment, etc.) extend this class
 * and add payment-method-specific behavior.
 *
 * KEY BRIDGE CONCEPT:
 *   Payment (abstraction) ---has-a---> PaymentProvider (implementation)
 *
 * This means:
 *   - UPIPayment can work with GatewayA, GatewayB, or GatewayC
 *   - CardPayment can also work with any of those gateways
 *   - The two hierarchies vary independently
 */
public abstract class Payment {

    // Bridge: The implementation is composed, not inherited
    protected PaymentProvider provider;

    public Payment(PaymentProvider provider) {
        this.provider = provider;
    }

    /**
     * Template for making a payment.
     * Subclasses provide method-specific behavior, then delegate to the provider.
     */
    public abstract boolean makePayment(double amount);

    /**
     * @return the payment method name (e.g., "UPI", "Card")
     */
    public abstract String getMethodName();

    public PaymentProvider getProvider() {
        return provider;
    }
}
