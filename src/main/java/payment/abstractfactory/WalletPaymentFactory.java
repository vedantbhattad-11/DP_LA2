package payment.abstractfactory;

import payment.factory.PaymentProcessor;
import payment.factory.WalletPaymentProcessor;

/**
 * DESIGN PATTERN: Abstract Factory - Concrete Factory
 *
 * Creates a family of Wallet-related payment components:
 *   - WalletPaymentProcessor
 *   - WalletValidator
 *   - WalletReceiptGenerator
 */
public class WalletPaymentFactory implements PaymentComponentFactory {

    @Override
    public PaymentProcessor createProcessor() {
        return new WalletPaymentProcessor();
    }

    @Override
    public PaymentValidator createValidator() {
        return new WalletValidator();
    }

    @Override
    public ReceiptGenerator createReceiptGenerator() {
        return new WalletReceiptGenerator();
    }
}
