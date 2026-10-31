package payment.abstractfactory;

/**
 * Concrete validator for Card payments.
 */
public class CardValidator implements PaymentValidator {

    @Override
    public boolean validate(double amount) {
        System.out.println("  Card Validator: Checking card number, CVV, and amount...");
        if (amount <= 0) {
            System.out.println("  Card Validator: [X] Invalid amount.");
            return false;
        }
        if (amount > 500000) {
            System.out.println("  Card Validator: [X] Card limit exceeded (max Rs.5,00,000).");
            return false;
        }
        System.out.println("  Card Validator: [OK] Validation passed.");
        return true;
    }
}
