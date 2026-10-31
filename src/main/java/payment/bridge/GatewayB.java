package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Concrete Implementor
 *
 * Simulates Gateway B payment processing.
 * This gateway always succeeds (alternative provider).
 */
public class GatewayB implements PaymentProvider {

    @Override
    public boolean processPayment(double amount, String paymentMethod) {
        System.out.println("  Gateway B: Received " + paymentMethod + " payment of Rs." +
                String.format("%.2f", amount));
        System.out.println("  Gateway B: Routing through secondary channel...");
        System.out.println("  Gateway B: Payment processed successfully.");
        return true;
    }

    @Override
    public String getProviderName() {
        return "Gateway B";
    }
}
