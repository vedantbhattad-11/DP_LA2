package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Concrete Implementor
 *
 * Simulates Gateway A payment processing.
 * This gateway always succeeds (used for happy-path testing).
 */
public class GatewayA implements PaymentProvider {

    @Override
    public boolean processPayment(double amount, String paymentMethod) {
        System.out.println("  Gateway A: Received " + paymentMethod + " payment of Rs." +
                String.format("%.2f", amount));
        System.out.println("  Gateway A: Routing through primary channel...");
        System.out.println("  Gateway A: Payment processed successfully.");
        return true;
    }

    @Override
    public String getProviderName() {
        return "Gateway A";
    }
}
