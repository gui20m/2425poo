package spotifum.statistics;

import java.io.Serializable;
import java.util.*;

import spotifum.musics.*;
import spotifum.utils.ConsoleColors;

import static spotifum.utils.DisplayInformation.displayMostPlayedGenres;

/**
 * The type Most played genres.
 */
public class MostPlayedGenres implements Serializable {
    private final Map<String, Integer> playedgenres;

    /**
     * Instantiates a new Most played genres.
     */
    public MostPlayedGenres() {
        playedgenres = new HashMap<>();
    }

    /**
     * Record play.
     *
     * @param music the music
     */
    public void recordPlay(Music music) {
        playedgenres.put(music.getGenre(), playedgenres.getOrDefault(music.getGenre(), 0) + 1);
    }

    /**
     * Display most played genres.
     */
    public String getMostPlayedGenres() {
        return displayMostPlayedGenres(this.playedgenres);
    }
}
