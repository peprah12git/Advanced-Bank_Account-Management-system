package utils;
import  java.util.regex.Pattern;
public class ValidationUtils {
    // Email validation pattern
    private  static final  Pattern EMAIL_PATTERN = Pattern.compile(
      "^[A-Za-z0-9+_.-]@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    // Account Number Pattern
    // Matches ACC001, ACC002
    static  final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^ACC\\d+$");
    private static final Pattern CUSTOMER_ID_PATTERN =  Pattern.compile("^CUS\\d+$");

    //phone number pattern{various format}
    //Matches 555-1234, {555} 123-4567
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^[+]?[(]?\\d{1,4}[)]?[-\\s.]?\\d{1,4}[-\\s.]?\\d{1,9}$");

    // Transaction ID pattern (TXN followed by timestamp-like number
    private static final Pattern TRANSACTION_ID_PATTERN = Pattern.compile("^TXN\\d+$"
    );
    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Validates an email address.
     *
     * @param email the email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates an email and provides helpful error message.
     *
     * @param email the email to validate
     * @throws IllegalArgumentException if email is invalid
     */
    public static void validateEmail(String email) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Error: Email cannot be empty.\n" +
                            "Please provide a valid email address."
            );
        }

        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException(
                    "Error: Invalid email format.\n" +
                            "Email must be in format: user@example.com\n" +
                            "Provided: " + email
            );
        }
    }

    /**
     * Validates an account number.
     *
     * @param accountNumber the account number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        return ACCOUNT_NUMBER_PATTERN.matcher(accountNumber.trim()).matches();
    }

    /**
     * Validates an account number and provides helpful error message.
     *
     * @param accountNumber the account number to validate
     * @throws IllegalArgumentException if account number is invalid
     */
    public static void validateAccountNumber(String accountNumber) throws IllegalArgumentException {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Error: Account number cannot be empty.\n" +
                            "Please provide a valid account number."
            );
        }

        if (!ACCOUNT_NUMBER_PATTERN.matcher(accountNumber.trim()).matches()) {
            throw new IllegalArgumentException(
                    "Error: Invalid account number format.\n" +
                            "Account number must be in format: ACC### (e.g., ACC001, ACC123)\n" +
                            "Provided: " + accountNumber
            );
        }
    }

    /**
     * Validates a customer ID.
     *
     * @param customerId the customer ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            return false;
        }
        return CUSTOMER_ID_PATTERN.matcher(customerId.trim()).matches();
    }

    /**
     * Validates a customer ID and provides helpful error message.
     *
     * @param customerId the customer ID to validate
     * @throws IllegalArgumentException if customer ID is invalid
     */
    public static void validateCustomerId(String customerId) throws IllegalArgumentException {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Error: Customer ID cannot be empty.\n" +
                            "Please provide a valid customer ID."
            );
        }

        if (!CUSTOMER_ID_PATTERN.matcher(customerId.trim()).matches()) {
            throw new IllegalArgumentException(
                    "Error: Invalid customer ID format.\n" +
                            "Customer ID must be in format: CUS# (e.g., CUS0, CUS123)\n" +
                            "Provided: " + customerId
            );
        }
    }

    /**
     * Validates a phone number.
     *
     * @param phone the phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_NUMBER_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates a phone number and provides helpful error message.
     *
     * @param phone the phone number to validate
     * @throws IllegalArgumentException if phone number is invalid
     */
    public static void validatePhoneNumber(String phone) throws IllegalArgumentException {

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Error: Phone number cannot be empty.\n" +
                            "Please provide a valid Ghana phone number."
            );
        }

        // Normalize input (remove spaces, hyphens, dots)
        String normalizedPhone = phone.replaceAll("[\\s.-]", "");

        // Ghana phone number pattern
        String PHONE_REGEX =  "^[+]?\\d{10,15}$";

        if (!normalizedPhone.matches(PHONE_REGEX)) {
            throw new IllegalArgumentException(
                    "Error: Invalid phone number format.\n" +
                            "Accepted formats:\n" +
                            "  - 0552221777\n" +
                            "  - 055 222 1777\n" +
                            "  - +233552221777\n" +
                            "Provided: " + phone
            );
        }
    }

    /**
     * Validates a transaction ID.
     *
     * @param transactionId the transaction ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidTransactionId(String transactionId) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            return false;
        }
        return TRANSACTION_ID_PATTERN.matcher(transactionId.trim()).matches();
    }

    /**
     * Validates a transaction ID and provides helpful error message.
     *
     * @param transactionId the transaction ID to validate
     * @throws IllegalArgumentException if transaction ID is invalid
     */
    public static void validateTransactionId(String transactionId) throws IllegalArgumentException {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Error: Transaction ID cannot be empty.\n" +
                            "Please provide a valid transaction ID."
            );
        }

        if (!TRANSACTION_ID_PATTERN.matcher(transactionId.trim()).matches()) {
            throw new IllegalArgumentException(
                    "Error: Invalid transaction ID format.\n" +
                            "Transaction ID must be in format: TXN# (e.g., TXN123456789)\n" +
                            "Provided: " + transactionId
            );
        }
    }

    /**
     * Validates an amount for transactions.
     *
     * @param amount the amount to validate
     * @param operationType the type of operation (Deposit, Withdrawal, etc.)
     * @throws IllegalArgumentException if amount is invalid
     */
    public static void validateAmount(double amount, String operationType) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Error: Invalid " + operationType.toLowerCase() + " amount.\n" +
                            operationType + " amount must be greater than zero.\n" +
                            "Provided: $" + String.format("%.2f", amount)
            );
        }

        if (amount > 1000000) {
            throw new IllegalArgumentException(
                    "Error: " + operationType + " amount exceeds maximum limit.\n" +
                            "Maximum allowed: $1,000,000.00\n" +
                            "Provided: $" + String.format("%.2f", amount)
            );
        }
    }

    /**
     * Validates a name (for customer or account holder).
     *
     * @param name the name to validate
     * @param fieldName the field name for error message (e.g., "Customer name", "Account holder")
     * @throws IllegalArgumentException if name is invalid
     */
    public static void validateName(String name, String fieldName) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Error: " + fieldName + " cannot be empty.\n" +
                            "Please provide a valid name."
            );
        }

        if (name.trim().length() < 2) {
            throw new IllegalArgumentException(
                    "Error: " + fieldName + " is too short.\n" +
                            "Name must be at least 2 characters long.\n" +
                            "Provided: " + name
            );
        }

        if (name.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Error: " + fieldName + " is too long.\n" +
                            "Name must be less than 100 characters.\n" +
                            "Provided length: " + name.trim().length()
            );
        }
    }

    /**
     * Validates an age.
     *
     * @param age the age to validate
     * @throws IllegalArgumentException if age is invalid
     */
    public static void validateAge(int age) throws IllegalArgumentException {
        if (age < 18) {
            throw new IllegalArgumentException(
                    "Error: Customer must be at least 18 years old.\n" +
                            "Provided age: " + age
            );
        }

        if (age > 150) {
            throw new IllegalArgumentException(
                    "Error: Invalid age provided.\n" +
                            "Age must be less than 150.\n" +
                            "Provided age: " + age
            );
        }
    }

    /**
     * Validates an address.
     *
     * @param address the address to validate
     * @throws IllegalArgumentException if address is invalid
     */
    public static void validateAddress(String address) throws IllegalArgumentException {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Error: Address cannot be empty.\n" +
                            "Please provide a valid address."
            );
        }
    }
}
