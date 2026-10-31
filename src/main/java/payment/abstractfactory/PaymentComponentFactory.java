package payment.abstractfactory;

import payment.factory.PaymentProcessor;

/**
 * DESIGN PATTERN: Abstract Factory - Abstract Factory Interface
 *
 * This interface defines methods to create a FAMILY of related payment objects:
 *   1. PaymentProcessor  - processes the payment
 *   2. PaymentValidator   - validates payment details
 *   3. ReceiptGenerator   - generates a receipt after payment
 *
 * Each concrete factory (UPIPaymentFactory, CardPaymentFactory, etc.)
 * creates objects that belong to the SAME payment method family.
 *
 * DIFFERENCE FROM FACTORY METHOD:
 * - Factory Method (PaymentProcessorFactory) creates ONE product - a PaymentProcessor.
 * - Abstract Factory creates a FAMILY of related products that work together.
 *   For example, UPIPaymentFactory creates a UPIPaymentProcessor,
 *   a UPIValidator, AND a UPIReceiptGenerator - all UPI-specific.
 */
public interface PaymentComponentFactory {

    /**
     * Creates the payment processor for this payment family.
     */
    PaymentProcessor createProcessor();

    /**
     * Creates the payment validator for this payment family.
     */
    PaymentValidator createValidator();

    /**
     * Creates the receipt generator for this payment family.
     */
    ReceiptGenerator createReceiptGenerator();
}
