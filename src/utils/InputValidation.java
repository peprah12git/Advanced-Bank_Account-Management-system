package utils;

import java.util.regex.Pattern;

public class InputValidation {
    // Account number pattern (ACC followed by 3 digits)
    // Matches: ACC001, ACC123, ACC999
    private static final Pattern ACCOUNT_NUMBER_PATTERN =
            Pattern.compile("^ACC\\d{3}$");

    // Email validation pattern
    // Matches: user@example.com, john.doe@company.co.uk, etc.
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_-]+@[A-Za-z0-9.-]+ $.");

    // Customer ID pattern (CUS followed by digits)
    // Matches: CUS0, CUS1, CUS123
    private static final Pattern CUSTOMER_ID_PATTERN = Pattern.compile(
            "^CUS\\d+$"
    );
    public static boolean isValidAccountNumber(String input) {
        return ACCOUNT_NUMBER_PATTERN.matcher(input).matches();
    }
    public static boolean IsEmail(){
        return EMAIL_PATTERN.matcher();
    }
}
