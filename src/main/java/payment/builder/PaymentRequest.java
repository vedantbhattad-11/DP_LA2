package payment.builder;

/** Builder pattern: immutable validated input assembled from optional payment fields. */
public final class PaymentRequest {
    private final double amount;
    private final String paymentMethod;
    private final String provider;
    private final String userId;
    private final String retryPolicy;

    private PaymentRequest(Builder b) {
        if (!Double.isFinite(b.amount) || b.amount <= 0) throw new IllegalArgumentException("Amount must be a finite positive number.");
        this.amount=b.amount; this.paymentMethod=require(b.paymentMethod,"payment method");
        this.provider=require(b.provider,"provider"); this.userId=require(b.userId,"user ID");
        this.retryPolicy=b.retryPolicy == null ? "STANDARD" : b.retryPolicy;
    }
    private static String require(String value,String field) {
        if(value==null || value.trim().isEmpty()) throw new IllegalArgumentException("Missing " + field + ".");
        return value.trim();
    }
    public double getAmount(){return amount;} public String getPaymentMethod(){return paymentMethod;}
    public String getProvider(){return provider;} public String getUserId(){return userId;}
    public String getRetryPolicy(){return retryPolicy;}
    public static Builder builder(){return new Builder();}
    public static final class Builder {
        private double amount; private String paymentMethod,provider,userId,retryPolicy;
        public Builder amount(double v){amount=v;return this;} public Builder paymentMethod(String v){paymentMethod=v;return this;}
        public Builder provider(String v){provider=v;return this;} public Builder userId(String v){userId=v;return this;}
        public Builder retryPolicy(String v){retryPolicy=v;return this;}
        public PaymentRequest build(){return new PaymentRequest(this);}
    }
}
