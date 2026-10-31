# Viva Guide and Setup Instructions

## Run in VS Code

1. Open the `payment-processing-system` folder in VS Code.
2. Ensure a JDK 11+ and the VS Code Extension Pack for Java are installed.
3. Open `src/main/java/payment/Main.java` and click **Run**, or open a terminal in the project root and run:

```bash
javac -encoding UTF-8 -d out @sources.txt
java -cp out payment.Main
```

Windows users can run `run.bat`; macOS/Linux users can run `./run.sh`.

## The 11 core design patterns

### 1. Factory Method
- **Purpose:** Encapsulates creation of a payment-method-specific processor.
- **Classes:** `PaymentProcessorFactory`, `PaymentProcessor`, `UPIPaymentProcessor`, `CardPaymentProcessor`, `NetBankingPaymentProcessor`, `WalletPaymentProcessor`.
- **In this app:** `Main` creates the selected processor and `RealPaymentService` invokes its method-specific processing step.

### 2. Abstract Factory
- **Purpose:** Creates a family of related products that belong together.
- **Classes:** `PaymentComponentFactory`, concrete payment factories, `PaymentValidator`, `ReceiptGenerator`.
- **In this app:** The chosen method determines the matching validator and receipt generator.

### 3. Bridge
- **Purpose:** Separates payment method from gateway provider so both can vary independently.
- **Classes:** `Payment`, concrete payment types, `PaymentProvider`, `GatewayA/B/C`.
- **In this app:** A UPI payment can be paired with Gateway A, B, or C.

### 4. Proxy
- **Purpose:** Controls access to the real service.
- **Classes:** `PaymentService`, `PaymentProxy`, `RealPaymentService`.
- **In this app:** The proxy checks a demonstration user allow-list and amount limits before forwarding.

### 5. Chain of Responsibility
- **Purpose:** Passes a failure through handlers until it is resolved or escalated.
- **Classes:** `PaymentFailureHandler`, `ValidationFailureHandler`, `GatewayFailureHandler`, `RetryHandler`, `SupportHandler`.
- **In this app:** Validation failures are handled separately; recoverable gateway failures can be retried, then escalated.

### 6. Builder
- **Purpose:** Builds a complex object step by step and validates required fields at construction.
- **Classes:** `PaymentRequest` and nested `PaymentRequest.Builder`.
- **In this app:** Amount, payment method, provider, user ID, and retry policy are assembled into a request.

### 7. Prototype
- **Purpose:** Creates a new object by copying a reusable prototype.
- **Class:** `PaymentProfile`.
- **In this app:** A default profile is cloned and the copy is customized for the selected method and gateway.

### 8. Mediator
- **Purpose:** Centralizes communication among cooperating components.
- **Class:** `PaymentMediator`.
- **In this app:** It coordinates validation, payment execution, and receipt generation.

### 9. Template Method
- **Purpose:** Defines the invariant algorithm skeleton while allowing selected steps to vary.
- **Classes:** `PaymentWorkflowTemplate`, `StandardPaymentWorkflow`.
- **In this app:** The workflow always calls the before hook, mediator execution, and after hook in that order.

### 10. Strategy
- **Purpose:** Encapsulates interchangeable algorithms behind one interface.
- **Classes:** `RetryStrategy`, `StandardRetryStrategy`.
- **In this app:** Retry limit and retry/success decisions are separated from the handler that executes retries.

### 11. Memento
- **Purpose:** Captures and restores an object's prior state without exposing the internal state representation to the caretaker.
- **Classes:** `Transaction`, `TransactionMemento`.
- **In this app:** A transaction status/failure checkpoint is restored after an unsuccessful simulated retry attempt.

### Supporting pattern: Singleton
`TransactionStore.getInstance()` returns one in-memory store instance. It is an existing supporting pattern and is not counted in the 11 core patterns requested for this extension.

## Demo scenarios

- **Successful payment:** Choose menu option 1, choose method/provider, enter a valid user such as `user001`, and enter a positive amount.
- **Retry succeeds:** Choose option 3, select a recoverable failure such as `GATEWAY_TIMEOUT`, and choose retry success.
- **Escalation:** Choose option 3 and select retry failure; the chain exhausts retries and escalates to support.
- **Authentication failure:** In option 1, enter a user ID not in the allow-list.

This is a design-pattern demonstration, not a production payment system. It does not process real payments, use a real database, or provide production-grade authentication.
