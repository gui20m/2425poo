package spotifum.musics.types;

import spotifum.musics.Music;

import java.io.Serializable;

/**
 * The type Explicit music.
 */
public class ExplicitMusic extends Music implements Serializable, ExplicitMusicInterface {
    /**
     * Instantiates a new Explicit music.
     *
     * @param music the music
     */
    public ExplicitMusic(Music music) {
        super(music);
    }

    /**
     * Compares this explicit music to another object for equality.
     * Two explicit musics are equal if they have the same explicit flag value.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    public boolean equals(Object o) {
        if  (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExplicitMusic that = (ExplicitMusic) o;
        return that.equals(this);
    }
}
