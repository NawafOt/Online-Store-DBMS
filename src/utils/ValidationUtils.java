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
}
