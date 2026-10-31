package payment.workflow;

import payment.model.Transaction;

/** Concrete Template Method workflow for standard payments. */
public final class StandardPaymentWorkflow extends PaymentWorkflowTemplate {
    @Override protected void beforeProcessing(Transaction t){
        System.out.println("[TEMPLATE METHOD] Step 1/3: starting standard payment workflow for " + t.getTransactionId());
        t.setStatus(Transaction.STATUS_VALIDATING);
    }
    @Override protected void afterProcessing(Transaction t){
        System.out.println("[TEMPLATE METHOD] Step 3/3: workflow finished with status " + t.getStatus());
    }
}
