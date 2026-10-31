package payment.prototype;

/** Prototype pattern: clone a reusable configuration template for each transaction. */
public final class PaymentProfile implements Cloneable {
    private String paymentMethod; private String provider; private int maxRetries;
    public PaymentProfile(String paymentMethod,String provider,int maxRetries){
        this.paymentMethod=paymentMethod; this.provider=provider; this.maxRetries=maxRetries;
    }
    public PaymentProfile copyFor(String method,String gateway){
        PaymentProfile copy=clone(); copy.paymentMethod=method; copy.provider=gateway; return copy;
    }
    @Override public PaymentProfile clone(){
        try{return (PaymentProfile)super.clone();}catch(CloneNotSupportedException e){throw new AssertionError(e);}
    }
    public String getPaymentMethod(){return paymentMethod;} public String getProvider(){return provider;}
    public int getMaxRetries(){return maxRetries;}
}
