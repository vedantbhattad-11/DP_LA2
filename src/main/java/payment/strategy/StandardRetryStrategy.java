package payment.strategy;

/** Retries recoverable gateway/network failures up to three times. */
public final class StandardRetryStrategy implements RetryStrategy {
    private final boolean simulatedSuccess;
    public StandardRetryStrategy(boolean simulatedSuccess){this.simulatedSuccess=simulatedSuccess;}
    @Override public int getMaxAttempts(){return 3;}
    @Override public boolean shouldRetry(String type,int attempt){
        return attempt < getMaxAttempts() && ("GATEWAY_TIMEOUT".equals(type)||"GATEWAY_DOWN".equals(type)||"NETWORK_ERROR".equals(type)||"PAYMENT_DECLINED".equals(type));
    }
    @Override public boolean simulatedRetrySucceeds(int attempt){return simulatedSuccess && attempt==2;}
    @Override public String getName(){return "Standard retry (max 3 attempts)";}
}
