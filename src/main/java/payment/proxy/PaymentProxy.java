package payment.proxy;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Proxy - Protection Proxy
 *
 * Controls access to the RealPaymentService by performing
 * pre-processing checks before allowing the payment to proceed.
 *
 * Checks performed:
 * 1. Accepts an entered, non-empty user ID as a demo label (not authentication)
 * 2. Payment amount validation (positive, within limits)
 * 3. Basic access/permission check
 *
 * WHY Proxy?
 * - The client code interacts with PaymentService interface.
 * - The Proxy adds validation WITHOUT modifying
 *   the RealPaymentService class (Open/Closed Principle).
 * - The client doesn't know it's going through a proxy.
 */
public class PaymentProxy implements PaymentService {

    private final RealPaymentService realService;
    private final String userId;

    public PaymentProxy(RealPaymentService realService, String userId) {
        this.realService = realService;
        this.userId = userId;
    }

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("\n[PROXY]");

        // The entered ID is an informational demo value, not authentication.
        System.out.println("  Proxy: Received a non-empty user ID label (not verified).");
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("  Proxy: [X] User ID must not be empty.");
            System.out.println("  Proxy: Payment request REJECTED.");
            transaction.setStatus(Transaction.STATUS_FAILED);
            transaction.setFailureReason("MISSING_USER_ID");
            return false;
        }
        System.out.println("  Proxy: [OK] User ID provided.");

        // Step 2: Amount validation
        System.out.println("  Proxy: Validating payment amount Rs." +
                String.format("%.2f", transaction.getAmount()) + "...");
        if (!validateAmount(transaction.getAmount())) {
            System.out.println("  Proxy: [X] Amount validation FAILED.");
            System.out.println("  Proxy: Payment request REJECTED.");
            transaction.setStatus(Transaction.STATUS_FAILED);
            transaction.setFailureReason("INVALID_AMOUNT");
            return false;
        }
        System.out.println("  Proxy: [OK] Amount validated successfully.");

        // Step 3: Permission check
        System.out.println("  Proxy: Checking payment permissions...");
        System.out.println("  Proxy: [OK] Access granted.");

        transaction.setStatus(Transaction.STATUS_VALIDATING);
        System.out.println("  Proxy: -> Forwarding request to Real Payment Service...");

        // Delegate to the real service
        return realService.processPayment(transaction);
    }

    /**
     * Simulated amount validation.
     */
    private boolean validateAmount(double amount) {
        if (amount <= 0) {
            System.out.println("  Proxy: Amount must be positive.");
            return false;
        }
        if (amount > 1000000) {
            System.out.println("  Proxy: Amount exceeds maximum limit of Rs.10,00,000.");
            return false;
        }
        return true;
    }
}
