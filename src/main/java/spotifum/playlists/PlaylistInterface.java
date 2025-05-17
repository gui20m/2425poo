package spotifum.playlists;

import java.util.*;
import spotifum.musics.*;

/**
 * The interface Playlist interface.
 */
public interface PlaylistInterface {
    /**
     * Gets id.
     *
     * @return the id
     */
    UUID getId();

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Sets name.
     *
     * @param name the name
     */
    void setName(String name);

    /**
     * Sets current track index.
     *
     * @param currentTrackIndex the current track index
     */
    void setCurrentTrackIndex(int currentTrackIndex);

    /**
     * Add music.
     *
     * @param music the music
     */
    void addMusic(Music music);

    /**
     * Add album.
     *
     * @param album the album
     */
    void addAlbum(Album album);

    /**
     * Remove music boolean.
     *
     * @param music the music
     * @return the boolean
     */
    boolean removeMusic(Music music);

    /**
     * Contains boolean.
     *
     * @param music the music
     * @return the boolean
     */
    boolean contains(Music music);

    /**
     * Gets tracks.
     *
     * @return the tracks
     */
    List<Music> getTracks();

    /**
     * Gets total duration.
     *
     * @return the total duration
     */
    int getTotalDuration();

    /**
     * Play current music.
     *
     * @return the music
     */
    Music playCurrent();

    /**
     * Next track music.
     *
     * @return the music
     */
    Music nextTrack();

    /**
     * Previous track music.
     *
     * @return the music
     */
    Music previousTrack();

    /**
     * Can skip boolean.
     *
     * @return the boolean
     */
    boolean canSkip();

    /**
     * Gets current track index.
     *
     * @return the current track index
     */
    int getCurrentTrackIndex();

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    boolean isEmpty();

    /**
     * Compares this playlist to another object for equality.
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    boolean equals(Object obj);

    /**
     * Creates a copy of the playlist.
     * @return a cloned playlist
     */
    Playlist clone();
}