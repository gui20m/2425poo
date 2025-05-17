package spotifum.playlists;

import org.junit.jupiter.api.Test;
import spotifum.musics.Album;
import spotifum.musics.Music;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Playlist test.
 */
public class PlaylistTest {

    /**
     * The type Test playlist.
     */
    static class TestPlaylist extends Playlist {
        /**
         * Instantiates a new Test playlist.
         */
        public TestPlaylist() { super(); }

        /**
         * Instantiates a new Test playlist.
         *
         * @param name  the name
         * @param owner the owner
         */
        public TestPlaylist(String name, String owner) { super(name, owner); }

        /**
         * Instantiates a new Test playlist.
         *
         * @param p the p
         */
        public TestPlaylist(Playlist p) { super(p); }

        @Override
        public Music nextTrack() { return null; }

        @Override
        public Music previousTrack() { return null; }

        @Override
        public boolean canSkip() { return true; }

        @Override
        public Playlist clone() { return new TestPlaylist(this); }
    }

    private Music createSampleMusic(String name) {
        return new Music(name, "Artist", "Label", "Lyrics",
                Arrays.asList("note1", "note2"), "Genre", 180);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        TestPlaylist playlist = new TestPlaylist();
        assertNotNull(playlist.getId());
        assertEquals("", playlist.getName());
        assertEquals("spotifum", playlist.getOwner());
        assertTrue(playlist.isPublic());
        assertTrue(playlist.isEmpty());
        assertEquals(0, playlist.getCurrentTrackIndex());
    }

    /**
     * Test parameterized constructor.
     */
    @Test
    public void testParameterizedConstructor() {
        TestPlaylist playlist = new TestPlaylist("Relaxing", "octavio");
        assertEquals("Relaxing", playlist.getName());
        assertEquals("octavio", playlist.getOwner());
        assertTrue(playlist.isEmpty());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        TestPlaylist original = new TestPlaylist("Copy", "octavio");
        Music music = createSampleMusic("Copy Track");
        original.addMusic(music);
        original.setCurrentTrackIndex(0);
        original.setPublic(false);

        TestPlaylist copy = new TestPlaylist(original);

        assertEquals(original.getId(), copy.getId());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getOwner(), copy.getOwner());
        assertEquals(original.getTracks().size(), copy.getTracks().size());
        assertEquals(original.getTracks().get(0).getName(), copy.getTracks().get(0).getName());
        assertEquals(original.getCurrentTrackIndex(), copy.getCurrentTrackIndex());
        assertEquals(original.isPublic(), copy.isPublic());
    }

    /**
     * Test add music.
     */
    @Test
    public void testAddMusic() {
        TestPlaylist playlist = new TestPlaylist("Add Test", "octavio");
        Music music = createSampleMusic("Song One");
        playlist.addMusic(music);

        List<Music> tracks = playlist.getTracks();
        assertEquals(1, tracks.size());
        assertEquals("Song One", tracks.get(0).getName());
    }

    /**
     * Test add music to index.
     */
    @Test
    public void testAddMusicToIndex() {
        TestPlaylist playlist = new TestPlaylist();
        Music m1 = createSampleMusic("Track A");
        Music m2 = createSampleMusic("Track B");

        playlist.addMusic(m1);
        playlist.addMusicToIndex(m2, 0);

        List<Music> tracks = playlist.getTracks();
        assertEquals(2, tracks.size());
        assertEquals("Track B", tracks.get(0).getName());
        assertEquals("Track A", tracks.get(1).getName());
    }

    /**
     * Test add album.
     */
    @Test
    public void testAddAlbum() {
        TestPlaylist playlist = new TestPlaylist();
        Album album = new Album("Hits", "octavio");
        album.addMusic(createSampleMusic("Hit 1"));
        album.addMusic(createSampleMusic("Hit 2"));

        playlist.addAlbum(album);

        List<Music> tracks = playlist.getTracks();
        assertEquals(2, tracks.size());
        assertEquals("Hit 1", tracks.get(0).getName());
        assertEquals("Hit 2", tracks.get(1).getName());
    }

    /**
     * Test remove music valid.
     */
    @Test
    public void testRemoveMusicValid() {
        TestPlaylist playlist = new TestPlaylist();
        Music music = createSampleMusic("Delete Me");
        playlist.addMusic(music);

        boolean removed = playlist.removeMusic(music);
        assertTrue(removed);
        assertTrue(playlist.getTracks().isEmpty());
    }

    /**
     * Test remove music invalid.
     */
    @Test
    public void testRemoveMusicInvalid() {
        TestPlaylist playlist = new TestPlaylist();
        Music notAdded = createSampleMusic("Ghost Track");

        boolean removed = playlist.removeMusic(notAdded);
        assertFalse(removed);
        assertTrue(playlist.getTracks().isEmpty());
    }

    /**
     * Test remove music null.
     */
    @Test
    public void testRemoveMusicNull() {
        TestPlaylist playlist = new TestPlaylist();
        assertFalse(playlist.removeMusic(null));
    }

    /**
     * Test play current track.
     */
    @Test
    public void testPlayCurrentTrack() {
        TestPlaylist playlist = new TestPlaylist();
        Music music = createSampleMusic("Play Me");
        playlist.addMusic(music);

        Music played = playlist.playCurrent();
        assertNotNull(played);
        assertEquals("Play Me", played.getName());
    }

    /**
     * Test play current empty.
     */
    @Test
    public void testPlayCurrentEmpty() {
        TestPlaylist playlist = new TestPlaylist();
        Music played = playlist.playCurrent();
        assertNull(played);
    }

    /**
     * Test get total duration.
     */
    @Test
    public void testGetTotalDuration() {
        TestPlaylist playlist = new TestPlaylist();
        playlist.addMusic(createSampleMusic("Track 1"));
        playlist.addMusic(createSampleMusic("Track 2"));

        assertEquals(360, playlist.getTotalDuration());
    }

    /**
     * Test clone creates equivalent.
     */
    @Test
    public void testCloneCreatesEquivalent() {
        TestPlaylist playlist = new TestPlaylist("Clone", "octavio");
        playlist.addMusic(createSampleMusic("Cloneable Track"));

        Playlist clone = playlist.clone();

        assertEquals(playlist.getName(), clone.getName());
        assertEquals(playlist.getOwner(), clone.getOwner());
        assertEquals(playlist.getTracks().size(), clone.getTracks().size());
    }

}
