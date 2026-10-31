package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Implementor Interface
 *
 * This interface represents the IMPLEMENTATION side of the Bridge pattern.
 * It defines what a payment gateway/provider can do.
 *
 * Concrete implementors (GatewayA, GatewayB, GatewayC) provide
 * specific gateway behavior.
 *
 * WHY Bridge?
 * - Without Bridge, we'd need classes like UPIGatewayA, UPIGatewayB,
 *   CardGatewayA, CardGatewayB, etc. - a class explosion!
 * - Bridge lets us combine ANY payment type with ANY gateway
 *   at runtime through composition (has-a), not inheritance (is-a).
 */
public interface PaymentProvider {

    /**
     * Process a payment through this gateway.
     * @param amount the payment amount
     * @param paymentMethod the type of payment (UPI, Card, etc.)
     * @return true if the gateway processes successfully, false on failure
     */
    boolean processPayment(double amount, String paymentMethod);

    /**
     * @return the name of this gateway provider
     */
    String getProviderName();
}
