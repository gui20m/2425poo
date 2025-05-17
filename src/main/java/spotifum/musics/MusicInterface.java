package spotifum.musics;

import java.util.*;

/**
 * Interface that defines the basic operations and properties of a Music object.
 */
public interface MusicInterface {

    /**
     * Gets the name of the music.
     * @return the music name
     */
    String getName();

    /**
     * Gets the artist name.
     * @return the artist name
     */

    String getArtist();

    /**
     * Gets the record label.
     * @return the label name
     */

    String getLabel();

    /**
     * Gets the lyrics text.
     * @return the lyrics
     */
    String getLyrics();

    /**
     * Gets the music data.
     * @return list of music data
     */
    List<String> getMusic();

    /**
     * Gets the music genre.
     * @return the genre
     */
    String getGenre();

    /**
     * Gets the duration in seconds.
     * @return duration in seconds
     */
    int getDuration();

    /**
     * Gets the unique music identifier.
     * @return the music UUID
     */
    UUID getMid();

    /**
     * Sets the music name.
     * @param name the new name
     */
    void setName(String name);

    /**
     * Sets the artist name.
     * @param artist the new artist name
     */
    void setArtist(String artist);

    /**
     * Sets the record label.
     * @param label the new label
     */
    void setLabel(String label);

    /**
     * Sets the lyrics text.
     * @param lyrics the new lyrics
     */
    void setLyrics(String lyrics);

    /**
     * Sets the music data.
     * @param music list of music data
     */
    void setMusic(List<String> music);

    /**
     * Sets the music genre.
     * @param genre the new genre
     */
    void setGenre(String genre);

    /**
     * Sets the duration in seconds.
     * @param duration duration in seconds
     */
    void setDuration(int duration);

    /**
     * Creates and returns a copy of this music.
     * @return a clone of this music
     */
    Music clone();

    /**
     * Compares this music to another object for equality.
     * @param o the object to compare with
     * @return true if equal, false otherwise
     */
    boolean equals(Object o);

    /**
     * Returns a string representation of the music.
     * @return the string representation
     */
    String toString();

}
