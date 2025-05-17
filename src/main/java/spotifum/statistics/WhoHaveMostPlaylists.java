package spotifum.statistics;

import spotifum.playlists.Playlist;
import spotifum.users.User;
import spotifum.utils.ConsoleColors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spotifum.utils.DisplayInformation.displayWhoHaveMostPlaylists;

/**
 * The type Who have most playlists.
 */
public class WhoHaveMostPlaylists implements Serializable {
    private Map<User, List<Playlist>> playlists;

    /**
     * Instantiates a new Who have most playlists.
     */
    public WhoHaveMostPlaylists() {
        this.playlists = new HashMap<>();
    }

    /**
     * Sets playlists.
     *
     * @param playlists the playlists
     */
    public void setPlaylists(Map<User, List<Playlist>> playlists) {
        this.playlists = playlists;
    }

    /**
     * Get who have most playlists.
     */
    public User getWhoHaveMostPlaylists() {
        return displayWhoHaveMostPlaylists(this.playlists);
    }
}
