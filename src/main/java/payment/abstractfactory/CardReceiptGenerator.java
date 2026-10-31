package payment.abstractfactory;

/**
 * Concrete receipt generator for Card payments.
 */
public class CardReceiptGenerator implements ReceiptGenerator {

    @Override
    public void generateReceipt(String transactionId, double amount) {
        System.out.println("\n  +-------------------------------------+");
        System.out.println("  |        CARD PAYMENT RECEIPT          |");
        System.out.println("  +-------------------------------------+");
        System.out.printf("  |  Transaction ID : %-18s |%n", transactionId);
        System.out.printf("  |  Amount         : Rs.%-17.2f |%n", amount);
        System.out.println("  |  Method         : Credit/Debit Card  |");
        System.out.println("  |  Status         : SUCCESS            |");
        System.out.println("  +-------------------------------------+");
    }
}
