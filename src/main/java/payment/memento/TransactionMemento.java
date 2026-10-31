package payment.memento;

/** Memento pattern: opaque snapshot of mutable transaction state. */
public final class TransactionMemento {
    private final String status, failureReason;
    public TransactionMemento(String status,String failureReason){this.status=status;this.failureReason=failureReason;}
    public String getStatus(){return status;} public String getFailureReason(){return failureReason;}
}
