package payment;

import java.util.Scanner;

import payment.builder.PaymentRequest;
import payment.chain.*;
import payment.model.*;

/**
 * Main entry point for the Smart Payment Processing and Failure Handling
 * System.
 *
 * This console application demonstrates 11 core Design Patterns:
 * 1. Factory Method - Creating the appropriate payment processor
 * 2. Abstract Factory - Creating a family of payment components
 * 3. Bridge - Separating payment type from payment provider
 * 4. Proxy - Controlling access to the real payment service
 * 5. Chain of Responsibility - Handling payment failures with a handler chain
 * 6. Builder - Building validated payment requests
 * 7. Prototype - Cloning payment configuration profiles
 * 8. Mediator - Coordinating payment workflow components
 * 9. Template Method - Defining the payment workflow skeleton
 * 10. Strategy - Selecting retry behavior
 * 11. Memento - Saving and restoring transaction state
 *
 * Run this class to start the interactive menu.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TransactionStore store = TransactionStore.getInstance();
    private static final PaymentApplicationService paymentService = new PaymentApplicationService(store);

    public static void main(String[] args) {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("    SMART PAYMENT PROCESSING AND FAILURE HANDLING SYSTEM");
        System.out.println("========================================================");
        System.out.println("  Core Patterns: Factory Method | Abstract Factory | Bridge | Proxy");
        System.out.println("                Chain of Responsibility | Builder | Prototype");
        System.out.println("                Mediator | Template Method | Strategy | Memento");
        System.out.println("========================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput("Enter your choice: ", 1, 4);

            switch (choice) {
                case 1:
                    makePayment();
                    break;
                case 2:
                    viewTransactions();
                    break;
                case 3:
                    simulatePaymentFailure();
                    break;
                case 4:
                    System.out.println("\n  Thank you for using Smart Payment System. Goodbye!\n");
                    running = false;
                    break;
            }
        }

        scanner.close();
    }

    // ==========================================
    // MAIN MENU
    // ==========================================

    private static void printMainMenu() {
        System.out.println("\n+==========================================+");
        System.out.println("|           MAIN MENU                      |");
        System.out.println("+==========================================+");
        System.out.println("|  1. Make Payment                         |");
        System.out.println("|  2. View Transactions                    |");
        System.out.println("|  3. Simulate Payment Failure             |");
        System.out.println("|  4. Exit                                 |");
        System.out.println("+==========================================+");
    }

    // ==========================================
    // OPTION 1: MAKE PAYMENT (Happy Path)
    // ==========================================

    private static void makePayment() {
        System.out.println("\n------------------------------------------");
        System.out.println("  MAKE A PAYMENT");
        System.out.println("------------------------------------------");
        String method = selectPaymentMethod();
        double amount = readDoubleInput("  Enter amount (Rs.): ");
        String providerName = selectProvider();
        System.out.print("  Enter a non-empty User ID label (not verified): ");
        String userId = scanner.nextLine().trim();

        // BUILDER: create one validated request with all required fields.
        PaymentRequest request = PaymentRequest.builder().amount(amount).paymentMethod(method)
                .provider(providerName).userId(userId).retryPolicy("STANDARD").build();
        System.out.println("\n[BUILDER] Validated payment request assembled.");

        // PROTOTYPE: clone a reusable profile and customize the clone for this payment.
        Transaction transaction = paymentService.processPayment(request);
        boolean success = Transaction.STATUS_SUCCESS.equals(transaction.getStatus());

        System.out.println("\n==========================================");
        System.out.println("[RESULT]");
        if (success) {
            System.out.println("  [OK] Payment SUCCESSFUL!");
            System.out.println("  Transaction ID: " + transaction.getTransactionId());
        } else {
            System.out.println("  [X] Payment FAILED.");
            System.out.println("  Transaction ID: " + transaction.getTransactionId());
            System.out.println("  Reason: " + transaction.getFailureReason());
        }
        System.out.println("==========================================");
    }

    // ==========================================
    // OPTION 2: VIEW TRANSACTIONS
    // ==========================================

    private static void viewTransactions() {
        System.out.println("\n------------------------------------------");
        System.out.println("  ALL TRANSACTIONS");
        System.out.println("------------------------------------------");
        store.printAllTransactions();
    }

    // ==========================================
    // OPTION 3: SIMULATE PAYMENT FAILURE
    // ==========================================

    private static void simulatePaymentFailure() {
        System.out.println("\n------------------------------------------");
        System.out.println("  SIMULATE PAYMENT FAILURE");
        System.out.println("------------------------------------------");

        // Step 1: Select payment method
        String paymentMethod = selectPaymentMethod();

        // Step 2: Enter amount
        double amount = readDoubleInput("  Enter amount (Rs.): ");

        // Step 3: Select provider
        String providerName = selectProvider();

        // Step 4: Select failure type
        System.out.println("\n  Select failure type to simulate:");
        System.out.println("  1. GATEWAY_TIMEOUT");
        System.out.println("  2. GATEWAY_DOWN");
        System.out.println("  3. NETWORK_ERROR");
        System.out.println("  4. PAYMENT_DECLINED");
        System.out.println("  5. INVALID_AMOUNT");
        System.out.println("  6. INVALID_PAYMENT_DETAILS");
        System.out.println("  7. UNKNOWN_ERROR");
        int failChoice = readIntInput("  Enter failure type (1-7): ", 1, 7);

        FailureType failureType;
        switch (failChoice) {
            case 1:
                failureType = FailureType.GATEWAY_TIMEOUT;
                break;
            case 2:
                failureType = FailureType.GATEWAY_DOWN;
                break;
            case 3:
                failureType = FailureType.NETWORK_ERROR;
                break;
            case 4:
                failureType = FailureType.PAYMENT_DECLINED;
                break;
            case 5:
                failureType = FailureType.INVALID_AMOUNT;
                break;
            case 6:
                failureType = FailureType.INVALID_PAYMENT_DETAILS;
                break;
            case 7:
            default:
                failureType = FailureType.UNKNOWN_ERROR;
                break;
        }

        // Step 5: Should retry succeed?
        System.out.println("\n  Should the retry succeed? (for demonstrating different scenarios)");
        System.out.println("  1. Yes (Scenario 2: Retry succeeds)");
        System.out.println("  2. No  (Scenario 3: Retry fails -> Escalation)");
        int retryChoice = readIntInput("  Enter choice (1-2): ", 1, 2);
        boolean retrySucceeds = (retryChoice == 1);

        System.out.println("\n==========================================");
        System.out.println("  SIMULATING PAYMENT FAILURE");
        System.out.println("==========================================");

        // --- PATTERN 1: FACTORY METHOD ---
        // PaymentProcessor processor =
        // PaymentProcessorFactory.createProcessor(paymentMethod);

        // --- PATTERN 5: CHAIN OF RESPONSIBILITY ---
        System.out.println("\n[CHAIN OF RESPONSIBILITY]");
        System.out.println("  Payment failed with: " + failureType.name());
        System.out.println("  Initiating failure handling chain...");
        System.out
                .println("  Chain: Validation Handler -> Gateway Failure Handler -> Retry Handler -> Support Handler");
        System.out.println();

        Transaction transaction = paymentService.simulateFailure(amount, paymentMethod, providerName,
                failureType, retrySucceeds);
        boolean resolved = Transaction.STATUS_SUCCESS.equals(transaction.getStatus());

        // Print final result
        System.out.println("\n==========================================");
        System.out.println("[FINAL RESULT]");
        System.out.println("  Transaction ID: " + transaction.getTransactionId());
        System.out.println("  Status: " + transaction.getStatus());
        if (resolved) {
            System.out.println("  [OK] Payment was resolved through retry.");
        } else if (Transaction.STATUS_ESCALATED.equals(transaction.getStatus())) {
            System.out.println("  [!!] Payment has been ESCALATED to support team.");
            System.out.println("  Failure: " + transaction.getFailureReason());
        } else {
            System.out.println("  [X] Payment FAILED.");
            System.out.println("  Failure: " + transaction.getFailureReason());
        }
        System.out.println("==========================================");
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    private static String selectPaymentMethod() {
        System.out.println("\n  Select Payment Method:");
        System.out.println("  1. UPI");
        System.out.println("  2. Card (Credit/Debit)");
        System.out.println("  3. Net Banking");
        System.out.println("  4. Wallet");
        int choice = readIntInput("  Enter choice (1-4): ", 1, 4);
        switch (choice) {
            case 1:
                return "UPI";
            case 2:
                return "Card";
            case 3:
                return "Net Banking";
            case 4:
                return "Wallet";
            default:
                return "UPI";
        }
    }

    private static String selectProvider() {
        System.out.println("\n  Select Payment Provider (Gateway):");
        System.out.println("  1. Gateway A");
        System.out.println("  2. Gateway B");
        System.out.println("  3. Gateway C");
        int choice = readIntInput("  Enter choice (1-3): ", 1, 3);
        switch (choice) {
            case 1:
                return "Gateway A";
            case 2:
                return "Gateway B";
            case 3:
                return "Gateway C";
            default:
                return "Gateway A";
        }
    }

    /**
     * Read an integer input within a valid range.
     */
    private static int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("  Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Read a double (amount) input.
     */
    private static double readDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                double value = Double.parseDouble(line);
                if (Double.isFinite(value) && value > 0) {
                    return value;
                }
                System.out.println("  Amount must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a valid number.");
            }
        }
    }
}
