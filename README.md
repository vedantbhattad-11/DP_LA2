# Smart Payment Processing and Failure Handling System

A Java console application and local web interface for exploring a simulated payment workflow built around design patterns. No real payments are sent, no payment credentials are collected, and the entered user ID is a label rather than authentication.

## Existing design patterns

| Pattern | Implementation | Role |
|---|---|---|
| Factory Method | `payment.factory.PaymentProcessorFactory` | Selects a payment-method processor. |
| Abstract Factory | `payment.abstractfactory.*PaymentFactory` | Creates matching validators and receipt generators. |
| Bridge | `payment.bridge.Payment` + `PaymentProvider` | Separates payment method from gateway. |
| Proxy | `payment.proxy.PaymentProxy` | Validates amount and forwards the request. An entered ID is not verified. |
| Chain of Responsibility | `payment.chain.*Handler` | Routes payment failures to validation, gateway, retry, and support handlers. |
| Builder | `payment.builder.PaymentRequest` | Assembles and validates payment requests. |
| Prototype | `payment.prototype.PaymentProfile` | Copies the reusable payment configuration. |
| Mediator | `payment.mediator.PaymentMediator` | Coordinates validator, payment service, and receipt. |
| Template Method | `payment.workflow.PaymentWorkflowTemplate` | Preserves the standard workflow order. |
| Strategy | `payment.strategy.RetryStrategy` | Selects retry behavior. |
| Memento | `payment.memento.TransactionMemento` | Saves transaction state during retries. |

`TransactionStore` also uses the Singleton pattern. It remains in memory and is shared by the console and web entry points while each is running.

## Console application

Requirements: JDK 11 or newer.

From the project root:

```powershell
javac -encoding UTF-8 -d out @sources.txt
java -cp out payment.Main
```

Or on Windows run `run.bat`; on macOS/Linux run `./run.sh`. This remains an independent build path and does not require Spring Boot or Maven.

## Web application

The HTTP integration is isolated in `integration/backend`; the React/TypeScript client is in `integration/frontend`. The Spring application compiles the existing Java sources from `src/main/java` directly, then calls their shared `payment.PaymentApplicationService`. It does not copy the payment behavior into the controllers.

### Requirements and versions

- JDK 17 or newer for the web module. Spring Boot 4.1.2 is configured with Java 17 source compatibility and supports the installed JDK 26. The project console source itself supports JDK 11+.
- Apache Maven 3.9+.
- Node.js 20.19+ or 22.12+ and npm (Vite 8 requirement).

### Start Spring Boot

In one terminal, from the project root:

```powershell
cd integration/backend
mvn spring-boot:run
```

The API listens on port `8080`. Optional environment variables:

| Variable | Default | Purpose |
|---|---|---|
| `SERVER_PORT` | `8080` | Spring Boot HTTP port. |
| `FRONTEND_ORIGINS` | `http://localhost:5173,http://127.0.0.1:5173` | Comma-separated browser origins allowed by CORS. |

For a production-style build, run `mvn clean package` in `integration/backend`, then `java -jar target/payment-web-integration-1.0.0.jar`.

### Start the frontend

In a second terminal, from the project root:

```powershell
cd integration/frontend
npm install
Copy-Item .env.example .env.local
npm run dev
```

Open `http://localhost:5173`. Set `VITE_API_BASE_URL` in `.env.local` if the API is hosted at another base URL, for example `http://localhost:8080/api`. The Vite server uses port `5173` and refuses to silently choose a different port. Build the frontend with `npm run build`; use `npm run preview` to serve the built output locally.

## HTTP API

All endpoints are under `/api`.

| Method and path | Behavior |
|---|---|
| `GET /options` | Lists payment methods, providers, and failure codes/descriptions for the UI. |
| `POST /payments` | Builds a `PaymentRequest` and executes the existing workflow. |
| `POST /failures` | Creates a simulated failure and runs the existing failure chain with the selected retry outcome. |
| `GET /transactions` | Lists the current in-memory records. |
| `GET /transactions/{id}` | Returns a transaction, or `404` if it is unknown. |

Payment JSON: `{"amount":1250,"method":"UPI","provider":"Gateway A","userId":"demo-operator"}`. Failure JSON additionally accepts `"failureType":"GATEWAY_TIMEOUT"` and `"retrySucceeds":true`. The methods, providers, and failure type codes are returned by `/api/options` and are not hardcoded in the UI.

Invalid inputs return `400` with a clear `message`. A payment that the existing workflow rejects returns `422` with its transaction status and reason; failure simulation also returns `422` when the chain leaves the payment failed or escalates it. Failure responses include the selected `failureType` and a `retryOutcome` (`RETRY_SUCCEEDED`, `RETRIES_EXHAUSTED_ESCALATED`, `ESCALATED_WITHOUT_RETRY`, or `NOT_RETRIED`). Successful workflow responses return `200`. Transaction details use `200` or `404`. Unexpected server errors return a generic message without a stack trace.

## Integration notes and limitations

- The console entry point now calls the shared `PaymentApplicationService` for payment processing and failure simulation. This small extraction lets the HTTP adapter invoke the same existing pattern implementations.
- `PaymentProxy` no longer checks a hardcoded user allowlist. It accepts a non-empty entered ID for the demo and retains positive-amount validation and the Rs. 10,00,000 upper limit. This ID is not authentication or identity verification.
- The payment gateways and failure outcomes are simulations. They do not connect to banks or move money.
- Transactions are held in the existing singleton in-memory store. They disappear when the backend process exits; there is no database or persistence.
- The failure chain is the existing Chain of Responsibility. Validation failures cannot be retried; gateway and declined failures follow the existing retry strategy and can escalate to support.
- The API does not ask for card numbers, UPI IDs, wallet credentials, bank credentials, or identity data. Do not connect this demo to real payment rails.

## UML and presentation

- `UML_DOCUMENTATION.md` describes the patterns and workflows.
- `UML_CLASS_DIAGRAM.png`, `.svg`, and `.dot` provide the class diagram.
- `Smart-Payment-Processing-and-Failure-Handling-System.pptx` is the project presentation.
