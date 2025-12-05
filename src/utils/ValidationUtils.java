package utils;

/**
 * A utility class for common validation logic.
 */
public class ValidationUtils {

    /**
     * Validates a phone number.
     * For this application, we define a valid phone number as a string containing only digits.
     * @param phoneNumber The phone number to validate.
     * @return true if the phone number is valid, false otherwise.
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        // This regex checks if the string consists of one or more digits.
        return phoneNumber.matches("\\d+");
    }

    /**
     * Validates an email address format.
     * This is a simple check to ensure the email contains an "@" symbol and is not at the beginning or end.
     * @param email The email to validate.
     * @return true if the email format is considered valid, false otherwise.
     */
    public static boolean isEmailValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // A basic check for the presence of "@" not at the start or end.
        int atIndex = email.indexOf('@');
        return atIndex > 0 && atIndex < email.length() - 1;
    }

    /**
     * Checks if a string is a valid positive integer (for Stock, Quantity).
     *
     * @param str The string to check.
     * @return true if the string represents a positive integer, false otherwise.
     */
    public static boolean isPositiveInteger(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        try {
            int value = Integer.parseInt(str.trim());
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string is a valid positive double (for Prices).
     *
     * @param str The string to check.
     * @return true if the string represents a positive double, false otherwise.
     */
    public static boolean isPositiveDouble(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        try {
            double value = Double.parseDouble(str.trim());
            return value >= 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
