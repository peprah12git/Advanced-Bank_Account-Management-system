package utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    // Regex Patterns
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
    private static final String ACCOUNT_NUMBER_REGEX = "^ACC\\d{3}$";
//    private static final String CUSTOMER_ID_REGEX = "^CUS\\d{3}$";
//    private static final String NAME_REGEX = "^[A-Za-z\\s]+$";
//    private static final String PHONE_REGEX = "^\\d{10}$"; // Simple 10-digit phone validation

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile(ACCOUNT_NUMBER_REGEX);
//    private static final Pattern CUSTOMER_ID_PATTERN = Pattern.compile(CUSTOMER_ID_REGEX);
//    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
//    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);


    public static boolean isValidAccountNumber(String input) {
        return ACCOUNT_NUMBER_PATTERN.matcher(input).matches();
    }
    public static boolean IsEmail(String email){
        return EMAIL_PATTERN.matcher(email).matches();
    }
}