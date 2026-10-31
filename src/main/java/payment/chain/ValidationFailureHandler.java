package payment.chain;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Chain of Responsibility - Concrete Handler
 *
 * Handles validation-related failures:
 *   - INVALID_AMOUNT
 *   - INVALID_PAYMENT_DETAILS
 *
 * These failures cannot be retried - the user must correct the input.
 */
public class ValidationFailureHandler extends PaymentFailureHandler {

    public ValidationFailureHandler() {
        super("Validation Handler");
    }

    @Override
    protected boolean canHandle(FailureType failureType) {
        return failureType == FailureType.INVALID_AMOUNT ||
               failureType == FailureType.INVALID_PAYMENT_DETAILS;
    }

    @Override
    protected boolean doHandle(Transaction transaction, FailureType failureType) {
        System.out.println("  Validation Handler -> Handling: " + failureType.getDescription());

        if (failureType == FailureType.INVALID_AMOUNT) {
            System.out.println("  Validation Handler -> Please enter a valid payment amount.");
            System.out.println("  Validation Handler -> Amount must be positive and within limits.");
        } else {
            System.out.println("  Validation Handler -> Please check your payment details.");
            System.out.println("  Validation Handler -> Ensure card number, UPI ID, or account details are correct.");
        }

        transaction.setStatus(Transaction.STATUS_FAILED);
        transaction.setFailureReason(failureType.name());
        System.out.println("  Validation Handler -> Status: FAILED (user correction required)");
        return false;  // Validation errors can't be auto-resolved
    }
}
