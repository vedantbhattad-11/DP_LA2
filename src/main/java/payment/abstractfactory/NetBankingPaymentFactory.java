package payment.abstractfactory;

import payment.factory.PaymentProcessor;
import payment.factory.NetBankingPaymentProcessor;

/**
 * DESIGN PATTERN: Abstract Factory - Concrete Factory
 *
 * Creates a family of Net Banking-related payment components:
 *   - NetBankingPaymentProcessor
 *   - NetBankingValidator
 *   - NetBankingReceiptGenerator
 */
public class NetBankingPaymentFactory implements PaymentComponentFactory {

    @Override
    public PaymentProcessor createProcessor() {
        return new NetBankingPaymentProcessor();
    }

    @Override
    public PaymentValidator createValidator() {
        return new NetBankingValidator();
    }

    @Override
    public ReceiptGenerator createReceiptGenerator() {
        return new NetBankingReceiptGenerator();
    }
}
