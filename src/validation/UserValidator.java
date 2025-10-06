package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PHONE_REGEX = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    public boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean isPhoneNumberValid(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        Matcher matcher = PHONE_PATTERN.matcher(phone.trim());
        return matcher.matches();
    }

    public boolean isEmailValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email.trim());
        return matcher.matches();
    }

    public boolean isUsernameValid(String username) {
        return username != null && username.trim().length() > 6;
    }

    public boolean hasValidLength(String password) {
        return password != null && password.length() >= 8;
    }

    public boolean hasCaseVariety(String password) {
        return password != null && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*");
    }

    public boolean hasNumberAndSpecialChar(String password) {
        return password != null && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
}
