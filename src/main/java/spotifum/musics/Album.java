package spotifum.musics;

import spotifum.exceptions.EntityAlreadyExistsException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Album.
 */
public class Album implements Serializable {
    private final UUID albumId;
    private String name;
    private String creator;
    private List<Music> musics;

    /**
     * Instantiates a new Album.
     *
     * @param name    the name
     * @param creator the creator
     */
    public Album(String name, String creator) {
        this.albumId = UUID.randomUUID();
        this.name = name;
        this.creator = creator;
        this.musics = new ArrayList<>();
    }

    /**
     * Instantiates a new Album.
     *
     * @param al the al
     */
    public Album(Album al){
        this.albumId = al.getAlbumId();
        this.name = al.getName();
        this.creator = al.getCreator();
        this.musics = al.getMusics().stream().map(Music::clone).collect(Collectors.toList());
    }

    /**
     * Sets creator.
     *
     * @param creator the creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Gets creator.
     *
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Gets album id.
     *
     * @return the album id
     */
    public UUID getAlbumId() {
        return albumId;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets musics.
     *
     * @return the musics
     */
    public List<Music> getMusics() {
        return this.musics.stream().map(Music::clone).collect(Collectors.toList());
    }

    /**
     * Add music.
     *
     * @param music the music
     */
    public void addMusic(Music music) throws EntityAlreadyExistsException {
            if (musics.contains(music)) {
                throw new EntityAlreadyExistsException();
            }
            musics.add(music);
            System.out.println("[app] music '" + music.getName() + "' added successfully!");
    }

    /**
     * Add music at index.
     *
     * @param music the music
     * @param index the index
     */
    public void addMusicAtIndex(Music music, int index) {
        if (musics.contains(music)) {
            System.out.println("[app-log] music '" + music.getName() + "' already exists!");
            return;
        }

        musics.add(index , music);
        System.out.println("[app] music '" + music.getName() + "' added successfully!");
    }

    /**
     * Remove music.
     *
     * @param mid the mid
     */
    public void removeMusic(UUID mid) {
        musics.removeIf(m -> m.getMid().equals(mid));
    }

    /**
     * Returns a string representation of the album with its name and tracks.
     *
     * @return the string representation of the album
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("╭────────────────────────────────────────────────╮\n");
        sb.append(String.format("│  \uD83D\uDCD3 %-43s│\n", "Album: " + this.name));
        sb.append("├────────────────────────────────────────────────┤\n");

        if (!musics.isEmpty()) {
            sb.append(String.format("│  \uD83C\uDFB5 Tracks:%-36s│\n", ""));
            musics.stream()
                    .map(Music::getName)
                    .forEach(track -> sb.append(String.format("│  • %-44s│\n", track)));
        } else {
            sb.append(String.format("│  \uD83C\uDFB5 %-43s│\n", "No tracks in this album"));
        }

        sb.append("╰────────────────────────────────────────────────╯");

        return sb.toString();
    }

    /**
     * Compares this album to another object for equality.
     * Albums are equal if they have the same ID or same name and music list.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        return (this.name.equals(album.getName()) &&
                this.musics.equals(album.getMusics())) ||
                this.albumId.equals(album.getAlbumId());
    }

    /**
     * Creates and returns a deep copy of this album.
     *
     * @return a clone of this album
     */
    public Album clone(){
        return new Album(this);
    }
}
