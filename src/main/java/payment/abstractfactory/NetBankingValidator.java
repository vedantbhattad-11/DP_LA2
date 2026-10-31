package payment.abstractfactory;

/**
 * Concrete validator for Net Banking payments.
 */
public class NetBankingValidator implements PaymentValidator {

    @Override
    public boolean validate(double amount) {
        System.out.println("  Net Banking Validator: Checking bank account and amount...");
        if (amount <= 0) {
            System.out.println("  Net Banking Validator: [X] Invalid amount.");
            return false;
        }
        if (amount > 200000) {
            System.out.println("  Net Banking Validator: [X] Net Banking limit exceeded (max Rs.2,00,000).");
            return false;
        }
        System.out.println("  Net Banking Validator: [OK] Validation passed.");
        return true;
    }
}
