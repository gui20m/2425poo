package spotifum.users;

import spotifum.exceptions.*;
import spotifum.musics.Music;
import spotifum.playlists.*;
import spotifum.utils.ConsoleColors;

import java.io.Serializable;
import java.util.*;

/**
 * The type User manager.
 */
public class UserManager implements Serializable {
    private Map<String, User> users;

    /**
     * Instantiates a new User manager.
     */
    public UserManager() {
        this.users = new HashMap<>();
    }

    /**
     * Get users map.
     *
     * @return the map
     */
    public Map<String, User> getUsers(){
        return new HashMap<>(this.users);
    }

    /**
     * Create user user.
     *
     * @param email    the email
     * @param username the username
     * @return the user
     */
    public User createUser(String email, String username) {
        return new FreeUser(email, username);
    }

    /**
     * Insert user.
     *
     * @param user the user
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    public void insertUser(User user) throws EntityAlreadyExistsException {
        String email = user.getEmail().toLowerCase();

        if (users.containsKey(email)) {
            throw new EntityAlreadyExistsException("user with email \"" + email + "\" already exists!");
        }

        this.users.put(email, user);
    }

    /**
     * Update user.
     *
     * @param user the user
     * @throws EntityNotFoundException the entity not found exception
     */
    public void updateUser(User user) throws EntityNotFoundException {
        String email = user.getEmail().toLowerCase();
        User u = users.get(email);

        if (u==null) throw new EntityNotFoundException("[app-log] user with email: " + email + " does not exist!");

        this.users.put(email, user);
    }

    /**
     * Remove user.
     *
     * @param email the email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void removeUser(String email) throws EntityNotFoundException {
        if (!this.users.containsKey(email)) {
            throw new EntityNotFoundException();
        }
        this.users.remove(email);
    }

    /**
     * Gets all users.
     */
    public void getAllUsers() {

        final int boxWidth = 100;
        String title = ConsoleColors.CYAN + "👥 ALL REGISTERED USERS" + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-106s │\n", title);
        System.out.println(borderMiddle);

        if (this.users.isEmpty()) {
            System.out.printf("│  %-106s │\n", ConsoleColors.RED + "no users found" + ConsoleColors.RESET);
        } else {
            for (User user : this.users.values()) {
                String email = user.getEmail();
                String username = user.getUsername();
                SubscriptionPlans plan = user.getUserPlan();
                String line = "• " + email + " | " + ConsoleColors.ORANGE + username + ConsoleColors.RESET + " | Plan: " + plan.toString() + " | Address: " + ConsoleColors.ORANGE + user.getAddress() + ConsoleColors.RESET;
                System.out.printf("│  %-125s │\n", line);
            }
        }

        System.out.println(borderBottom);
    }

    /**
     * Update user email.
     *
     * @param user     the user
     * @param newEmail the new email
     * @param oldEmail the old email
     */
    public void updateUserEmail(User user, String newEmail, String oldEmail) {
        if (users.remove(oldEmail)==null);
        this.users.put(newEmail, user);
    }

    /**
     * Exist user with email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean existUserWithEmail(String email) {
        return this.users.containsKey(email.toLowerCase());
    }

    /**
     * Gets user.
     *
     * @param email the email
     * @return the user
     * @throws EntityNotFoundException the entity not found exception
     */
    public User getUser(String email) throws EntityNotFoundException {
        User u = this.users.get(email.toLowerCase());

        if (u == null) {
            throw new EntityNotFoundException(email);
        }

        return u;
    }

    /**
     * Gets public playlists.
     *
     * @return the public playlists
     */
    public Map<UUID, Playlist> getPublicPlaylists() {
        HashMap<UUID, Playlist> playlists = new HashMap<>();
        for (User user: this.users.values()) {
                for (Playlist p : user.getPlaylists().values()) {
                    p.setOwner(user.getUsername());
                    if (p.isPublic()) playlists.put(p.getId(), p);
                }
        }
        return Collections.unmodifiableMap(playlists);
    }

    /**
     * Gets public playlists.
     *
     * @param currentUser  the current user
     * @param playlistName the playlist name
     * @return the public playlists
     */
    public Map<UUID, Playlist> getPublicPlaylists(User currentUser, String playlistName) {
        HashMap<UUID, Playlist> playlists = new HashMap<>();
        for (User user: this.users.values()) {
            if (!user.equals(currentUser)) {
                for (Playlist p : user.getPlaylists().values()) {
                    p.setOwner(user.getUsername());
                    if (p.isPublic() && p.getName().contains(playlistName)) playlists.put(p.getId(), p);
                }
            }
        }
        return Collections.unmodifiableMap(playlists);
    }

    /**
     * Get users playlists map.
     *
     * @return the map
     */
    public Map<User, List<Playlist>> getUsersPlaylists(){
        HashMap<User, List<Playlist>> playlists = new HashMap<>();
        for (User user: this.users.values()) {
            List<Playlist> playlistsList = new ArrayList<>();
            for (Playlist p: user.getPlaylists().values()) {
                playlistsList.add(p);
            }
            playlists.put(user, playlistsList);
        }
        return playlists;
    }

    public void removeTracksFromPlaylists(Map<UUID, Music> tracks) {
        if (tracks == null || tracks.isEmpty()) {
            return;
        }

        for (User user : this.users.values()) {
            Map<UUID, Playlist> playlists = user.getPlaylists();
            for (Playlist p : playlists.values()) {
                List<Music> musics = p.getTracks();
                for (Music music : musics) {
                    if (tracks.containsKey(music.getMid())){
                        p.removeMusic(music);
                    }
                }
            }
        }
    }

    /**
     * Delete music from playlists.
     *
     * @param music the music
     */
    public void deleteMusicFromPlaylists(Music music){
        for (User user: this.users.values()) {
            for (Playlist p: user.getPlaylists().values()) {
                List<Music> musics = p.getTracks();
                for (Music music2 : musics) {
                    if (music2.equals(music)) {
                        p.removeMusic(music2);
                    }
                }
            }
        }
    }
}
