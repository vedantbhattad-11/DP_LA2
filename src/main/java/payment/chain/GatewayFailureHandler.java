package payment.chain;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Chain of Responsibility - Concrete Handler
 *
 * Handles gateway-related failures:
 *   - GATEWAY_TIMEOUT
 *   - GATEWAY_DOWN
 *   - NETWORK_ERROR
 *
 * This handler logs the gateway issue and then passes to the RetryHandler
 * to attempt a retry.
 */
public class GatewayFailureHandler extends PaymentFailureHandler {

    public GatewayFailureHandler() {
        super("Gateway Failure Handler");
    }

    @Override
    protected boolean canHandle(FailureType failureType) {
        return failureType == FailureType.GATEWAY_TIMEOUT ||
               failureType == FailureType.GATEWAY_DOWN ||
               failureType == FailureType.NETWORK_ERROR;
    }

    @Override
    protected boolean doHandle(Transaction transaction, FailureType failureType) {
        System.out.println("  Gateway Failure Handler -> Handling: " + failureType.getDescription());

        switch (failureType) {
            case GATEWAY_TIMEOUT:
                System.out.println("  Gateway Failure Handler -> Gateway timed out. This may be temporary.");
                break;
            case GATEWAY_DOWN:
                System.out.println("  Gateway Failure Handler -> Gateway is currently unavailable.");
                break;
            case NETWORK_ERROR:
                System.out.println("  Gateway Failure Handler -> Network connectivity issue detected.");
                break;
            default:
                break;
        }

        System.out.println("  Gateway Failure Handler -> Recommending retry...");

        // Pass to the next handler (RetryHandler) for retry attempt
        if (nextHandler != null) {
            return nextHandler.handle(transaction, failureType);
        }
        return false;
    }
}
