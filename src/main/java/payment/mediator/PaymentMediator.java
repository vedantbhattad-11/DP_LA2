package payment.mediator;

import payment.abstractfactory.PaymentValidator;
import payment.abstractfactory.ReceiptGenerator;
import payment.model.Transaction;
import payment.proxy.PaymentService;

/** Mediator pattern: coordinates validation, payment service, and receipt generation. */
public final class PaymentMediator {
    private final PaymentValidator validator; private final PaymentService paymentService;
    private final ReceiptGenerator receiptGenerator;
    public PaymentMediator(PaymentValidator validator,PaymentService paymentService,ReceiptGenerator receiptGenerator){
        this.validator=validator;this.paymentService=paymentService;this.receiptGenerator=receiptGenerator;
    }
    public boolean execute(Transaction txn){
        System.out.println("[MEDIATOR] Coordinating validator, payment service, and receipt generator.");
        if(!validator.validate(txn.getAmount())){
            txn.setStatus(Transaction.STATUS_FAILED);txn.setFailureReason("VALIDATION_FAILED");return false;
        }
        boolean success=paymentService.processPayment(txn);
        if(success) receiptGenerator.generateReceipt(txn.getTransactionId(),txn.getAmount());
        return success;
    }
}
