package payment.factory;

/**
 * DESIGN PATTERN: Factory Method - Creator / Factory
 *
 * This class encapsulates the object creation logic.
 * Instead of the client (Main.java) directly instantiating concrete processors,
 * it delegates to this factory which decides WHICH concrete class to create
 * based on the payment method string.
 *
 * WHY Factory Method?
 * - The client code doesn't need to know the concrete class names.
 * - Adding a new payment method only requires adding a new case here
 *   and a new concrete processor class - no changes to client code.
 * - Follows the Open/Closed Principle.
 *
 * DIFFERENCE FROM ABSTRACT FACTORY:
 * - Factory Method creates ONE product (a PaymentProcessor).
 * - Abstract Factory creates a FAMILY of related products
 *   (PaymentProcessor + PaymentValidator + ReceiptGenerator).
 */
public class PaymentProcessorFactory {

    /**
     * Factory Method: Creates the appropriate PaymentProcessor
     * based on the selected payment method.
     *
     * @param paymentMethod one of "UPI", "Card", "Net Banking", "Wallet"
     * @return the appropriate PaymentProcessor instance
     * @throws IllegalArgumentException if the payment method is unknown
     */
    public static PaymentProcessor createProcessor(String paymentMethod) {
        System.out.println("\n[FACTORY METHOD]");
        System.out.println("  Creating " + paymentMethod + " Payment Processor...");

        PaymentProcessor processor;
        switch (paymentMethod) {
            case "UPI": processor = new UPIPaymentProcessor(); break;
            case "Card": processor = new CardPaymentProcessor(); break;
            case "Net Banking": processor = new NetBankingPaymentProcessor(); break;
            case "Wallet": processor = new WalletPaymentProcessor(); break;
            default: throw new IllegalArgumentException("Unknown payment method: " + paymentMethod);
        }

        System.out.println("  Factory Method: " + paymentMethod + " Payment Processor created successfully.");
        return processor;
    }
}
