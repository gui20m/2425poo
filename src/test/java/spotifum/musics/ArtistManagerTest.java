package spotifum.musics;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import spotifum.musics.*;
import spotifum.exceptions.*;

/**
 * The type Artist manager test.
 */
public class ArtistManagerTest {

    private List<String> createSamplePartiture() {
        return Arrays.asList("note1", "note2", "note3");
    }

    private Music createSampleMusic() {
        return new Music("Test Song", "Test Artist", "Label", "Lyrics",
                createSamplePartiture(), "Pop", 180);
    }

    private Artist createSampleArtist() {
        return new Artist("test@artist.com", "artistUser");
    }

    /**
     * Test insert artist.
     *
     * @throws Exception the exception
     */
    @Test
    public void testInsertArtist() throws Exception {
        ArtistManager manager = new ArtistManager();
        Artist artist = createSampleArtist();

        manager.insertArtist(artist);
        assertTrue(manager.existArtistWithEmail("test@artist.com"));
    }

    /**
     * Test insert artist already exists.
     *
     * @throws Exception the exception
     */
    @Test
    public void testInsertArtistAlreadyExists() throws Exception {
        ArtistManager manager = new ArtistManager();
        Artist artist = createSampleArtist();

        manager.insertArtist(artist);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            manager.insertArtist(artist);
        });
    }

    /**
     * Test update artist.
     *
     * @throws Exception the exception
     */
    @Test
    public void testUpdateArtist() throws Exception {
        ArtistManager manager = new ArtistManager();
        Artist artist = new Artist("update@artist.com", "original");
        manager.insertArtist(artist);

        Artist updated = new Artist("update@artist.com", "updatedUser");
        manager.updateArtist(updated);

        Artist result = manager.getArtist("update@artist.com");
        assertEquals("updatedUser", result.getUsername());
    }

    /**
     * Test update nonexistent artist.
     */
    @Test
    public void testUpdateNonexistentArtist() {
        ArtistManager manager = new ArtistManager();
        Artist artist = new Artist("ghost@artist.com", "ghost");

        assertThrows(EntityNotFoundException.class, () -> {
            manager.updateArtist(artist);
        });
    }

    /**
     * Test exist artist with email.
     *
     * @throws Exception the exception
     */
    @Test
    public void testExistArtistWithEmail() throws Exception {
        ArtistManager manager = new ArtistManager();
        Artist artist = createSampleArtist();
        manager.insertArtist(artist);

        assertTrue(manager.existArtistWithEmail("test@artist.com"));
        assertFalse(manager.existArtistWithEmail("nonexistent@artist.com"));
    }

    /**
     * Test get artist.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetArtist() throws Exception {
        ArtistManager manager = new ArtistManager();
        Artist artist = createSampleArtist();
        manager.insertArtist(artist);

        Artist fetched = manager.getArtist("test@artist.com");
        assertEquals("artistUser", fetched.getUsername());
    }

    /**
     * Test get artist not found.
     */
    @Test
    public void testGetArtistNotFound() {
        ArtistManager manager = new ArtistManager();

        assertThrows(EntityNotFoundException.class, () -> {
            manager.getArtist("missing@artist.com");
        });
    }

    /**
     * Test add music to artist.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAddMusicToArtist() throws Exception {
        ArtistManager manager = new ArtistManager();
        Artist artist = createSampleArtist();
        manager.insertArtist(artist);

        int result = manager.addMusic("test@artist.com", "New Song", "Artist A", "Label A", "Lyrics A",
                createSamplePartiture(), "Genre", 200, false, false, null);

        assertEquals(1, result);
        assertEquals(1, manager.getArtist("test@artist.com").getTracks().size());
    }

    /**
     * Test add music to nonexistent artist.
     */
    @Test
    public void testAddMusicToNonexistentArtist() {
        ArtistManager manager = new ArtistManager();

        assertThrows(EntityNotFoundException.class, () -> {
            manager.addMusic("ghost@artist.com", "Song", "Artist", "Label", "Lyrics",
                    createSamplePartiture(), "Genre", 200, false, false, null);
        });
    }

    /**
     * Test search musics.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSearchMusics() throws Exception {
        ArtistManager manager = new ArtistManager();
        Artist a1 = new Artist("a1@mail.com", "a1");
        Artist a2 = new Artist("a2@mail.com", "a2");

        a1.addMusic(new Music("Song One", "X", "Y", "Z", createSamplePartiture(), "Pop", 200));
        a2.addMusic(new Music("Another Song One", "X", "Y", "Z", createSamplePartiture(), "Pop", 200));

        manager.insertArtist(a1);
        manager.insertArtist(a2);

        Map<UUID, Music> results = manager.searchMusics("one");

        assertEquals(2, results.size());
    }

    /**
     * Test get artists songs.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetArtistsSongs() throws Exception {
        ArtistManager manager = new ArtistManager();
        Artist artist1 = new Artist("artist1@mail.com", "artist1");
        Artist artist2 = new Artist("artist2@mail.com", "artist2");

        artist1.addMusic(new Music("Track 1", "X", "Y", "Lyrics", createSamplePartiture(), "Pop", 180));
        artist2.addMusic(new Music("Track 2", "X", "Y", "Lyrics", createSamplePartiture(), "Rock", 200));

        manager.insertArtist(artist1);
        manager.insertArtist(artist2);

        Map<UUID, Music> songs = manager.getArtistsSongs();
        assertEquals(2, songs.size());
    }
}
