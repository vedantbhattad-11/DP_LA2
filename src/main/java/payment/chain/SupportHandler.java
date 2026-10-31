package payment.chain;

import payment.model.Transaction;

/**
 * DESIGN PATTERN: Chain of Responsibility - Concrete Handler (Terminal)
 *
 * This is the LAST handler in the chain.
 * If no previous handler could resolve the failure, the issue is
 * escalated to human support.
 *
 * This handler always processes the request (it's the catch-all).
 */
public class SupportHandler extends PaymentFailureHandler {

    public SupportHandler() {
        super("Support Handler");
    }

    @Override
    protected boolean canHandle(FailureType failureType) {
        // SupportHandler is the catch-all - it handles everything
        return true;
    }

    @Override
    protected boolean doHandle(Transaction transaction, FailureType failureType) {
        System.out.println("  Support Handler -> Escalating to human support team...");
        System.out.println("  Support Handler -> Failure: " + failureType.getDescription());
        System.out.println("  Support Handler -> Transaction " + transaction.getTransactionId() +
                " has been ESCALATED.");
        System.out.println("  Support Handler -> A support ticket has been created.");
        System.out.println("  Support Handler -> The customer will be contacted within 24 hours.");

        transaction.setStatus(Transaction.STATUS_ESCALATED);
        transaction.setFailureReason(failureType.name() + " (ESCALATED)");
        return false;  // Not resolved - escalated to humans
    }
}
