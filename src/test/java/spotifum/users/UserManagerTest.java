package spotifum.users;

import org.junit.jupiter.api.Test;
import spotifum.exceptions.EntityAlreadyExistsException;
import spotifum.exceptions.EntityNotFoundException;
import spotifum.playlists.Playlist;
import spotifum.musics.Music;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The type User manager test.
 */
public class UserManagerTest {

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

        @Override
        public User clone() {
            return new TestUser(this.getEmail(), this.getUsername());
        }
    }

    private static class TestPlaylist extends Playlist {
        /**
         * Instantiates a new Test playlist.
         *
         * @param name  the name
         * @param owner the owner
         */
        public TestPlaylist(String name, String owner) {
            super(name, owner);
        }

        @Override public Music nextTrack() { return null; }
        @Override public Music previousTrack() { return null; }
        @Override public boolean canSkip() { return true; }
        @Override public Playlist clone() { return new TestPlaylist(this.getName(), this.getOwner()); }
    }

    /**
     * Test create user.
     */
    @Test
    void testCreateUser() {
        UserManager manager = new UserManager();
        User user = manager.createUser("test@spotifum.com", "testuser");

        assertEquals("test@spotifum.com", user.getEmail());
        assertEquals("testuser", user.getUsername());
    }

    /**
     * Test insert user.
     *
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    @Test
    void testInsertUser() throws EntityAlreadyExistsException {
        UserManager manager = new UserManager();
        User user = new TestUser("new@user.com", "newuser");

        manager.insertUser(user);
        assertEquals(1, manager.getUsers().size());
        assertTrue(manager.existUserWithEmail("new@user.com"));
    }

    /**
     * Test insert duplicate user.
     *
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    @Test
    void testInsertDuplicateUser() throws EntityAlreadyExistsException {
        UserManager manager = new UserManager();
        manager.insertUser(new TestUser("dupe@test.com", "user1"));

        assertThrows(EntityAlreadyExistsException.class, () -> {
            manager.insertUser(new TestUser("dupe@test.com", "user2"));
        });
    }

    /**
     * Test update user.
     *
     * @throws EntityNotFoundException      the entity not found exception
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    @Test
    void testUpdateUser() throws EntityNotFoundException, EntityAlreadyExistsException {
        UserManager manager = new UserManager();
        User original = new TestUser("update@test.com", "original");
        manager.insertUser(original);

        User updated = new TestUser("update@test.com", "updated");
        manager.updateUser(updated);

        assertEquals("updated", manager.getUser("update@test.com").getUsername());
    }

    /**
     * Test update non existent user.
     */
    @Test
    void testUpdateNonExistentUser() {
        UserManager manager = new UserManager();
        assertThrows(EntityNotFoundException.class, () -> {
            manager.updateUser(new TestUser("nonexistent@test.com", "ghost"));
        });
    }

    /**
     * Test remove user.
     *
     * @throws EntityAlreadyExistsException the entity already exists exception
     * @throws EntityNotFoundException      the entity not found exception
     */
    @Test
    void testRemoveUser() throws EntityAlreadyExistsException, EntityNotFoundException {
        UserManager manager = new UserManager();
        manager.insertUser(new TestUser("remove@test.com", "toremove"));

        manager.removeUser("remove@test.com");
        assertEquals(0, manager.getUsers().size());
    }

    /**
     * Test remove non existent user.
     */
    @Test
    void testRemoveNonExistentUser() {
        UserManager manager = new UserManager();
        assertThrows(EntityNotFoundException.class, () -> {
            manager.removeUser("notfound@test.com");
        });
    }

    /**
     * Test get user.
     *
     * @throws EntityAlreadyExistsException the entity already exists exception
     * @throws EntityNotFoundException      the entity not found exception
     */
    @Test
    void testGetUser() throws EntityAlreadyExistsException, EntityNotFoundException {
        UserManager manager = new UserManager();
        User expected = new TestUser("get@test.com", "getuser");
        manager.insertUser(expected);

        User actual = manager.getUser("get@test.com");
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getUsername(), actual.getUsername());
    }

    /**
     * Test get non existent user.
     */
    @Test
    void testGetNonExistentUser() {
        UserManager manager = new UserManager();
        assertThrows(EntityNotFoundException.class, () -> {
            manager.getUser("missing@test.com");
        });
    }

    /**
     * Test update user email.
     *
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    @Test
    void testUpdateUserEmail() throws EntityAlreadyExistsException {
        UserManager manager = new UserManager();
        User user = new TestUser("old@email.com", "user");
        manager.insertUser(user);

        manager.updateUserEmail(user, "new@email.com", "old@email.com");

        assertFalse(manager.existUserWithEmail("old@email.com"));
        assertTrue(manager.existUserWithEmail("new@email.com"));
    }

    /**
     * Test get public playlists.
     *
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    @Test
    void testGetPublicPlaylists() throws EntityAlreadyExistsException {
        UserManager manager = new UserManager();

        // User 1 com playlist pública
        User user1 = new TestUser("user1@test.com", "user1");
        TestPlaylist publicPlaylist = new TestPlaylist("Public", "user1");
        publicPlaylist.setPublic(true);
        user1.createPlaylist(publicPlaylist);
        manager.insertUser(user1);

        // User 2 com playlist privada
        User user2 = new TestUser("user2@test.com", "user2");
        TestPlaylist privatePlaylist = new TestPlaylist("Private", "user2");
        privatePlaylist.setPublic(false);
        user2.createPlaylist(privatePlaylist);
        manager.insertUser(user2);

        Map<UUID, Playlist> publicPlaylists = manager.getPublicPlaylists();
        assertEquals(1, publicPlaylists.size());
        assertEquals("Public", publicPlaylists.values().iterator().next().getName());
    }

    /**
     * Test get users playlists.
     *
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    @Test
    void testGetUsersPlaylists() throws EntityAlreadyExistsException {
        UserManager manager = new UserManager();
        User user = new TestUser("playlists@test.com", "playlistuser");

        user.createPlaylist(new TestPlaylist("List1", "playlistuser"));
        user.createPlaylist(new TestPlaylist("List2", "playlistuser"));
        manager.insertUser(user);

        Map<User, List<Playlist>> usersPlaylists = manager.getUsersPlaylists();
        assertEquals(1, usersPlaylists.size());
        assertEquals(2, usersPlaylists.get(user).size());
    }
}