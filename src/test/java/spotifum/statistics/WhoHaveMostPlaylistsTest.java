package spotifum.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotifum.playlists.Playlist;
import spotifum.playlists.PremiumPlaylist;
import spotifum.playlists.RandomPlaylist;
import spotifum.users.User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WhoHaveMostPlaylistsTest {

    private WhoHaveMostPlaylists stats;
    private Map<User, List<Playlist>> userPlaylists;
    private ConcreteTestUser user1;
    private ConcreteTestUser user2;

    private static class ConcreteTestUser extends User {
        public ConcreteTestUser(String email, String username) {
            super(email, username);
        }

        @Override
        public User clone() {
            return new ConcreteTestUser(this.getEmail(), this.getUsername());
        }
    }

    @BeforeEach
    void setUp() {
        stats = new WhoHaveMostPlaylists();
        userPlaylists = new HashMap<>();

        user1 = new ConcreteTestUser("user1@example.com", "User One");
        user2 = new ConcreteTestUser("user2@example.com", "User Two");

        // Criar playlists de teste - agora usando um mapa vazio para RandomPlaylist
        Playlist p1 = new PremiumPlaylist("Playlist 1", user1.getUsername());
        Playlist p2 = new PremiumPlaylist("Playlist 2", user1.getUsername());
        Playlist p3 = new RandomPlaylist(new HashMap<>()); // RandomPlaylist com mapa vazio

        userPlaylists.put(user1, Arrays.asList(p1, p2));
        userPlaylists.put(user2, Collections.singletonList(p3));
    }

    @Test
    void testInitialization() {
        assertNotNull(stats);
        assertNull(stats.getWhoHaveMostPlaylists());
    }

    @Test
    void testNullPlaylists() {
        stats.setPlaylists(new HashMap<>());
        User result = stats.getWhoHaveMostPlaylists();
        assertNull(result);
    }

    @Test
    void testSingleUserWithMultiplePlaylists() {
        Map<User, List<Playlist>> testData = new HashMap<>();
        testData.put(user1, Arrays.asList(
                new PremiumPlaylist("P1", user1.getEmail()),
                new PremiumPlaylist("P2", user1.getEmail()),
                new RandomPlaylist(new HashMap<>())
        ));

        stats.setPlaylists(testData);
        User result = stats.getWhoHaveMostPlaylists();
        assertEquals(3, testData.get(result).size());
    }

    @Test
    void testMultipleUsersWithEqualPlaylists() {
        Map<User, List<Playlist>> testData = new HashMap<>();
        testData.put(user1, Arrays.asList(
                new PremiumPlaylist("P1", user1.getEmail()),
                new RandomPlaylist(new HashMap<>())
        ));
        testData.put(user2, Arrays.asList(
                new PremiumPlaylist("P2", user2.getEmail()),
                new RandomPlaylist(new HashMap<>())
        ));

        stats.setPlaylists(testData);
        User result = stats.getWhoHaveMostPlaylists();
        assertEquals(2, testData.get(result).size());
    }

}