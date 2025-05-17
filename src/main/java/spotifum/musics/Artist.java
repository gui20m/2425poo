package spotifum.musics;

import spotifum.exceptions.EntityAlreadyExistsException;
import spotifum.exceptions.EntityNotFoundException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Artist.
 */
public class Artist implements Serializable {
    private final UUID aid;
    private String email;
    private String username;
    private Map<UUID, Music> tracks;
    private Map<UUID, Album> albums;

    /**
     * Instantiates a new Artist.
     */
    public Artist() {
        this.aid = UUID.randomUUID();
        this.email = "";
        this.username = "";
        this.tracks = new HashMap<>();
        this.albums = new HashMap<>();
    }

    /**
     * Instantiates a new Artist.
     *
     * @param email    the email
     * @param username the username
     */
    public Artist(String email, String username) {
        this.aid = UUID.randomUUID();
        this.email = email;
        this.username = username;
        this.tracks = new HashMap<>();
        this.albums = new HashMap<>();
    }

    /**
     * Instantiates a new Artist.
     *
     * @param artist the artist
     */
    public Artist(Artist artist) {
        this.aid = artist.getArtistId();
        this.email = artist.getEmail();
        this.username = artist.getUsername();
        this.tracks = new HashMap<>(artist.getTracks());
        this.albums = artist.getAlbums().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new Album(entry.getValue())
                ));
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets tracks.
     *
     * @return the tracks
     */
    public Map<UUID, Music> getTracks() {
        return this.tracks.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    /**
     * Get artist id uuid.
     *
     * @return the uuid
     */
    public UUID getArtistId(){
        return this.aid;
    }

    /**
     * Add album.
     *
     * @param album the album
     */
    public void addAlbum(Album album) {
        this.albums.put(album.getAlbumId(), album);
    }

    /**
     * Update album.
     *
     * @param album the album
     */
    public void updateAlbum(Album album) {
        this.albums.put(album.getAlbumId(), album);
    }

    /**
     * Remove album.
     *
     * @param albumid the albumid
     */
    public void removeAlbum(UUID albumid) throws EntityNotFoundException {
        if (this.albums.containsKey(albumid)) this.albums.remove(albumid);
        else throw new EntityNotFoundException();
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public Map<UUID, Album> getAlbums() {
        return albums.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new Album(entry.getValue())
                ));
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Add music.
     *
     * @param music the music
     */
    public void addMusic(Music music) throws EntityAlreadyExistsException {
        if (tracks.containsKey(music.getMid())) throw new EntityAlreadyExistsException("music '" + music.getMid() + "' already exists!");
        tracks.put(music.getMid(), music);
    }

    /**
     * Remove music int.
     *
     * @param music the music
     * @return the int
     */
    public int removeMusic(Music music) {
        UUID mid = music.getMid();

        boolean removed = tracks.remove(mid) != null;

        for (Album album : albums.values()) {
            album.removeMusic(mid);
        }

        return removed ? 1 : 0;
    }

    /**
     * Returns a string representation of the artist with their info, tracks and albums.
     *
     * @return the formatted string representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int boxWidth = 50;
        int contentWidth = boxWidth - 4;

        // Header
        sb.append("╭────────────────────────────────────────────────╮\n");
        sb.append(String.format("│  \uD83C\uDFA7 %-42s │\n", "Artist: " + this.username));
        sb.append(String.format("│  ✉️ %-42s │\n", "Email: " + this.email));
        sb.append("├────────────────────────────────────────────────┤\n");

        if (!tracks.isEmpty()) {
            sb.append(String.format("│  \uD83C\uDFB5 Tracks:%-36s│\n", ""));

            tracks.values().stream()
                    .map(Music::getName)
                    .forEach(track -> sb.append(String.format("│  • %-44s│\n", track)));
        } else {
            sb.append(String.format("│  \uD83C\uDFB5 %-43s│\n", "No tracks"));
        }

        sb.append("├────────────────────────────────────────────────┤\n");
        if (!albums.isEmpty()) {
            sb.append(String.format("│  \uD83D\uDCD3 Albums:%-36s│\n", ""));
            albums.values().stream()
                    .map(Album::getName)
                    .forEach(album -> sb.append(String.format("│  • %-44s│\n", album)));
        } else {
            sb.append(String.format("│  \uD83D\uDCD3 %-43s│\n", "No albums"));
        }

        sb.append("╰────────────────────────────────────────────────╯");

        return sb.toString();
    }

    /**
     * Compares this artist to another object for equality.
     * Artists are equal if they have the same ID or same email, tracks, albums and username.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        return (this.email.equals(artist.getEmail()) &&
                this.tracks.equals(artist.getTracks()) &&
                this.albums.equals(artist.getAlbums()) &&
                this.username.equals(artist.getUsername())) ||
                this.aid.equals(artist.getArtistId());
    }
}
