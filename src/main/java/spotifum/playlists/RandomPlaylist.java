package spotifum.playlists;

import spotifum.musics.Music;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Random playlist.
 */
public class RandomPlaylist extends Playlist{
    /**
     * Instantiates a new Random playlist.
     */
    public RandomPlaylist() {
        super();
    }

    /**
     * Instantiates a new Random playlist.
     *
     * @param playlist the playlist
     */
    public RandomPlaylist(Playlist playlist) {
        super(playlist);
    }

    /**
     * Instantiates a new Random playlist.
     *
     * @param musics the musics
     */
    public RandomPlaylist(Map<UUID, Music> musics) {
        super();
        musics.values().stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list);
                    return list;
                }))
                .stream()
                .limit(10)
                .forEach(this::addMusic);
    }

    /**
     * Attempts to go to the next track (not available for free users).
     * Displays a message prompting to upgrade to premium.
     * @return always returns null for free users
     */
    public Music nextTrack() {
        System.out.println("[app-log] buy premium to use this feature!");
        return null;
    }

    /**
     * Attempts to go to the previous track (not available for free users).
     * Displays a message prompting to upgrade to premium.
     * @return always returns null for free users
     */
    public Music previousTrack() {
        System.out.println("[app-log] buy premium to use this feature!");
        return null;
    }

    /**
     * Checks if skipping tracks is allowed (always false for free playlists).
     * @return false (free playlists don't allow skipping tracks)
     */
    public boolean canSkip(){
        return false;
    }

    /**
     * Creates and returns a copy of this random playlist.
     * @return a cloned random playlist
     */
    public RandomPlaylist clone(){
        return new RandomPlaylist(this);
    }
}
