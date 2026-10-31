package payment.chain;

import payment.model.Transaction;
import payment.memento.TransactionMemento;
import payment.strategy.RetryStrategy;
import payment.strategy.StandardRetryStrategy;

/** Chain of Responsibility handler whose retry behavior is selected by Strategy. */
public class RetryHandler extends PaymentFailureHandler {
    private final RetryStrategy strategy;
    public RetryHandler(boolean retrySucceeds) { this(new StandardRetryStrategy(retrySucceeds)); }
    public RetryHandler(RetryStrategy strategy) { super("Retry Handler"); this.strategy=strategy; }
    @Override protected boolean canHandle(FailureType type) {
        return type==FailureType.GATEWAY_TIMEOUT || type==FailureType.GATEWAY_DOWN ||
               type==FailureType.NETWORK_ERROR || type==FailureType.PAYMENT_DECLINED;
    }
    @Override protected boolean doHandle(Transaction txn,FailureType type) {
        System.out.println("  Retry Handler -> Strategy: " + strategy.getName());
        TransactionMemento checkpoint=txn.saveState();
        for(int attempt=1;attempt<=strategy.getMaxAttempts();attempt++) {
            if(attempt>1 && !strategy.shouldRetry(type.name(),attempt-1)) break;
            txn.setStatus(Transaction.STATUS_RETRYING);
            System.out.println("  Retry Handler -> Attempt " + attempt + "/" + strategy.getMaxAttempts());
            if(strategy.simulatedRetrySucceeds(attempt)) {
                txn.setStatus(Transaction.STATUS_SUCCESS);txn.setFailureReason(null);
                System.out.println("  Retry Handler -> [OK] Retry succeeded on attempt " + attempt + ".");return true;
            }
            System.out.println("  Retry Handler -> Attempt failed.");
            txn.restoreState(checkpoint);
        }
        System.out.println("  Retry Handler -> Retries exhausted; passing to support.");
        if(nextHandler!=null)return nextHandler.handle(txn,type);
        txn.setStatus(Transaction.STATUS_FAILED);txn.setFailureReason(type.name());return false;
    }
}
