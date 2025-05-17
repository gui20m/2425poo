package spotifum.utils;

import java.util.regex.Pattern;

/**
 * The type Valid email.
 */
public class ValidEmail {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * Is valid email boolean.
     *
     * @param emailString the email string
     * @return the boolean
     */
    public static boolean isValidEmail(String emailString) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(emailString).matches();
    }
}