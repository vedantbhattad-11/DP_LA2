package payment.chain;

/**
 * Enum representing the different types of payment failures.
 * Used by the Chain of Responsibility to determine which handler
 * should process the failure.
 */
public enum FailureType {

    INVALID_AMOUNT("Invalid payment amount"),
    INVALID_PAYMENT_DETAILS("Invalid payment details (card number, UPI ID, etc.)"),
    GATEWAY_TIMEOUT("Payment gateway timed out"),
    GATEWAY_DOWN("Payment gateway is down"),
    NETWORK_ERROR("Network connectivity error"),
    PAYMENT_DECLINED("Payment was declined by the bank/provider"),
    UNKNOWN_ERROR("An unknown error occurred");

    private final String description;

    FailureType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name() + " - " + description;
    }
}
