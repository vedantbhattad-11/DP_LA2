package payment.strategy;

/** Strategy pattern: interchangeable retry policies. */
public interface RetryStrategy {
    int getMaxAttempts();
    boolean shouldRetry(String failureType, int attempt);
    boolean simulatedRetrySucceeds(int attempt);
    String getName();
}
