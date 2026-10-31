package payment;

import payment.abstractfactory.*;
import payment.bridge.*;
import payment.builder.PaymentRequest;
import payment.chain.*;
import payment.factory.PaymentProcessor;
import payment.factory.PaymentProcessorFactory;
import payment.mediator.PaymentMediator;
import payment.model.Transaction;
import payment.model.TransactionStore;
import payment.prototype.PaymentProfile;
import payment.proxy.PaymentProxy;
import payment.proxy.RealPaymentService;
import payment.workflow.StandardPaymentWorkflow;

/** Shared orchestration used by the console application and HTTP adapter. */
public final class PaymentApplicationService {
    private final TransactionStore store;

    public PaymentApplicationService() {
        this(TransactionStore.getInstance());
    }

    public PaymentApplicationService(TransactionStore store) {
        this.store = store;
    }

    public Transaction processPayment(PaymentRequest request) {
        PaymentProfile profileTemplate = new PaymentProfile("UPI", "Gateway A", 3);
        PaymentProfile profile = profileTemplate.copyFor(request.getPaymentMethod(), request.getProvider());
        PaymentProcessor processor = PaymentProcessorFactory.createProcessor(request.getPaymentMethod());
        PaymentComponentFactory factory = getComponentFactory(request.getPaymentMethod());
        Transaction transaction = store.createTransaction(
                request.getAmount(), request.getPaymentMethod(), request.getProvider());
        Payment payment = getBridgePayment(profile.getPaymentMethod(), getProvider(profile.getProvider()));
        RealPaymentService realService = new RealPaymentService(payment, processor);
        PaymentProxy proxy = new PaymentProxy(realService, request.getUserId());
        PaymentMediator mediator = new PaymentMediator(
                factory.createValidator(), proxy, factory.createReceiptGenerator());
        new StandardPaymentWorkflow().process(transaction, mediator);
        return transaction;
    }

    public Transaction simulateFailure(double amount, String paymentMethod, String provider,
                                       FailureType failureType, boolean retrySucceeds) {
        PaymentComponentFactory factory = getComponentFactory(paymentMethod);
        Transaction transaction = store.createTransaction(amount, paymentMethod, provider);
        transaction.setStatus(Transaction.STATUS_PROCESSING);
        transaction.setStatus(Transaction.STATUS_FAILED);
        transaction.setFailureReason(failureType.name());

        ValidationFailureHandler validation = new ValidationFailureHandler();
        GatewayFailureHandler gateway = new GatewayFailureHandler();
        RetryHandler retry = new RetryHandler(retrySucceeds);
        SupportHandler support = new SupportHandler();
        validation.setNext(gateway);
        gateway.setNext(retry);
        retry.setNext(support);
        boolean resolved = validation.handle(transaction, failureType);
        if (resolved) {
            factory.createReceiptGenerator().generateReceipt(transaction.getTransactionId(), amount);
        }
        return transaction;
    }

    private static PaymentComponentFactory getComponentFactory(String method) {
        switch (method) {
            case "UPI": return new UPIPaymentFactory();
            case "Card": return new CardPaymentFactory();
            case "Net Banking": return new NetBankingPaymentFactory();
            case "Wallet": return new WalletPaymentFactory();
            default: throw new IllegalArgumentException("Unknown payment method: " + method);
        }
    }

    private static PaymentProvider getProvider(String name) {
        switch (name) {
            case "Gateway A": return new GatewayA();
            case "Gateway B": return new GatewayB();
            case "Gateway C": return new GatewayC();
            default: throw new IllegalArgumentException("Unknown provider: " + name);
        }
    }

    private static Payment getBridgePayment(String method, PaymentProvider provider) {
        switch (method) {
            case "UPI": return new UPIPayment(provider);
            case "Card": return new CardPayment(provider);
            case "Net Banking": return new NetBankingPayment(provider);
            case "Wallet": return new WalletPayment(provider);
            default: throw new IllegalArgumentException("Unknown payment method: " + method);
        }
    }
}
