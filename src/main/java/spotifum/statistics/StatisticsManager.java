package spotifum.statistics;

import spotifum.exceptions.EntityNotFoundException;
import spotifum.musics.*;
import spotifum.playlists.Playlist;
import spotifum.playlists.RandomPlaylist;
import spotifum.users.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * The type Statistics manager.
 */
public class StatisticsManager implements Serializable {
    private UserManager userManager;
    private ArtistManager artistManager;
    private MostPlayedMusic mostPlayedMusic;
    private MostPlayedArtist mostPlayedArtist;
    private MostListeningTimeUser mostListeningTime;
    private UserLeaderBoard userLeaderBoard;
    private MostPlayedGenres mostPlayedGenres;
    private HowMuchPublicPlaylistsExist howMuchPublicPlaylistsExist;
    private WhoHaveMostPlaylists whoHaveMostPlaylists;

    /**
     * Instantiates a new Statistics manager.
     *
     * @param usermanager   the usermanager
     * @param artistmanager the artistmanager
     */
    public StatisticsManager(UserManager usermanager, ArtistManager artistmanager) {
        this.userManager = usermanager;
        this.artistManager = artistmanager;
        this.mostPlayedMusic = new MostPlayedMusic();
        this.mostPlayedArtist = new MostPlayedArtist();
        this.mostListeningTime = new MostListeningTimeUser();
        this.userLeaderBoard = new UserLeaderBoard();
        this.mostPlayedGenres = new MostPlayedGenres();
        this.howMuchPublicPlaylistsExist = new HowMuchPublicPlaylistsExist();
        this.whoHaveMostPlaylists = new WhoHaveMostPlaylists();
    }

    /**
     * Remove user.
     *
     * @param user the user
     */
    public void removeUser(User user) throws EntityNotFoundException {
        if (user == null) {
            throw new EntityNotFoundException();
        }
        this.mostListeningTime.removeUser(user);
        this.userLeaderBoard.removeUser(user);
    }

    /**
     * Record play.
     *
     * @param music the music
     */
    public void recordPlay(Music music) {
        if (music != null) {
            this.mostPlayedMusic.recordPlay(music);
            this.mostPlayedGenres.recordPlay(music);
        }
    }

    /**
     * Record play.
     *
     * @param artist the artist
     */
    public void recordPlay(Artist artist) {
        if (artist != null) {
            this.mostPlayedArtist.recordPlay(artist);
        }
    }

    /**
     * Record play.
     *
     * @param user the user
     * @param date the date
     */
    public void recordPlay(User user, LocalDate date) {
        if (user != null) {
            this.mostListeningTime.recordPlay(user, date);
        }
    }

    /**
     * Record play.
     *
     * @param user  the user
     * @param music the music
     */
    public void recordPlay(User user, Music music) {
        if (user != null) {
            this.userLeaderBoard.recordPlay(user, music);
        }
    }

    /**
     * Get most played music.
     */
    public void getMostPlayedMusic(){
        this.mostPlayedMusic.getMostPlayedMusic();
    }

    /**
     * Get most played artist.
     */
    public Artist getMostPlayedArtist(){
        return this.mostPlayedArtist.getMostPlayedArtist();
    }

    /**
     * Get most listening time.
     */
    public void getMostListeningTime(){
        this.mostListeningTime.displayTopUser();
    }

    /**
     * Get most listening time.
     *
     * @param initialDate the initial date
     * @param finalDate   the final date
     */
    public void getMostListeningTime(LocalDate initialDate, LocalDate finalDate){
        this.mostListeningTime.displayTopUser(initialDate, finalDate);
    }

    /**
     * Get user leaderboard.
     */
    public void getUserLeaderboard(){
        this.userLeaderBoard.displayTopUser();
    }

    /**
     * Get most played genres.
     */
    public void getMostPlayedGenres(){
        this.mostPlayedGenres.getMostPlayedGenres();
    }

    /**
     * How much public playlists exist.
     *
     * @param publicplaylists    the publicplaylists
     * @param randomPlaylistList the random playlist list
     */
    public void howMuchPublicPlaylistsExist(Map<UUID, Playlist> publicplaylists, List<RandomPlaylist> randomPlaylistList){
        this.howMuchPublicPlaylistsExist.setPublicplaylists(publicplaylists);
        this.howMuchPublicPlaylistsExist.setRandomPlaylists(randomPlaylistList);
        this.howMuchPublicPlaylistsExist.displayPublicPlaylists();
    }

    /**
     * Who have most playlists.
     *
     * @param usersPlaylists the users playlists
     */
    public void whoHaveMostPlaylists(Map<User, List<Playlist>> usersPlaylists){
        this.whoHaveMostPlaylists.setPlaylists(usersPlaylists);
        this.whoHaveMostPlaylists.getWhoHaveMostPlaylists();
    }

    /**
     * Get user history map.
     *
     * @param user the user
     * @return the map
     */
    public Map<Music, Double> getUserHistory(User user){
        return this.userLeaderBoard.getUserHistory(user);
    }
}
