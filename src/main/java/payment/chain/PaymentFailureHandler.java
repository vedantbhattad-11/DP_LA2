package payment.chain;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Chain of Responsibility - Abstract Handler
 *
 * This abstract class defines the chain structure.
 * Each concrete handler decides whether it can handle a failure;
 * if not, it passes the request to the next handler in the chain.
 *
 * Chain order:
 *   ValidationFailureHandler -> GatewayFailureHandler -> RetryHandler -> SupportHandler
 *
 * WHY Chain of Responsibility?
 * - Different types of failures need different handling strategies.
 * - The sender (payment system) doesn't need to know which handler
 *   will ultimately process the failure.
 * - Handlers can be added, removed, or reordered without changing
 *   the client code.
 */
public abstract class PaymentFailureHandler {

    protected PaymentFailureHandler nextHandler;
    protected String handlerName;

    public PaymentFailureHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    /**
     * Sets the next handler in the chain.
     * @param next the next handler
     * @return the next handler (for chaining setNext calls)
     */
    public PaymentFailureHandler setNext(PaymentFailureHandler next) {
        this.nextHandler = next;
        return next;
    }

    /**
     * Attempt to handle the failure.
     * If this handler cannot handle it, pass to the next handler.
     *
     * @param transaction the failed transaction
     * @param failureType the type of failure
     * @return true if the failure was resolved, false if it was escalated
     */
    public boolean handle(Transaction transaction, FailureType failureType) {
        if (canHandle(failureType)) {
            return doHandle(transaction, failureType);
        } else {
            System.out.println("  " + handlerName + " -> Cannot handle " + failureType.name() +
                    ", passing to next handler...");
            if (nextHandler != null) {
                return nextHandler.handle(transaction, failureType);
            } else {
                System.out.println("  " + handlerName + " -> No more handlers in the chain. Failure unresolved.");
                return false;
            }
        }
    }

    /**
     * Check if this handler can handle the given failure type.
     */
    protected abstract boolean canHandle(FailureType failureType);

    /**
     * Actually handle the failure.
     */
    protected abstract boolean doHandle(Transaction transaction, FailureType failureType);
}
