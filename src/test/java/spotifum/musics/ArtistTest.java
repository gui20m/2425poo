package spotifum.musics;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

import spotifum.exceptions.EntityNotFoundException;
import spotifum.musics.Artist;
import spotifum.musics.Music;
import spotifum.musics.Album;

/**
 * The type Artist test.
 */
public class ArtistTest {

    private Music createSampleMusic(String name) {
        return new Music(name, "Test Artist", "Label", "Lyrics",
                Arrays.asList("note1", "note2"), "Pop", 180);
    }

    private Album createSampleAlbum(String name, String creator) {
        return new Album(name, creator);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Artist artist = new Artist();

        assertNotNull(artist);
        assertEquals("", artist.getEmail());
        assertEquals("", artist.getUsername());
        assertNotNull(artist.getTracks());
        assertNotNull(artist.getAlbums());
        assertEquals(0, artist.getTracks().size());
        assertEquals(0, artist.getAlbums().size());
    }

    /**
     * Test parameterized constructor.
     */
    @Test
    public void testParameterizedConstructor() {
        Artist artist = new Artist("email@music.com", "Test Artist");

        assertEquals("email@music.com", artist.getEmail());
        assertEquals("Test Artist", artist.getUsername());
        assertEquals(0, artist.getTracks().size());
        assertEquals(0, artist.getAlbums().size());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        Artist original = new Artist("copy@artist.com", "Test Artist");
        Music music = createSampleMusic("Original Song");
        Album album = createSampleAlbum("Original Album", "Test Artist");

        original.addMusic(music);
        original.addAlbum(album);

        Artist copy = new Artist(original);

        assertEquals(original.getEmail(), copy.getEmail());
        assertEquals(original.getUsername(), copy.getUsername());
        assertEquals(1, copy.getTracks().size());
        assertTrue(copy.getTracks().containsKey(music.getMid()));
        assertEquals(original.getAlbums().size(), copy.getAlbums().size());
    }

    /**
     * Test setters and getters.
     */
    @Test
    public void testSettersAndGetters() {
        Artist artist = new Artist();
        artist.setEmail("set@test.com");
        artist.setUsername("newuser");

        assertEquals("set@test.com", artist.getEmail());
        assertEquals("newuser", artist.getUsername());
    }

    /**
     * Test add music.
     */
    @Test
    public void testAddMusic() {
        Artist artist = new Artist();
        Music music = createSampleMusic("Test Song");

        artist.addMusic(music);

        assertEquals(1, artist.getTracks().size());
        assertEquals(music, artist.getTracks().get(music.getMid()));
    }

    /**
     * Test remove music.
     */
    @Test
    public void testRemoveMusic() {
        Artist artist = new Artist();
        Music music = createSampleMusic("Track A");

        artist.addMusic(music);
        int result = artist.removeMusic(music);

        assertEquals(1, result);
        assertEquals(0, artist.getTracks().size());
    }

    /**
     * Test remove nonexistent music.
     */
    @Test
    public void testRemoveNonexistentMusic() {
        Artist artist = new Artist();
        Music music = createSampleMusic("Ghost Track");

        int result = artist.removeMusic(music);

        assertEquals(0, result);
        assertEquals(0, artist.getTracks().size());
    }

    /**
     * Test remove music also removes from albums.
     */
    @Test
    public void testRemoveMusicAlsoRemovesFromAlbums() {
        Artist artist = new Artist();
        Music music = createSampleMusic("To Remove");

        Album album = createSampleAlbum("Album", "");
        album.addMusic(music);

        artist.addMusic(music);
        artist.addAlbum(album);

        artist.removeMusic(music);

        Album updatedAlbum = artist.getAlbums().get(album.getAlbumId());
        assertNotNull(updatedAlbum);
        assertEquals(0, updatedAlbum.getMusics().size());
    }

    /**
     * Test add album.
     */
    @Test
    public void testAddAlbum() {
        Artist artist = new Artist();
        Album album = createSampleAlbum("Album 1", "");

        artist.addAlbum(album);

        assertEquals(1, artist.getAlbums().size());
        assertTrue(artist.getAlbums().get(album.getAlbumId()).equals(album));
    }

    /**
     * Test update album.
     */
    @Test
    public void testUpdateAlbum() {
        Artist artist = new Artist();
        Album album = createSampleAlbum("Album A", "");
        artist.addAlbum(album);

        Album updatedAlbum = new Album("Album A (Updated)", "");
        UUID id = album.getAlbumId();

        // forçar a atualização com o mesmo ID
        Album replacement = new Album(updatedAlbum) {
            @Override
            public UUID getAlbumId() {
                return id;
            }
        };

        artist.updateAlbum(replacement);

        assertEquals(1, artist.getAlbums().size());
        assertEquals("Album A (Updated)", artist.getAlbums().get(id).getName());
    }

    /**
     * Test remove album.
     */
    @Test
    public void testRemoveAlbum() {
        Artist artist = new Artist();
        Album album = createSampleAlbum("Delete Me" ,"");

        artist.addAlbum(album);
        artist.removeAlbum(album.getAlbumId());

        assertEquals(0, artist.getAlbums().size());
    }

    /**
     * Test remove nonexistent album.
     */
    @Test
    public void testRemoveNonexistentAlbum() {
        Artist artist = new Artist();
        UUID fakeId = UUID.randomUUID();
        try {
            artist.removeAlbum(fakeId);
        } catch (EntityNotFoundException e) {
        }

        assertEquals(0, artist.getAlbums().size());
    }

    /**
     * Test get tracks encapsulation.
     */
    @Test
    public void testGetTracksEncapsulation() {
        Artist artist = new Artist();
        Music music = createSampleMusic("Secure Track");
        artist.addMusic(music);

        Map<UUID, Music> externalTracks = artist.getTracks();
        externalTracks.clear(); // não deve afetar original

        assertEquals(1, artist.getTracks().size());
    }

    /**
     * Test get albums encapsulation.
     */
    @Test
    public void testGetAlbumsEncapsulation() {
        Artist artist = new Artist();
        Album album = createSampleAlbum("Secure Album", "");
        artist.addAlbum(album);

        Map<UUID, Album> externalAlbums = artist.getAlbums();
        externalAlbums.clear();

        assertEquals(1, artist.getAlbums().size());
    }
}
