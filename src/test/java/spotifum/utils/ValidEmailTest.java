package spotifum.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Valid email test.
 */
public class ValidEmailTest {

    /**
     * Test is valid.
     */
    @Test
    public void testIsValid() {
        assertTrue(ValidEmail.isValidEmail("luis@mail.com"));
        assertTrue(ValidEmail.isValidEmail("david@gmail.com"));
        assertFalse(ValidEmail.isValidEmail("luismail.com"));
        assertFalse(ValidEmail.isValidEmail("david@gmailcom"));
        assertFalse(ValidEmail.isValidEmail("luis@mailcom"));
        assertFalse(ValidEmail.isValidEmail("davidgmail.com"));
        assertFalse(ValidEmail.isValidEmail("luismailcom"));
        assertFalse(ValidEmail.isValidEmail("davidgmailcom"));
    }
}