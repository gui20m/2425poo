package spotifum.playlists;

import org.junit.jupiter.api.Test;
import spotifum.musics.Music;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Random playlist test.
 */
public class RandomPlaylistTest {

    private Music createSampleMusic(String name) {
        return new Music(name, "Artist", "Label", "Lyrics",
                Arrays.asList("note1", "note2"), "Genre", 200);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        RandomPlaylist playlist = new RandomPlaylist();
        assertEquals("", playlist.getName());
        assertEquals("spotifum", playlist.getOwner());
        assertTrue(playlist.isPublic());
        assertEquals(0, playlist.getCurrentTrackIndex());
        assertTrue(playlist.getTracks().isEmpty());
    }

    /**
     * Test constructor from playlist.
     */
    @Test
    public void testConstructorFromPlaylist() {
        Playlist base = new PremiumPlaylist("To Copy", "octavio");
        base.addMusic(createSampleMusic("Copy Track"));

        RandomPlaylist copied = new RandomPlaylist(base);

        assertEquals(base.getName(), copied.getName());
        assertEquals(base.getOwner(), copied.getOwner());
        assertEquals(1, copied.getTracks().size());
        assertEquals("Copy Track", copied.getTracks().get(0).getName());
    }

    /**
     * Test constructor from map shuffled.
     */
    @Test
    public void testConstructorFromMapShuffled() {
        Map<UUID, Music> musicMap = new LinkedHashMap<>();

        Music m1 = createSampleMusic("A");
        Music m2 = createSampleMusic("B");
        Music m3 = createSampleMusic("C");

        musicMap.put(UUID.randomUUID(), m1);
        musicMap.put(UUID.randomUUID(), m2);
        musicMap.put(UUID.randomUUID(), m3);

        RandomPlaylist playlist = new RandomPlaylist(musicMap);

        List<Music> tracks = playlist.getTracks();
        assertEquals(3, tracks.size());

        int found = 0;
        for (Music m : tracks) {
            if (m.getName().equals("A") || m.getName().equals("B") || m.getName().equals("C")) {
                found++;
            }
        }
        assertEquals(3, found);
    }

    /**
     * Test next track returns null.
     */
    @Test
    public void testNextTrackReturnsNull() {
        RandomPlaylist playlist = new RandomPlaylist();
        playlist.addMusic(createSampleMusic("Can't skip"));

        Music next = playlist.nextTrack();
        assertNull(next);
    }

    /**
     * Test previous track returns null.
     */
    @Test
    public void testPreviousTrackReturnsNull() {
        RandomPlaylist playlist = new RandomPlaylist();
        playlist.addMusic(createSampleMusic("No back"));

        Music previous = playlist.previousTrack();
        assertNull(previous);
    }

    /**
     * Test can skip is false.
     */
    @Test
    public void testCanSkipIsFalse() {
        RandomPlaylist playlist = new RandomPlaylist();
        assertFalse(playlist.canSkip());
    }

    /**
     * Test clone creates valid copy.
     */
    @Test
    public void testCloneCreatesValidCopy() {
        RandomPlaylist original = new RandomPlaylist();
        original.addMusic(createSampleMusic("Clone Me"));

        RandomPlaylist clone = original.clone();

        assertEquals(original.getTracks().size(), clone.getTracks().size());
        assertEquals(original.getTracks().get(0).getName(), clone.getTracks().get(0).getName());
    }
}
