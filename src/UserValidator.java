import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

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