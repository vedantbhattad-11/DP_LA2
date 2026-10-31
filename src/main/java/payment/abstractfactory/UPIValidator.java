package payment.abstractfactory;

/**
 * Concrete validator for UPI payments.
 */
public class UPIValidator implements PaymentValidator {

    @Override
    public boolean validate(double amount) {
        System.out.println("  UPI Validator: Checking UPI ID format and amount...");
        if (amount <= 0) {
            System.out.println("  UPI Validator: [X] Invalid amount.");
            return false;
        }
        if (amount > 100000) {
            System.out.println("  UPI Validator: [X] UPI limit exceeded (max Rs.1,00,000).");
            return false;
        }
        System.out.println("  UPI Validator: [OK] Validation passed.");
        return true;
    }
}
