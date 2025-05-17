package spotifum.playlists;

import java.util.*;
import spotifum.musics.*;

/**
 * The type Premium playlist.
 */
public class PremiumPlaylist extends Playlist {
    /**
     * The Shuffle mode.
     */
    protected boolean shuffleMode;
    /**
     * The Navigation history.
     */
    protected final List<Integer> navigationHistory;

    /**
     * Random number generator used for shuffle mode functionality.
     */
    private Random random;

    public PremiumPlaylist() {
        super();
        this.shuffleMode = false;
        this.navigationHistory = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Instantiates a new Premium playlist.
     *
     * @param name  the name
     * @param owner the owner
     */
    public PremiumPlaylist(String name, String owner) {
        super(name, owner);
        this.shuffleMode = false;
        this.navigationHistory = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Instantiates a new Premium playlist.
     *
     * @param premiumPlaylist the premium playlist
     * @param owner           the owner
     */
    public PremiumPlaylist(PremiumPlaylist premiumPlaylist, String owner) {
        super(premiumPlaylist.getName(), owner);
        this.shuffleMode = premiumPlaylist.isShuffleModeActive();
        this.random = new Random();
        this.navigationHistory = new ArrayList<>(premiumPlaylist.getNavigationHistory());
    }

    /**
     * Checks if skipping tracks is allowed (always true for premium playlists).
     * @return true (premium playlists always allow skipping)
     */
    public boolean canSkip(){
        return true;
    }

    /**
     * Goes to the previous track in the playlist using navigation history.
     * @return the previous music track, or current track if no history
     */
    public Music previousTrack() {
        if (getTracks().isEmpty()) {
            System.out.println("[app] empty playlist");
            return null;
        }

        if (navigationHistory.isEmpty()) {
            return playCurrent();
        }

        setCurrentTrackIndex(navigationHistory.remove(navigationHistory.size() - 1));
        return playCurrent();
    }

    /**
     * Advances to the next track in the playlist, with shuffle mode support.
     * @return the next music track (random if shuffle mode is on)
     */
    public Music nextTrack() {
        List<Music> tracks = getTracks();
        if (tracks.isEmpty()) {
            System.out.println("[app] empty playlist");
            return null;
        }

        recordNavigation();

        if (getTracks().size() == 1) {
            return playCurrent();
        }

        if (shuffleMode) {
            int newIndex;
            do {
                newIndex = random.nextInt(tracks.size());
            } while (tracks.size() > 1 && newIndex == getCurrentTrackIndex());

            setCurrentTrackIndex(newIndex);
        } else {
            int newIndex = (getCurrentTrackIndex() + 1) % tracks.size();
            setCurrentTrackIndex(newIndex);
        }

        return playCurrent();
    }

    /**
     * Jump to track music.
     *
     * @param index the index
     * @return the music
     */
    public Music jumpToTrack(int index) {
        if (index < 0 || index >= getTracks().size()) {
            System.out.println("[app] invalid track index");
            return null;
        }
        recordNavigation();
        setCurrentTrackIndex(index);
        return playCurrent();
    }

    /**
     * Sets shuffle mode.
     *
     * @param shuffleMode the shuffle mode
     */
    public void setShuffleMode(boolean shuffleMode) {
        this.shuffleMode = shuffleMode;
    }

    /**
     * Is shuffle mode active boolean.
     *
     * @return the boolean
     */
    public boolean isShuffleModeActive() {
        return this.shuffleMode;
    }

    /**
     * Gets navigation history.
     *
     * @return the navigation history
     */
    public List<Integer> getNavigationHistory() {
        return new ArrayList<>(this.navigationHistory);
    }

    /**
     * Clear history.
     */
    public void clearHistory() {
        this.navigationHistory.clear();
    }

    /**
     * Records the current track index in navigation history.
     */
    protected void recordNavigation(){
        navigationHistory.add(getCurrentTrackIndex());
    }

    /**
     * Creates and returns a copy of this premium playlist.
     * @return a cloned premium playlist
     */
    public PremiumPlaylist clone(){
        return new PremiumPlaylist(this,  getOwner());
    }
}