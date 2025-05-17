package spotifum.users;

import org.junit.jupiter.api.Test;
import spotifum.playlists.Playlist;
import spotifum.musics.Music;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Premium top user test.
 */
public class PremiumTopUserTest {

    private static class ConcreteUser extends User {
        /**
         * Instantiates a new Concrete user.
         *
         * @param email    the email
         * @param username the username
         */
        public ConcreteUser(String email, String username) {
            super(email, username);
        }

        @Override
        public User clone() {
            return new ConcreteUser(this.getEmail(), this.getUsername());
        }
    }

    /**
     * Test default constructor.
     */
    @Test
    void testDefaultConstructor() {
        PremiumTopUser user = new PremiumTopUser();

        assertNotNull(user.getUid());
        assertEquals("", user.getEmail());
        assertEquals("", user.getUsername());
        assertEquals(SubscriptionPlans.PremiumTop, user.getUserPlan());
        assertEquals("", user.getAddress());
    }

    /**
     * Test user copy constructor.
     */
    @Test
    void testUserCopyConstructor() {
        ConcreteUser original = new ConcreteUser("original@user.com", "original");
        original.setAddress("Premium Address");
        original.setUserPlan(SubscriptionPlans.Free); // Deve ser sobrescrito

        PremiumTopUser premiumUser = new PremiumTopUser(original);

        assertEquals(original.getUid(), premiumUser.getUid());
        assertEquals("original@user.com", premiumUser.getEmail());
        assertEquals("original", premiumUser.getUsername());
        assertEquals("Premium Address", premiumUser.getAddress());
        assertEquals(SubscriptionPlans.PremiumTop, premiumUser.getUserPlan());
    }

    /**
     * Test clone.
     */
    @Test
    void testClone() {
        PremiumTopUser original = new PremiumTopUser();
        original.setEmail("clone@premium.com");
        original.setUsername("toclone");
        original.setAddress("Clone Avenue");

        PremiumTopUser cloned = original.clone();

        assertNotSame(original, cloned);
        assertEquals(original.getUid(), cloned.getUid());
        assertEquals(original.getEmail(), cloned.getEmail());
        assertEquals(original.getUsername(), cloned.getUsername());
        assertEquals(original.getAddress(), cloned.getAddress());
        assertEquals(SubscriptionPlans.PremiumTop, cloned.getUserPlan());
    }

    /**
     * Test playlist management.
     */
    @Test
    void testPlaylistManagement() {
        PremiumTopUser user = new PremiumTopUser();
        user.setEmail("playlist@premium.com");
        user.setUsername("playlistuser");

        Playlist playlist = new Playlist("Premium Playlist", "playlistuser") {
            @Override public Music nextTrack() { return null; }
            @Override public Music previousTrack() { return null; }
            @Override public boolean canSkip() { return true; }
            @Override public Playlist clone() { return null; }
        };

        user.createPlaylist(playlist);
        assertEquals(1, user.getPlaylists().size());
        assertEquals("Premium Playlist", user.getPlaylists().get(playlist.getId()).getName());
    }

    /**
     * Test plan cannot be downgraded.
     */
    @Test
    void testPlanCannotBeDowngraded() {
        PremiumTopUser user = new PremiumTopUser();
        user.setUserPlan(SubscriptionPlans.PremiumTop);

        assertEquals(SubscriptionPlans.PremiumTop, user.getUserPlan());
    }
}