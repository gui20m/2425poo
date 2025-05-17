package spotifum.musics;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

import spotifum.musics.Music;

/**
 * The type Music test.
 */
public class MusicTest {

    private List<String> sampleNotes() {
        return Arrays.asList("do", "re", "mi");
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Music music = new Music();

        assertNotNull(music.getMid());
        assertEquals("", music.getName());
        assertEquals("", music.getArtist());
        assertEquals("", music.getLabel());
        assertEquals("", music.getLyrics());
        assertEquals("", music.getGenre());
        assertEquals(0, music.getDuration());
        assertNotNull(music.getMusic());
        assertTrue(music.getMusic().isEmpty());
    }

    /**
     * Test parameterized constructor.
     */
    @Test
    public void testParameterizedConstructor() {
        Music music = new Music("Song A", "Artist X", "Label Y", "Lyrics here",
                sampleNotes(), "Pop", 210);

        assertEquals("Song A", music.getName());
        assertEquals("Artist X", music.getArtist());
        assertEquals("Label Y", music.getLabel());
        assertEquals("Lyrics here", music.getLyrics());
        assertEquals("Pop", music.getGenre());
        assertEquals(210, music.getDuration());
        assertEquals(sampleNotes(), music.getMusic());
        assertNotNull(music.getMid());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        Music original = new Music("Copy Me", "Artist A", "Label B", "Text",
                sampleNotes(), "Rock", 180);

        Music copy = new Music(original);

        assertNotSame(original, copy);
        assertEquals(original.getMid(), copy.getMid());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getArtist(), copy.getArtist());
        assertEquals(original.getLabel(), copy.getLabel());
        assertEquals(original.getLyrics(), copy.getLyrics());
        assertEquals(original.getGenre(), copy.getGenre());
        assertEquals(original.getDuration(), copy.getDuration());
        assertEquals(original.getMusic(), copy.getMusic());
    }

    /**
     * Test setters and getters.
     */
    @Test
    public void testSettersAndGetters() {
        Music music = new Music();

        music.setName("Test Song");
        music.setArtist("Test Artist");
        music.setLabel("Test Label");
        music.setLyrics("Test Lyrics");
        music.setGenre("Jazz");
        music.setDuration(300);
        music.setMusic(sampleNotes());

        assertEquals("Test Song", music.getName());
        assertEquals("Test Artist", music.getArtist());
        assertEquals("Test Label", music.getLabel());
        assertEquals("Test Lyrics", music.getLyrics());
        assertEquals("Jazz", music.getGenre());
        assertEquals(300, music.getDuration());
        assertEquals(sampleNotes(), music.getMusic());
    }

    /**
     * Test clone.
     */
    @Test
    public void testClone() {
        Music original = new Music("Original", "Artist Z", "Label X", "Lyrics Z",
                sampleNotes(), "Electronic", 240);

        Music clone = original.clone();

        assertNotSame(original, clone);
        assertEquals(original.getMid(), clone.getMid());
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getArtist(), clone.getArtist());
        assertEquals(original.getLabel(), clone.getLabel());
        assertEquals(original.getLyrics(), clone.getLyrics());
        assertEquals(original.getGenre(), clone.getGenre());
        assertEquals(original.getDuration(), clone.getDuration());
        assertEquals(original.getMusic(), clone.getMusic());
    }

    /**
     * Test equals same values.
     */
    @Test
    public void testEqualsSameValues() {
        Music m1 = new Music("Equal Song", "Artist", "Label", "Lyrics",
                sampleNotes(), "Genre", 200);
        Music m2 = new Music("Equal Song", "Artist", "Label", "Lyrics",
                sampleNotes(), "Genre", 200);

        assertTrue(m1.equals(m2));
    }

    /**
     * Test equals different uui ds same content.
     */
    @Test
    public void testEqualsDifferentUUIDsSameContent() {
        Music m1 = new Music("Same", "Artist", "Label", "Lyrics",
                sampleNotes(), "Genre", 180);
        Music m2 = new Music(m1);

        assertTrue(m1.equals(m2));
    }

    /**
     * Test equals different content.
     */
    @Test
    public void testEqualsDifferentContent() {
        Music m1 = new Music("Track 1", "Artist A", "Label A", "Lyrics A",
                sampleNotes(), "Genre A", 200);
        Music m2 = new Music("Track 2", "Artist B", "Label B", "Lyrics B",
                sampleNotes(), "Genre B", 250);

        assertFalse(m1.equals(m2));
    }

    /**
     * Test get music encapsulation.
     */
    @Test
    public void testGetMusicEncapsulation() {
        Music music = new Music("Title", "Artist", "Label", "Lyrics",
                sampleNotes(), "Genre", 150);

        List<String> external = music.getMusic();
        external.clear();

        assertEquals(3, music.getMusic().size());
    }
}
