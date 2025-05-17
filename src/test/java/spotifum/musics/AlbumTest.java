package spotifum.musics;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import spotifum.exceptions.EntityAlreadyExistsException;

import java.util.*;

/**
 * The type Album test.
 */
public class AlbumTest {

    private Music createSampleMusic(String name) {
        return new Music(name, "Test Artist", "Test Label", "Lyrics...",
                Arrays.asList("note1", "note2"), "Pop", 200);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Album album = new Album("Greatest Hits", "DJ Test");
        assertNotNull(album);
        assertEquals("Greatest Hits", album.getName());
        assertEquals("DJ Test", album.getCreator());
        assertNotNull(album.getAlbumId());
        assertTrue(album.getMusics().isEmpty());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        Album original = new Album("Original Album", "Original Creator");
        Music music = createSampleMusic("Track 1");
        original.addMusic(music);

        Album copy = new Album(original);

        assertNotNull(copy);
        assertEquals(original.getAlbumId(), copy.getAlbumId());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getCreator(), copy.getCreator());
        assertEquals(original.getMusics().size(), copy.getMusics().size());
        assertEquals(original.getMusics().get(0).getName(), copy.getMusics().get(0).getName());
    }

    /**
     * Test set name.
     */
    @Test
    public void testSetName() {
        Album album = new Album("Initial", "Artist");
        album.setName("Updated");
        assertEquals("Updated", album.getName());
    }

    /**
     * Test add music.
     */
    @Test
    public void testAddMusic() {
        Album album = new Album("My Album", "Artist");
        Music music = createSampleMusic("Song A");

        album.addMusic(music);

        assertEquals(1, album.getMusics().size());
        assertEquals("Song A", album.getMusics().get(0).getName());
    }

    /**
     * Test add duplicate music.
     */
    @Test
    public void testAddDuplicateMusic() {
        Album album = new Album("My Album", "Artist");
        Music music = createSampleMusic("Song A");
        try {
            album.addMusic(music);
            album.addMusic(music);
        } catch (EntityAlreadyExistsException e) {
        }

        assertEquals(1, album.getMusics().size());
    }

    /**
     * Test add music at index.
     */
    @Test
    public void testAddMusicAtIndex() {
        Album album = new Album("My Album", "Artist");
        Music m1 = createSampleMusic("First");
        Music m2 = createSampleMusic("Second");

        album.addMusic(m1);
        album.addMusicAtIndex(m2, 0);

        assertEquals(2, album.getMusics().size());
        assertEquals("Second", album.getMusics().get(0).getName());
        assertEquals("First", album.getMusics().get(1).getName());
    }

    /**
     * Test remove music.
     */
    @Test
    public void testRemoveMusic() {
        Album album = new Album("My Album", "Artist");
        Music music = createSampleMusic("To Remove");
        album.addMusic(music);

        UUID mid = music.getMid();
        album.removeMusic(mid);

        assertEquals(0, album.getMusics().size());
    }

    /**
     * Test remove non existent music.
     */
    @Test
    public void testRemoveNonExistentMusic() {
        Album album = new Album("My Album", "Artist");

        album.removeMusic(UUID.randomUUID());

        assertEquals(0, album.getMusics().size());
    }

    /**
     * Test get musics encapsulation.
     */
    @Test
    public void testGetMusicsEncapsulation() {
        Album album = new Album("My Album", "Artist");
        Music music = createSampleMusic("Track X");
        album.addMusic(music);

        List<Music> external = album.getMusics();
        external.clear(); // não deve afetar a lista real

        assertEquals(1, album.getMusics().size());
    }
}
