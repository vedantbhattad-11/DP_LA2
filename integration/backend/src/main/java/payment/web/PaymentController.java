package payment.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payment.PaymentApplicationService;
import payment.builder.PaymentRequest;
import payment.chain.FailureType;
import payment.model.Transaction;
import payment.model.TransactionStore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {
    private static final Set<String> METHODS = Set.of("UPI", "Card", "Net Banking", "Wallet");
    private static final Set<String> PROVIDERS = Set.of("Gateway A", "Gateway B", "Gateway C");
    private final PaymentApplicationService service = new PaymentApplicationService();
    private final TransactionStore store = TransactionStore.getInstance();

    @GetMapping("/options")
    public Map<String, Object> options() {
        return Map.of("methods", METHODS, "providers", PROVIDERS,
                "failureTypes", Arrays.stream(FailureType.values())
                        .map(type -> Map.of("code", type.name(), "description", type.getDescription()))
                        .collect(Collectors.toList()));
    }

    @PostMapping("/payments")
    public ResponseEntity<?> process(@Valid @RequestBody PaymentInput input) {
        Optional<String> invalid = validateChoices(input.method(), input.provider());
        if (invalid.isPresent()) return badRequest(invalid.get());
        PaymentRequest request = PaymentRequest.builder().amount(input.amount()).paymentMethod(input.method())
                .provider(input.provider()).userId(input.userId().trim()).retryPolicy("STANDARD").build();
        Transaction txn = service.processPayment(request);
        TransactionResponse response = TransactionResponse.from(txn);
        if (Transaction.STATUS_SUCCESS.equals(txn.getStatus())) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> transactions() {
        return store.getAllTransactions().stream().map(TransactionResponse::from).collect(Collectors.toList());
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<?> transaction(@PathVariable String id) {
        return store.getAllTransactions().stream().filter(txn -> txn.getTransactionId().equalsIgnoreCase(id))
                .findFirst().<ResponseEntity<?>>map(txn -> ResponseEntity.ok(TransactionResponse.from(txn)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Transaction " + id + " was not found.")));
    }

    @PostMapping("/failures")
    public ResponseEntity<?> simulate(@Valid @RequestBody FailureInput input) {
        Optional<String> invalid = validateChoices(input.method(), input.provider());
        if (invalid.isPresent()) return badRequest(invalid.get());
        FailureType type;
        try { type = FailureType.valueOf(input.failureType()); }
        catch (IllegalArgumentException ex) { return badRequest("Choose a supported failure type."); }
        Transaction txn = service.simulateFailure(input.amount(), input.method(), input.provider(), type,
                input.retrySucceeds());
        boolean retryable = type == FailureType.GATEWAY_TIMEOUT || type == FailureType.GATEWAY_DOWN
                || type == FailureType.NETWORK_ERROR || type == FailureType.PAYMENT_DECLINED;
        String retryOutcome = Transaction.STATUS_SUCCESS.equals(txn.getStatus()) ? "RETRY_SUCCEEDED"
                : Transaction.STATUS_ESCALATED.equals(txn.getStatus()) && retryable ? "RETRIES_EXHAUSTED_ESCALATED"
                : Transaction.STATUS_ESCALATED.equals(txn.getStatus()) ? "ESCALATED_WITHOUT_RETRY"
                : "NOT_RETRIED";
        TransactionResponse response = TransactionResponse.from(txn, type.name(), retryOutcome);
        if (Transaction.STATUS_SUCCESS.equals(txn.getStatus())) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    private Optional<String> validateChoices(String method, String provider) {
        if (!METHODS.contains(method)) return Optional.of("Choose a supported payment method.");
        if (!PROVIDERS.contains(provider)) return Optional.of("Choose a supported gateway provider.");
        return Optional.empty();
    }
    private ResponseEntity<ErrorResponse> badRequest(String message) {
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }

    public record PaymentInput(@jakarta.validation.constraints.DecimalMin(value="0.01")
                               @jakarta.validation.constraints.DecimalMax(value="1000000.00") double amount,
                               @jakarta.validation.constraints.NotBlank String method,
                               @jakarta.validation.constraints.NotBlank String provider,
                               @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(max=120) String userId) {}
    public record FailureInput(@jakarta.validation.constraints.DecimalMin(value="0.01")
                               @jakarta.validation.constraints.DecimalMax(value="1000000.00") double amount,
                               @jakarta.validation.constraints.NotBlank String method,
                               @jakarta.validation.constraints.NotBlank String provider,
                               @jakarta.validation.constraints.NotBlank String failureType,
                               boolean retrySucceeds) {}
    public record ErrorResponse(String message) {}
    public record TransactionResponse(String transactionId, double amount, String method, String provider,
                                      String status, String failureReason, String timestamp,
                                      String failureType, String retryOutcome) {
        static TransactionResponse from(Transaction txn) {
            return from(txn, null, null);
        }
        static TransactionResponse from(Transaction txn, String failureType, String retryOutcome) {
            return new TransactionResponse(txn.getTransactionId(), txn.getAmount(), txn.getPaymentMethod(),
                    txn.getProvider(), txn.getStatus(), txn.getFailureReason(), txn.getTimestamp().toString(),
                    failureType, retryOutcome);
        }
    }
}
