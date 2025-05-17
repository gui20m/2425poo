package spotifum.users;

import java.io.Serializable;
import spotifum.playlists.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type User.
 */
public abstract class User implements Serializable, UserInterface {
    private final UUID uid;
    private String email;
    private String username;
    private String address;
    private SubscriptionPlans userPlan;
    private Map<UUID, Playlist> playlists;

    /**
     * Instantiates a new User.
     */
    public User() {
        this.uid = UUID.randomUUID();
        this.email="";
        this.username="";
        this.address="";
        this.userPlan=SubscriptionPlans.Free;
        this.playlists=new HashMap<>();
    }

    /**
     * Instantiates a new User.
     *
     * @param email    the email
     * @param username the username
     */
    public User(String email, String username) {
        this.uid = UUID.randomUUID();
        this.email=email;
        this.username=username;
        this.address="124st av. mars";
        this.userPlan=SubscriptionPlans.Free;
        this.playlists=new HashMap<>();
    }

    /**
     * Instantiates a new User.
     *
     * @param user the user
     */
    public User(User user){
        this.uid=user.getUid();
        this.email=user.getEmail();
        this.address=user.getAddress();
        this.username=user.getUsername();
        this.userPlan=user.getUserPlan();
        this.playlists=user.getPlaylists();
    }

    /**
     * Sets the user's email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets playlists.
     *
     * @param playlists the playlists
     */
    public void setPlaylists(Map<UUID, Playlist> playlists) {
        this.playlists = playlists;
    }

    /**
     * Sets the user's username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the user's address.
     *
     * @param adress the address to set
     */
    public void setAddress(String adress) {
        this.address = adress;
    }

    /**
     * Sets the user's subscription plan.
     *
     * @param userPlan the subscription plan to set
     */
    public void setUserPlan(SubscriptionPlans userPlan) {
        this.userPlan = userPlan;
    }

    /**
     * Gets the user's unique identifier.
     *
     * @return the UID
     */
    public UUID getUid() {
        return uid;
    }

    /**
     * Gets the user's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Create playlist.
     *
     * @param playlist the playlist
     */
    public void createPlaylist(Playlist playlist){
        this.playlists.put(playlist.getId(), playlist);
    }

    /**
     * Update playlist.
     *
     * @param playlist the playlist
     */
    public void updatePlaylist(Playlist playlist){
        if (this.playlists.containsKey(playlist.getId())) {
            this.playlists.put(playlist.getId(), playlist);
        }
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public Map<UUID, Playlist> getPlaylists() {
        return playlists.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Gets the user's address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the user's subscription plan.
     *
     * @return the subscription plan
     */
    public SubscriptionPlans getUserPlan(){
        return this.userPlan;
    }

    /**
     * Gets the user's username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns a formatted string representing the user's information.
     *
     * @return a string representation of the user
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int boxWidth = 50;
        int contentWidth = boxWidth - 4;

        // Header
        sb.append("╭────────────────────────────────────────────────╮\n");
        sb.append(String.format("│  \uD83D\uDC64 %-43s│\n", "User: " + this.username));
        sb.append(String.format("│  ✉️ %-43s│\n", "Email: " + this.email));
        sb.append(String.format("│  \uD83C\uDFE0 %-43s│\n", "Address: " + this.address));
        sb.append(String.format("│  \uD83D\uDCB3 %-43s│\n", "Plan: " + this.userPlan));
        sb.append("├────────────────────────────────────────────────┤\n");

        String playlistsStr = this.playlists.values().stream()
                .map(Playlist::getName)
                .collect(Collectors.joining(", "));

        if (!playlists.isEmpty()) {
            sb.append(String.format("│  \uD83C\uDFB5 Playlists:%-33s│\n", ""));
            playlists.values().stream()
                    .map(Playlist::getName)
                    .forEach(playlist -> sb.append(String.format("│  • %-44s│\n", playlist)));
        } else {
            sb.append(String.format("│  \uD83C\uDFB5 %-43s│\n", "No playlists"));
        }

        sb.append("╰────────────────────────────────────────────────╯");

        return sb.toString();
    }

    /**
     * Creates and returns a copy of this user.
     *
     * @return a cloned User object
     */
    public abstract User clone();
}
