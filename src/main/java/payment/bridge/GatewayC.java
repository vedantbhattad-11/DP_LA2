package payment.bridge;

/**
 * DESIGN PATTERN: Bridge - Concrete Implementor
 *
 * Simulates Gateway C payment processing.
 * This gateway always succeeds (tertiary provider).
 */
public class GatewayC implements PaymentProvider {

    @Override
    public boolean processPayment(double amount, String paymentMethod) {
        System.out.println("  Gateway C: Received " + paymentMethod + " payment of Rs." +
                String.format("%.2f", amount));
        System.out.println("  Gateway C: Routing through express channel...");
        System.out.println("  Gateway C: Payment processed successfully.");
        return true;
    }

    @Override
    public String getProviderName() {
        return "Gateway C";
    }
}
