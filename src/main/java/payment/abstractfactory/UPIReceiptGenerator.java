package payment.abstractfactory;

/**
 * Concrete receipt generator for UPI payments.
 */
public class UPIReceiptGenerator implements ReceiptGenerator {

    @Override
    public void generateReceipt(String transactionId, double amount) {
        System.out.println("\n  +-------------------------------------+");
        System.out.println("  |        UPI PAYMENT RECEIPT           |");
        System.out.println("  +-------------------------------------+");
        System.out.printf("  |  Transaction ID : %-18s |%n", transactionId);
        System.out.printf("  |  Amount         : Rs.%-17.2f |%n", amount);
        System.out.println("  |  Method         : UPI                |");
        System.out.println("  |  Status         : SUCCESS            |");
        System.out.println("  +-------------------------------------+");
    }
}
