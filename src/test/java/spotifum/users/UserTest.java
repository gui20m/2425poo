package spotifum.users;

import org.junit.jupiter.api.Test;
import spotifum.playlists.Playlist;
import spotifum.musics.Music;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type User test.
 */
public class UserTest {

    private static class TestUser extends User {
        /**
         * Instantiates a new Test user.
         *
         * @param email    the email
         * @param username the username
         */
        public TestUser(String email, String username) {
            super(email, username);
        }

        /**
         * Instantiates a new Test user.
         *
         * @param other the other
         */
        public TestUser(User other) {
            super(other);
        }

        @Override
        public User clone() {
            return new TestUser(this);
        }
    }

    private static class DummyPlaylist extends Playlist {
        /**
         * Instantiates a new Dummy playlist.
         *
         * @param name  the name
         * @param owner the owner
         */
        public DummyPlaylist(String name, String owner) {
            super(name, owner);
        }

        /**
         * Instantiates a new Dummy playlist.
         *
         * @param id    the id
         * @param name  the name
         * @param owner the owner
         */
        public DummyPlaylist(UUID id, String name, String owner) {
            super(name, owner);
            try {
                var field = Playlist.class.getDeclaredField("id");
                field.setAccessible(true);
                field.set(this, id);
            } catch (Exception ignored) {}
        }

        @Override
        public Music nextTrack() { return null; }

        @Override
        public Music previousTrack() { return null; }

        @Override
        public boolean canSkip() { return true; }

        @Override
        public Playlist clone() {
            return new DummyPlaylist(this.getId(), this.getName(), this.getOwner());
        }
    }

    /**
     * Test constructor and defaults.
     */
    @Test
    public void testConstructorAndDefaults() {
        User user = new TestUser("test@example.com", "testuser");

        assertNotNull(user.getUid());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testuser", user.getUsername());
        assertEquals("124st av. mars", user.getAddress());
        assertEquals(SubscriptionPlans.Free, user.getUserPlan());
        assertNotNull(user.getPlaylists());
        assertEquals(0, user.getPlaylists().size());
    }

    /**
     * Test setters and getters.
     */
    @Test
    public void testSettersAndGetters() {
        User user = new TestUser("x@y.com", "x");
        user.setEmail("z@y.com");
        user.setUsername("z");
        user.setAddress("Nova Rua");
        user.setUserPlan(SubscriptionPlans.Free);

        assertEquals("z@y.com", user.getEmail());
        assertEquals("z", user.getUsername());
        assertEquals("Nova Rua", user.getAddress());
        assertEquals(SubscriptionPlans.Free, user.getUserPlan());
    }

    /**
     * Test create playlist and update.
     */
    @Test
    public void testCreatePlaylistAndUpdate() {
        User user = new TestUser("user@x.com", "ux");
        DummyPlaylist p = new DummyPlaylist("mylist", "ux");
        user.createPlaylist(p);

        UUID pid = p.getId();
        Playlist retrieved = user.getPlaylists().get(pid);
        assertNotNull(retrieved);
        assertEquals("mylist", retrieved.getName());

        DummyPlaylist updated = new DummyPlaylist(pid, "updatedName", "ux");
        user.updatePlaylist(updated);

        assertEquals("updatedName", user.getPlaylists().get(pid).getName());
    }

    /**
     * Test clone.
     */
    @Test
    public void testClone() {
        User user = new TestUser("abc@xyz.com", "abc");
        user.setAddress("Rua A");
        user.setUserPlan(SubscriptionPlans.PremiumTop);
        DummyPlaylist p = new DummyPlaylist("rock", "abc");
        user.createPlaylist(p);

        User cloned = user.clone();

        assertNotSame(user, cloned);
        assertEquals(user.getUid(), cloned.getUid());
        assertEquals(user.getEmail(), cloned.getEmail());
        assertEquals(user.getUsername(), cloned.getUsername());
        assertEquals(user.getAddress(), cloned.getAddress());
        assertEquals(user.getUserPlan(), cloned.getUserPlan());
        assertEquals(user.getPlaylists().size(), cloned.getPlaylists().size());

        UUID playlistId = p.getId();
        Playlist clonedPlaylist = cloned.getPlaylists().get(playlistId);
        assertNotNull(clonedPlaylist);
        assertEquals("rock", clonedPlaylist.getName());
    }

    /**
     * Test playlist map encapsulation.
     */
    @Test
    public void testPlaylistMapEncapsulation() {
        User user = new TestUser("xx@xx.com", "x");
        DummyPlaylist p = new DummyPlaylist("pop", "x");
        user.createPlaylist(p);

        Map<UUID, Playlist> copy = user.getPlaylists();
        copy.clear();

        assertEquals(1, user.getPlaylists().size());
        assertNotNull(user.getPlaylists().get(p.getId()));
    }
}