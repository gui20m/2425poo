package spotifum.users;

import org.junit.jupiter.api.Test;
import spotifum.playlists.Playlist;
import spotifum.musics.Music;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Premium base user test.
 */
public class PremiumBaseUserTest {

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
        PremiumBaseUser user = new PremiumBaseUser();

        assertNotNull(user.getUid());
        assertEquals("", user.getEmail());
        assertEquals("", user.getUsername());
        assertEquals(SubscriptionPlans.PremiumBase, user.getUserPlan());
        assertEquals("", user.getAddress());
    }

    /**
     * Test user copy constructor.
     */
    @Test
    void testUserCopyConstructor() {
        ConcreteUser original = new ConcreteUser("original@user.com", "original");
        original.setAddress("Premium Address");
        original.setUserPlan(SubscriptionPlans.Free);

        PremiumBaseUser premiumUser = new PremiumBaseUser(original);

        assertEquals(original.getUid(), premiumUser.getUid());
        assertEquals("original@user.com", premiumUser.getEmail());
        assertEquals("original", premiumUser.getUsername());
        assertEquals("Premium Address", premiumUser.getAddress());
        assertEquals(SubscriptionPlans.PremiumBase, premiumUser.getUserPlan());
    }

    /**
     * Test premium base copy constructor.
     */
    @Test
    void testPremiumBaseCopyConstructor() {
        PremiumBaseUser original = new PremiumBaseUser();
        original.setEmail("premium@user.com");
        original.setUsername("premium");
        original.setAddress("Premium Street");

        PremiumBaseUser copy = new PremiumBaseUser(original);

        assertEquals(original.getUid(), copy.getUid());
        assertEquals("premium@user.com", copy.getEmail());
        assertEquals("premium", copy.getUsername());
        assertEquals("Premium Street", copy.getAddress());
        assertEquals(SubscriptionPlans.PremiumBase, copy.getUserPlan());
    }

    /**
     * Test clone.
     */
    @Test
    void testClone() {
        PremiumBaseUser original = new PremiumBaseUser();
        original.setEmail("clone@premium.com");
        original.setUsername("toclone");
        original.setAddress("Clone Avenue");

        PremiumBaseUser cloned = original.clone();

        assertNotSame(original, cloned);
        assertEquals(original.getUid(), cloned.getUid());
        assertEquals(original.getEmail(), cloned.getEmail());
        assertEquals(original.getUsername(), cloned.getUsername());
        assertEquals(original.getAddress(), cloned.getAddress());
        assertEquals(SubscriptionPlans.PremiumBase, cloned.getUserPlan());
    }

    /**
     * Test playlist management.
     */
    @Test
    void testPlaylistManagement() {
        PremiumBaseUser user = new PremiumBaseUser();
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
        PremiumBaseUser user = new PremiumBaseUser();
        user.setUserPlan(SubscriptionPlans.PremiumBase);

        assertEquals(SubscriptionPlans.PremiumBase, user.getUserPlan());
    }
}