package payment.workflow;

import payment.mediator.PaymentMediator;
import payment.model.Transaction;

/** Template Method pattern: fixes the workflow order while subclasses customize hooks. */
public abstract class PaymentWorkflowTemplate {
    public final boolean process(Transaction transaction,PaymentMediator mediator){
        beforeProcessing(transaction);
        boolean success;
        try { success=mediator.execute(transaction); }
        finally { afterProcessing(transaction); }
        return success;
    }
    protected abstract void beforeProcessing(Transaction transaction);
    protected abstract void afterProcessing(Transaction transaction);
}
