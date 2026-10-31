package payment.abstractfactory;

/**
 * Concrete validator for Wallet payments.
 */
public class WalletValidator implements PaymentValidator {

    @Override
    public boolean validate(double amount) {
        System.out.println("  Wallet Validator: Checking wallet balance and amount...");
        if (amount <= 0) {
            System.out.println("  Wallet Validator: [X] Invalid amount.");
            return false;
        }
        if (amount > 50000) {
            System.out.println("  Wallet Validator: [X] Wallet limit exceeded (max Rs.50,000).");
            return false;
        }
        System.out.println("  Wallet Validator: [OK] Validation passed.");
        return true;
    }
}
