package spotifum.statistics;

import spotifum.playlists.Playlist;
import spotifum.playlists.RandomPlaylist;
import spotifum.utils.ConsoleColors;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static spotifum.utils.DisplayInformation.displayHowMuchPublicPlaylistsExist;

/**
 * The type How much public playlists exist.
 */
public class HowMuchPublicPlaylistsExist implements Serializable {
    private Map<UUID, Playlist> publicplaylists;
    private List<RandomPlaylist> randomPlaylists;

    public List<RandomPlaylist> getRandomPlaylists() {
        if (this.randomPlaylists == null) return new ArrayList<>();
        return randomPlaylists.stream().filter(Playlist::isPublic).collect(Collectors.toList());
    }

    public Map<UUID, Playlist> getPublicplaylists() {
        if (this.publicplaylists == null) return new HashMap<>();
        return publicplaylists.entrySet().stream()
                .filter(entry -> entry.getValue().isPublic())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    /**
     * Instantiates a new How much public playlists exist.
     */
    public HowMuchPublicPlaylistsExist() {
        this.publicplaylists = new HashMap<>();
    }

    /**
     * Sets random playlists.
     *
     * @param randomPlaylists the random playlists
     */
    public void setRandomPlaylists(List<RandomPlaylist> randomPlaylists) {
        this.randomPlaylists = randomPlaylists;
    }

    /**
     * Sets publicplaylists.
     *
     * @param publicplaylists the publicplaylists
     */
    public void setPublicplaylists(Map<UUID, Playlist> publicplaylists) {
        this.publicplaylists = publicplaylists;
    }

    /**
     * Display public playlists.
     */
    public void displayPublicPlaylists() {
        displayHowMuchPublicPlaylistsExist(this.publicplaylists, this.randomPlaylists);
    }
}
