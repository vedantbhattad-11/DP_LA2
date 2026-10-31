package payment.abstractfactory;

import payment.factory.PaymentProcessor;
import payment.factory.CardPaymentProcessor;

/**
 * DESIGN PATTERN: Abstract Factory - Concrete Factory
 *
 * Creates a family of Card-related payment components:
 *   - CardPaymentProcessor
 *   - CardValidator
 *   - CardReceiptGenerator
 */
public class CardPaymentFactory implements PaymentComponentFactory {

    @Override
    public PaymentProcessor createProcessor() {
        return new CardPaymentProcessor();
    }

    @Override
    public PaymentValidator createValidator() {
        return new CardValidator();
    }

    @Override
    public ReceiptGenerator createReceiptGenerator() {
        return new CardReceiptGenerator();
    }
}
