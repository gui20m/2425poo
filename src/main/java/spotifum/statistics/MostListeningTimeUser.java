package spotifum.statistics;

import spotifum.users.*;
import spotifum.utils.ConsoleColors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static spotifum.utils.DisplayInformation.displayMostListeningTimeUser;

/**
 * The type Most listening time user.
 */
public class MostListeningTimeUser implements Serializable {
    private final Map<User, Map<LocalDate, Integer>> history;

    /**
     * Instantiates a new Most listening time user.
     */
    public MostListeningTimeUser() {
        history = new HashMap<>();
    }

    /**
     * Remove user.
     *
     * @param user the user
     */
    public void removeUser(User user) {
        if (user == null || user.getEmail() == null) return;
        history.keySet().removeIf(u ->
                u != null &&
                        user.getEmail().equals(u.getEmail())
        );
    }

    /**
     * Record play.
     *
     * @param user the user
     * @param date the date
     */
    public void recordPlay(User user, LocalDate date) {
        Map<LocalDate, Integer> userHistory = history.computeIfAbsent(user, k -> new HashMap<>());

        userHistory.merge(date, 1, Integer::sum);
    }

    /**
     * Display top user.
     *
     * @param initialDate the initial date
     * @param finalDate   the final date
     */
    public void displayTopUser(LocalDate initialDate, LocalDate finalDate) {
        displayMostListeningTimeUser(initialDate, finalDate, this.history);
    }

    /**
     * Display top user.
     */
    public void displayTopUser() {
        displayMostListeningTimeUser(this.history);
    }
}