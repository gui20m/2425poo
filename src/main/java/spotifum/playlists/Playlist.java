package spotifum.playlists;

import spotifum.musics.*;

import java.io.Serializable;
import java.util.*;

/**
 * The type Playlist.
 */
public abstract class Playlist implements PlaylistInterface, Serializable {
    private final UUID id;
    private String owner;
    private String name;
    private final List<Music> tracks;
    private int currentTrackIndex;
    private boolean _public;

    /**
     * Instantiates a new Playlist.
     */
    public Playlist(){
        this.id = UUID.randomUUID();
        this.name = "";
        this.owner = "spotifum";
        this.tracks = new ArrayList<>();
        this.currentTrackIndex = 0;
        this._public = true;
    }

    /**
     * Instantiates a new Playlist.
     *
     * @param name     the name
     * @param username the username
     */
    public Playlist(String name, String username) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.owner = username;
        this.tracks = new ArrayList<>();
        this.currentTrackIndex = 0;
        this._public = true;
    }

    /**
     * Instantiates a new Playlist.
     *
     * @param p the p
     */
    public Playlist(Playlist p) {
        this.id = p.getId();
        this.name = p.getName();
        this.owner = p.getOwner();
        this.tracks = p.getTracks().stream().map(Music::clone).toList();
        this.currentTrackIndex = p.getCurrentTrackIndex();
        this._public = p.isPublic();
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Sets the name of the playlist.
     *
     * @param name the new playlist name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set public.
     *
     * @param state the state
     */
    public void setPublic(boolean state){
        this._public = state;
    }

    /**
     * Sets the current track index.
     *
     * @param currentTrackIndex the new track index
     */
    public void setCurrentTrackIndex(int currentTrackIndex) {
        this.currentTrackIndex = currentTrackIndex;
    }

    /**
     * Gets the name of the playlist.
     *
     * @return the playlist name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get owner string.
     *
     * @return the string
     */
    public String getOwner(){
        return this.owner;
    }

    /**
     * Gets a copy of the tracks list.
     *
     * @return list of music tracks
     */
    public List<Music> getTracks() {
        return new ArrayList<>(this.tracks);
    }

    /**
     * Gets the playlist unique identifier.
     *
     * @return the playlist UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Is public boolean.
     *
     * @return the boolean
     */
    public boolean isPublic() {
        return _public;
    }

    /**
     * Gets the index of the currently selected track in the playlist.
     *
     * @return the current track index (zero-based)
     */
    public int getCurrentTrackIndex() {
        return this.currentTrackIndex;
    }

    /**
     * Adds a music track to the playlist.
     *
     * @param m the music to add
     */
    public void addMusic(Music m){
        this.tracks.add(m);
    }

    /**
     * Add music to index.
     *
     * @param m     the m
     * @param index the index
     */
    public void addMusicToIndex(Music m, int index){
        this.tracks.add(index, m);
    }

    /**
     * Adds all tracks from an album to the playlist.
     *
     * @param album the album to add
     */
    public void addAlbum(Album album){
        for (Music m : album.getMusics()){
            this.tracks.add(m);
        }
    }

    /**
     * Adjusts the current track index to stay within valid bounds.
     */
    private void adjustCurrentIndex() {
        if (tracks.isEmpty()) {
            currentTrackIndex = 0;
        } else {
            currentTrackIndex = Math.min(currentTrackIndex, tracks.size() - 1);
        }
    }

    /**
     * Removes a music track from the playlist.
     *
     * @param music the music to remove
     * @return true if removed, false otherwise
     */
    public boolean removeMusic(Music music) {
        boolean removed = tracks.removeIf(m -> m != null && m.equals(music));
        if (removed) adjustCurrentIndex();
        return removed;
    }

    /**
     * Plays the current track.
     *
     * @return the current music track or null if empty
     */
    public Music playCurrent(){
        if (tracks.isEmpty()) {
            System.out.println("[app] empty playlist");
            return null;
        }
        return this.tracks.get(this.currentTrackIndex);
    }

    /**
     * Checks if playlist contains a specific music track.
     *
     * @param m the music to check
     * @return true if contains, false otherwise
     */
    public boolean contains(Music m) {
        return tracks.contains(m);
    }

    /**
     * Calculates the total duration of all tracks in seconds.
     *
     * @return total duration in seconds
     */
    public int getTotalDuration() {
        return tracks.stream()
                .filter(Objects::nonNull)
                .mapToInt(Music::getDuration)
                .sum();
    }

    /**
     * Compares this playlist to another object for equality.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    public boolean equals(Object obj) {
        if (obj == this) return  true;

        if (obj == null || obj.getClass() != this.getClass()) return false;

        Playlist other = (Playlist)obj;
        return (this.name.equals(other.getName()) &&
                this.tracks.equals(other.tracks)) || this.id.equals(other.getId());
    }

    /**
     * Checks if the playlist is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return this.tracks.isEmpty();
    }

    /**
     * Advances to the next track in the playlist and returns it.
     * The specific behavior depends on the playlist implementation.
     *
     * @return the next music track, or null if no more tracks
     */
    public abstract Music nextTrack();

    /**
     * Goes back to the previous track in the playlist and returns it.
     * The specific behavior depends on the playlist implementation.
     *
     * @return the previous music track, or null if at the beginning
     */
    public abstract Music previousTrack();

    /**
     * Checks if skipping tracks (next/previous) is allowed for this playlist.
     * The specific conditions depend on the playlist implementation.
     *
     * @return true if skipping is allowed, false otherwise
     */
    public abstract boolean canSkip();

    /**
     * Returns a string representation of the playlist.
     *
     * @return formatted string with playlist details
     */
    public abstract Playlist clone();

    /**
     * Returns a formatted string representation of the playlist.
     *
     * @return the formatted string with playlist details
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int boxWidth = 50;
        int contentWidth = boxWidth - 4;

        sb.append("╭────────────────────────────────────────────────╮\n");
        sb.append(String.format("│  \uD83C\uDFB5 %-42s │\n", "Playlist: " + this.name));
        sb.append(String.format("│  \uD83D\uDC64 %-42s │\n", "Owner: " + this.owner));
        sb.append(String.format("│  \uD83D\uDD13 %-42s │\n",
                "Public: " + (this._public ? "yes" : "no")));
        sb.append("├────────────────────────────────────────────────┤\n");

        sb.append(String.format("│  \uD83D\uDCC3 Tracks:%-36s│\n", ""));

        if (!tracks.isEmpty()) {
            tracks.stream()
                    .filter(Objects::nonNull)
                    .forEach(music -> {
                        String line = "• " + music.getName() + " - " + music.getArtist();
                        sb.append(String.format("│  %-46s│\n", line));
                    });
        } else {
            sb.append(String.format("│  %-46s│\n", "No tracks available"));
        }

        sb.append("╰────────────────────────────────────────────────╯");

        return sb.toString();
    }
}