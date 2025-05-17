package spotifum.users;

import org.junit.jupiter.api.Test;
import spotifum.playlists.Playlist;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import spotifum.musics.Music;

/**
 * The type Free user test.
 */
public class FreeUserTest {

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
        FreeUser user = new FreeUser();

        assertNotNull(user.getUid());
        assertEquals("", user.getEmail());
        assertEquals("", user.getUsername());
        assertEquals(SubscriptionPlans.Free, user.getUserPlan());
        assertEquals("", user.getAddress());
    }

    /**
     * Test email username constructor.
     */
    @Test
    void testEmailUsernameConstructor() {
        FreeUser user = new FreeUser("test@spotifum.com", "freeuser");

        assertEquals("test@spotifum.com", user.getEmail());
        assertEquals("freeuser", user.getUsername());
        assertEquals(SubscriptionPlans.Free, user.getUserPlan());
        assertEquals("124st av. mars", user.getAddress());
    }

    /**
     * Test copy constructor.
     */
    @Test
    void testCopyConstructor() {
        ConcreteUser original = new ConcreteUser("original@user.com", "original");
        original.setAddress("New Address");
        original.setUserPlan(SubscriptionPlans.PremiumTop);

        FreeUser freeCopy = new FreeUser(original);

        assertEquals(original.getUid(), freeCopy.getUid());
        assertEquals("original@user.com", freeCopy.getEmail());
        assertEquals("original", freeCopy.getUsername());
        assertEquals("New Address", freeCopy.getAddress());
        assertEquals(SubscriptionPlans.Free, freeCopy.getUserPlan()); // Deve forçar plano Free
    }

    /**
     * Test clone.
     */
    @Test
    void testClone() {
        FreeUser original = new FreeUser("clone@test.com", "toclone");
        original.setAddress("Clone Street");

        FreeUser cloned = original.clone();

        assertNotSame(original, cloned);
        assertEquals(original.getUid(), cloned.getUid());
        assertEquals(original.getEmail(), cloned.getEmail());
        assertEquals(original.getUsername(), cloned.getUsername());
        assertEquals(original.getAddress(), cloned.getAddress());
        assertEquals(SubscriptionPlans.Free, cloned.getUserPlan());
    }

    /**
     * Test playlist management.
     */
    @Test
    void testPlaylistManagement() {
        FreeUser user = new FreeUser("playlist@test.com", "playlistuser");
        Playlist playlist = new Playlist("My Playlist", "playlistuser") {
            @Override public Music nextTrack() { return null; }
            @Override public Music previousTrack() { return null; }
            @Override public boolean canSkip() { return true; }
            @Override public Playlist clone() { return null; }
        };

        user.createPlaylist(playlist);
        assertEquals(1, user.getPlaylists().size());
        assertEquals("My Playlist", user.getPlaylists().get(playlist.getId()).getName());
    }
}