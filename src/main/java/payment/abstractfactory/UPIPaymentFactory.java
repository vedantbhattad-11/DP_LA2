package payment.abstractfactory;

import payment.factory.PaymentProcessor;
import payment.factory.UPIPaymentProcessor;

/**
 * DESIGN PATTERN: Abstract Factory - Concrete Factory
 *
 * Creates a family of UPI-related payment components:
 *   - UPIPaymentProcessor
 *   - UPIValidator
 *   - UPIReceiptGenerator
 *
 * All products created by this factory belong to the UPI payment family.
 */
public class UPIPaymentFactory implements PaymentComponentFactory {

    @Override
    public PaymentProcessor createProcessor() {
        return new UPIPaymentProcessor();
    }

    @Override
    public PaymentValidator createValidator() {
        return new UPIValidator();
    }

    @Override
    public ReceiptGenerator createReceiptGenerator() {
        return new UPIReceiptGenerator();
    }
}
