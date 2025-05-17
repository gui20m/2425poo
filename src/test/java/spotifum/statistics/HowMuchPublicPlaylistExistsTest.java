package spotifum.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotifum.playlists.Playlist;
import spotifum.playlists.PremiumPlaylist;
import spotifum.playlists.RandomPlaylist;
import spotifum.musics.Music;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HowMuchPublicPlaylistExistsTest {

    private HowMuchPublicPlaylistsExist stats;
    private Map<UUID, Playlist> publicPlaylists;
    private List<RandomPlaylist> randomPlaylists;

    @BeforeEach
    void setUp() {
        stats = new HowMuchPublicPlaylistsExist();

        // Initialize test data
        publicPlaylists = new HashMap<>();
        randomPlaylists = new ArrayList<>();

        // Create test playlists - using basic constructor
        Playlist p1 = new PremiumPlaylist();
        Playlist p2 = new PremiumPlaylist();
        p2.setPublic(false);

        publicPlaylists.put(p1.getId(), p1);
        publicPlaylists.put(p2.getId(), p2);

        // Create random playlists - using basic constructor
        RandomPlaylist rp1 = new RandomPlaylist();

        randomPlaylists.add(rp1);
    }

    @Test
    void testInitialization() {
        assertNotNull(stats);
        assertTrue(stats.getRandomPlaylists().size()==0); // Should be null until set
        assertNotNull(new HowMuchPublicPlaylistsExist().getRandomPlaylists()); // Should be initialized empty
    }

    @Test
    void testSetRandomPlaylists() {
        // Use the randomPlaylists created in setUp()
        stats.setRandomPlaylists(randomPlaylists);

        assertNotNull(stats.getRandomPlaylists());
        assertEquals(1, stats.getRandomPlaylists().size());
        assertTrue(stats.getRandomPlaylists().get(0) instanceof RandomPlaylist);
    }

    @Test
    void testSetPublicplaylists() {
        // Use the publicPlaylists created in setUp()
        stats.setPublicplaylists(publicPlaylists);

        assertEquals(1, stats.getPublicplaylists().size());
        assertTrue(stats.getPublicplaylists().values().stream().anyMatch(p -> p instanceof PremiumPlaylist));
    }

    @Test
    void testDisplayPublicPlaylists() {
        // Setup using data from setUp()
        stats.setPublicplaylists(publicPlaylists);
        stats.setRandomPlaylists(randomPlaylists);

        assertDoesNotThrow(() -> stats.displayPublicPlaylists());
    }

    @Test
    void testEmptyCollections() {
        // Test with empty collections (not using setUp data)
        stats.setRandomPlaylists(new ArrayList<>());
        stats.setPublicplaylists(new HashMap<>());

        assertNotNull(stats.getRandomPlaylists());
        assertEquals(0, stats.getRandomPlaylists().size());
        assertEquals(0, stats.getRandomPlaylists().size());
    }

    @Test
    void testRandomPlaylistProperties() {
        // Create a new one for specific property testing
        RandomPlaylist rp = new RandomPlaylist();
        assertFalse(rp.canSkip());
        assertNull(rp.nextTrack());
        assertNull(rp.previousTrack());
    }

    @Test
    void testCloneRandomPlaylist() {
        // Create a new one for clone testing
        RandomPlaylist original = new RandomPlaylist();
        original.addMusic(new Music());

        RandomPlaylist clone = original.clone();
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getOwner(), clone.getOwner());
        assertEquals(original.getTracks().size(), clone.getTracks().size());
    }
}