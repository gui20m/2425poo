package spotifum.statistics;

import java.io.Serializable;
import java.util.*;

import spotifum.musics.*;
import spotifum.utils.ConsoleColors;

import static spotifum.utils.DisplayInformation.displayMostPlayedArtist;

/**
 * The type Most played artist.
 */
public class MostPlayedArtist implements Serializable {
    private final Map<Artist, Integer> playedartists;

    /**
     * Instantiates a new Most played artist.
     */
    public MostPlayedArtist() {
        this.playedartists = new HashMap<>();
    }

    /**
     * Record play.
     *
     * @param artist the artist
     */
    public void recordPlay(Artist artist) {
        playedartists.put(artist, playedartists.getOrDefault(artist, 0) + 1);
    }

    /**
     * Gets most played artist.
     */
    public Artist getMostPlayedArtist() {
        return displayMostPlayedArtist(this.playedartists);
    }
}
