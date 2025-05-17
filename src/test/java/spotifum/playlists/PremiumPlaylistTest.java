package spotifum.playlists;

import org.junit.jupiter.api.Test;
import spotifum.musics.Music;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Premium playlist test.
 */
public class PremiumPlaylistTest {

    private Music createSampleMusic(String name) {
        return new Music(name, "Artist", "Label", "Lyrics",
                Arrays.asList("note1", "note2"), "Genre", 180);
    }

    /**
     * Test constructor and defaults.
     */
    @Test
    public void testConstructorAndDefaults() {
        PremiumPlaylist playlist = new PremiumPlaylist("Chill", "octavio");

        assertEquals("Chill", playlist.getName());
        assertEquals("octavio", playlist.getOwner());
        assertTrue(playlist.getTracks().isEmpty());
        assertFalse(playlist.isShuffleModeActive());
    }

    /**
     * Test add and play current.
     */
    @Test
    public void testAddAndPlayCurrent() {
        PremiumPlaylist playlist = new PremiumPlaylist("Focus", "octavio");
        Music track = createSampleMusic("Lo-fi Beat");
        playlist.addMusic(track);

        Music current = playlist.playCurrent();
        assertEquals("Lo-fi Beat", current.getName());
    }

    /**
     * Test next track sequential.
     */
    @Test
    public void testNextTrackSequential() {
        PremiumPlaylist playlist = new PremiumPlaylist("Sequence", "octavio");
        playlist.addMusic(createSampleMusic("Track A"));
        playlist.addMusic(createSampleMusic("Track B"));
        playlist.addMusic(createSampleMusic("Track C"));

        Music first = playlist.playCurrent();
        Music second = playlist.nextTrack();
        Music third = playlist.nextTrack();
        Music wrapAround = playlist.nextTrack();

        assertEquals("Track A", first.getName());
        assertEquals("Track B", second.getName());
        assertEquals("Track C", third.getName());
        assertEquals("Track A", wrapAround.getName());
    }

    /**
     * Test next track shuffle mode.
     */
    @Test
    public void testNextTrackShuffleMode() {
        PremiumPlaylist playlist = new PremiumPlaylist("Shuffle", "octavio");
        playlist.addMusic(createSampleMusic("One"));
        playlist.addMusic(createSampleMusic("Two"));
        playlist.addMusic(createSampleMusic("Three"));

        playlist.setShuffleMode(true);
        playlist.playCurrent();

        Music next = playlist.nextTrack();

        assertNotNull(next);
    }

    /**
     * Test previous track with history.
     */
    @Test
    public void testPreviousTrackWithHistory() {
        PremiumPlaylist playlist = new PremiumPlaylist("History", "octavio");
        playlist.addMusic(createSampleMusic("Intro"));
        playlist.addMusic(createSampleMusic("Middle"));
        playlist.addMusic(createSampleMusic("Outro"));

        playlist.nextTrack();
        playlist.nextTrack();

        Music back = playlist.previousTrack();
        Music backAgain = playlist.previousTrack();

        assertEquals("Middle", back.getName());
        assertEquals("Intro", backAgain.getName());
    }

    /**
     * Test jump to track valid.
     */
    @Test
    public void testJumpToTrackValid() {
        PremiumPlaylist playlist = new PremiumPlaylist("Jump", "octavio");
        playlist.addMusic(createSampleMusic("One"));
        playlist.addMusic(createSampleMusic("Two"));
        playlist.addMusic(createSampleMusic("Three"));

        Music jumped = playlist.jumpToTrack(2);
        assertEquals("Three", jumped.getName());
    }

    /**
     * Test jump to track invalid.
     */
    @Test
    public void testJumpToTrackInvalid() {
        PremiumPlaylist playlist = new PremiumPlaylist("Invalid", "octavio");
        playlist.addMusic(createSampleMusic("Only One"));

        Music jumped = playlist.jumpToTrack(10);
        assertNull(jumped);
    }

    /**
     * Test clone creates copy.
     */
    @Test
    public void testCloneCreatesCopy() {
        PremiumPlaylist original = new PremiumPlaylist("CloneMe", "octavio");
        original.addMusic(createSampleMusic("Track A"));
        original.setShuffleMode(true);
        original.nextTrack();
        original.nextTrack();

        PremiumPlaylist clone = original.clone();

        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getOwner(), clone.getOwner());
        assertEquals(original.isShuffleModeActive(), clone.isShuffleModeActive());
        assertEquals(original.getNavigationHistory().size(), clone.getNavigationHistory().size());
    }

    /**
     * Test clear history.
     */
    @Test
    public void testClearHistory() {
        PremiumPlaylist playlist = new PremiumPlaylist("HistoryClear", "octavio");
        playlist.addMusic(createSampleMusic("X"));
        playlist.addMusic(createSampleMusic("Y"));
        playlist.nextTrack();
        playlist.nextTrack();

        assertFalse(playlist.getNavigationHistory().isEmpty());

        playlist.clearHistory();
        assertTrue(playlist.getNavigationHistory().isEmpty());
    }
}
