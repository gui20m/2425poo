package spotifum.statistics;

import java.io.Serializable;
import java.util.*;

import spotifum.users.*;
import spotifum.musics.*;
import spotifum.utils.ConsoleColors;

import static spotifum.utils.DisplayInformation.displayUserLeaderBoard;

/**
 * The type User leader board.
 */
public class UserLeaderBoard implements Serializable {
    private final Map<User, Map<Music, Double>> leaderboard;

    /**
     * Instantiates a new User leader board.
     */
    public UserLeaderBoard() {
        this.leaderboard = new HashMap<>();
    }

    /**
     * Remove user.
     *
     * @param user the user
     */
    public void removeUser(User user) {
        if (user == null || user.getEmail() == null) return;
        leaderboard.keySet().removeIf(u ->
                u != null &&
                        user.getEmail().equals(u.getEmail())
        );
    }

    /**
     * Get user history map.
     *
     * @param user the user
     * @return the map
     */
    public Map<Music, Double> getUserHistory(User user){
        return leaderboard.get(user);
    }

    /**
     * Record play.
     *
     * @param user  the user
     * @param music the music
     */
    public void recordPlay(User user, Music music) {
        if (user == null || music == null) {
            return; // Ou lançar IllegalArgumentException dependendo dos requisitos
        }

        Map<Music, Double> userMusicPoints = leaderboard.computeIfAbsent(user, k -> new HashMap<>());

        double currentPoints = 0.0;

        for (Music m : userMusicPoints.keySet()) {
            if (m.equals(music)) {
                currentPoints = userMusicPoints.get(m);
                break;
            }
        }

        double pointsToAdd = 0;

        if (user.getClass().equals(FreeUser.class)) {
            pointsToAdd = currentPoints == 0 ? 5 : currentPoints * 0.025;
        } else if (user.getClass().equals(PremiumBaseUser.class)) {
            pointsToAdd = currentPoints == 0 ? 10 : currentPoints * 0.025;
        } else if (user.getClass().equals(PremiumTopUser.class)) {
            pointsToAdd = currentPoints == 0 ? 100 : currentPoints * 0.025;
        }

        boolean alreadyExists = false;
        for (Music m : userMusicPoints.keySet()) {
            if (m.equals(music)) {
                userMusicPoints.put(m, currentPoints + pointsToAdd);
                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists) {
            userMusicPoints.put(music, pointsToAdd);
        }
    }

    /**
     * Display top user.
     */
    public void displayTopUser() {
        displayUserLeaderBoard(this.leaderboard);
    }

    public int getLeaderboardSize() {
        return leaderboard.size();
    }
}