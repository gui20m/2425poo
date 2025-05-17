package spotifum.statistics;

import java.io.Serializable;
import java.util.*;

import spotifum.musics.*;
import spotifum.utils.ConsoleColors;

import static spotifum.utils.DisplayInformation.displayMostPlayedMusic;

/**
 * The type Most played music.
 */
public class MostPlayedMusic implements Serializable {
    private final Map<Music, Integer> playedtracks;

    /**
     * Instantiates a new Most played music.
     */
    public MostPlayedMusic() {
        this.playedtracks = new HashMap<>();
    }

    /**
     * Record play.
     *
     * @param music the music
     */
    public void recordPlay(Music music) {
        boolean found = false;
        for (Map.Entry<Music, Integer> entry : playedtracks.entrySet()) {
            if (entry.getKey().equals(music)) {
                playedtracks.put(entry.getKey(), entry.getValue() + 1);
                found = true;
                break;
            }
        }
        if (!found) {
            playedtracks.put(music, 1);
        }
    }

    /**
     * Gets most played music.
     */
    public Music getMostPlayedMusic() {
        return displayMostPlayedMusic(this.playedtracks);
    }
}
