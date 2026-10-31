package payment.abstractfactory;

/**
 * DESIGN PATTERN: Abstract Factory - Abstract Validator Product
 *
 * Interface for payment-specific validators.
 * Each payment method has its own validation logic.
 */
public interface PaymentValidator {

    /**
     * Validates the payment details for a specific payment method.
     * @param amount the payment amount
     * @return true if validation passes, false otherwise
     */
    boolean validate(double amount);
}
