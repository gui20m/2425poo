package spotifum.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotifum.musics.Music;
import spotifum.users.FreeUser;
import spotifum.users.PremiumBaseUser;
import spotifum.users.PremiumTopUser;
import spotifum.users.User;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserLeaderBoardTest {

    private UserLeaderBoard leaderBoard;
    private FreeUser freeUser;
    private PremiumBaseUser premiumBaseUser;
    private PremiumTopUser premiumTopUser;
    private Music song1;
    private Music song2;

    @BeforeEach
    void setUp() {
        leaderBoard = new UserLeaderBoard();
        freeUser = new FreeUser("free@user.com", "Free User");
        premiumBaseUser = new PremiumBaseUser("base@user.com", "Base User");
        premiumTopUser = new PremiumTopUser("top@user.com", "Top User");
        song1 = new Music();
        song2 = new Music();
    }

    @Test
    void testInitialization() {
        assertNotNull(leaderBoard);
        assertDoesNotThrow(() -> leaderBoard.displayTopUser());
    }

    @Test
    void testRecordPlayForFreeUser() {
        leaderBoard.recordPlay(freeUser, song1);
        leaderBoard.recordPlay(freeUser, song1); // Second play

        Map<Music, Double> history = leaderBoard.getUserHistory(freeUser);
        assertNotNull(history);
        assertEquals(1, history.size());

        double expectedPoints = 5 + (5 * 0.025); // First play + second play
        assertEquals(expectedPoints, history.get(song1), 0.001);
    }

    @Test
    void testRecordPlayForPremiumBaseUser() {
        leaderBoard.recordPlay(premiumBaseUser, song1);
        leaderBoard.recordPlay(premiumBaseUser, song1); // Second play

        Map<Music, Double> history = leaderBoard.getUserHistory(premiumBaseUser);
        assertNotNull(history);
        assertEquals(1, history.size());

        double expectedPoints = 10 + (10 * 0.025); // First play + second play
        assertEquals(expectedPoints, history.get(song1), 0.001);
    }

    @Test
    void testRecordPlayForPremiumTopUser() {
        leaderBoard.recordPlay(premiumTopUser, song1);
        leaderBoard.recordPlay(premiumTopUser, song1); // Second play

        Map<Music, Double> history = leaderBoard.getUserHistory(premiumTopUser);
        assertNotNull(history);
        assertEquals(1, history.size());

        double expectedPoints = 100 + (100 * 0.025); // First play + second play
        assertEquals(expectedPoints, history.get(song1), 0.001);
    }

    @Test
    void testMultipleSongsRecording() {
        Music song1 = new Music();
        Music song2 = new Music();
        // Forçar UUIDs diferentes
        song2.setName("Different"); // Modifica qualquer campo para tornar as músicas diferentes

        leaderBoard.recordPlay(freeUser, song1);
        leaderBoard.recordPlay(freeUser, song2);

        Map<Music, Double> history = leaderBoard.getUserHistory(freeUser);
        assertEquals(2, history.size(), "Deveria ter 2 músicas no histórico");
    }

    @Test
    void testRemoveUser() {
        leaderBoard.recordPlay(freeUser, song1);
        leaderBoard.recordPlay(premiumBaseUser, song1);

        leaderBoard.removeUser(freeUser);

        assertNull(leaderBoard.getUserHistory(freeUser));
        assertNotNull(leaderBoard.getUserHistory(premiumBaseUser));
    }

    @Test
    void testRemoveNullUser() {
        leaderBoard.recordPlay(freeUser, song1);

        leaderBoard.removeUser(null);

        assertNotNull(leaderBoard.getUserHistory(freeUser));
    }

    @Test
    void testRemoveUserWithNullEmail() {
        FreeUser invalidUser = new FreeUser(null, "Invalid User");
        leaderBoard.recordPlay(freeUser, song1);

        leaderBoard.removeUser(invalidUser);

        assertNotNull(leaderBoard.getUserHistory(freeUser));
    }

    @Test
    void testUserEqualityBasedOnEmail() {
        FreeUser freeUserCopy = new FreeUser("free@user.com", "Different Name");
        leaderBoard.recordPlay(freeUser, song1);

        leaderBoard.removeUser(freeUserCopy);

        assertNull(leaderBoard.getUserHistory(freeUser));
    }

    @Test
    void testDisplayTopUser() {
        leaderBoard.recordPlay(freeUser, song1);
        leaderBoard.recordPlay(premiumTopUser, song2);

        assertDoesNotThrow(() -> leaderBoard.displayTopUser());
    }

    @Test
    void testGetUserHistoryForNonExistentUser() {
        assertNull(leaderBoard.getUserHistory(freeUser));
    }

    @Test
    void testRecordPlayWithNullUser() {
        assertDoesNotThrow(() -> leaderBoard.recordPlay(null, song1));
        assertEquals(0, leaderBoard.getLeaderboardSize());
    }

    @Test
    void testRecordPlayWithNullMusic() {
        assertDoesNotThrow(() -> leaderBoard.recordPlay(freeUser, null));
    }
}