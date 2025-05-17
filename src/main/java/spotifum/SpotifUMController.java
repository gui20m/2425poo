package spotifum;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import com.sun.jdi.InvalidTypeException;
import spotifum.exceptions.*;
import spotifum.menu.*;
import spotifum.musics.Music;
import spotifum.playlists.Playlist;
import spotifum.users.SubscriptionPlans;
import spotifum.views.SpotifUMView;

import static spotifum.utils.DisplayInformation.displayExceptions;

/**
 * The type Spotif umdb.
 */
public class SpotifUMController implements Serializable {
    private SpotifUM spotifum;
    private String email;
    private String username;

    /**
     * Instantiates a new Spotif umdb.
     *
     * @param spotifumView the spotifum view
     */
    public SpotifUMController(SpotifUMView spotifumView) {
        this.spotifum = new SpotifUM(spotifumView);
        this.username="undefined";
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Create user.
     */
    public void createUser(){
        spotifum.createUser(this.email, this.username);
    }

    /**
     * Create user.
     *
     * @param email    the email
     * @param username the username
     * @throws IllegalArgumentException     the illegal argument exception
     * @throws EntityAlreadyExistsException the existing entity exception
     * @throws InvalidTypeException         the invalid type exception
     */
    public void createUser(String email, String username) throws IllegalArgumentException, EntityAlreadyExistsException, InvalidTypeException {
        spotifum.createUser(email, username);
        this.username = username;
    }

    /**
     * Create artist.
     *
     * @param email    the email
     * @param username the username
     * @throws IllegalArgumentException     the illegal argument exception
     * @throws EntityAlreadyExistsException the existing entity exception
     * @throws InvalidTypeException         the invalid type exception
     */
    public void createArtist(String email, String username) throws IllegalArgumentException, EntityAlreadyExistsException, InvalidTypeException {
        spotifum.createArtist(email, username);
        this.username = username;
    }

    /**
     * Remove user.
     */
    public void removeUser(){
        try{
            this.spotifum.removeUser(this.email);
            System.out.println("[app-log] user removed!");
        } catch (EntityNotFoundException e) {
            displayExceptions("user does not exist!");
        }
    }

    /**
     * Search musics map.
     *
     * @param musicName the music name
     * @return the map
     */
    public Map<UUID, Music> searchMusics(String musicName){
        return this.spotifum.searchMusics(musicName);
    }

    /**
     * Remove artist.
     */
    public void removeArtist(){
        try{
            this.spotifum.removeArtist(this.email);
            System.out.println("[app-log] artist removed!");
        } catch (EntityNotFoundException e) {
            displayExceptions("artist does not exist!");
        }
    }


    /**
     * Get all users.
     */
    public void getAllUsers(){
        this.spotifum.getAllUsers();
    }

    /**
     * Get all artists.
     */
    public void getAllArtists(){
        this.spotifum.getAllArtists();
    }

    /**
     * Delete music.
     *
     * @param musicName the music name
     * @param musics    the musics
     */
    public void deleteMusic(String musicName, Map<UUID, Music> musics){
        this.spotifum.deleteMusic(musicName, musics);
    }

    /**
     * Load state.
     *
     * @param fileName the file name
     * @throws FileNotFoundException  the file not found exception
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void loadState(String fileName)  throws FileNotFoundException, IOException, ClassNotFoundException {
            this.spotifum.loadState(fileName);
    }

    /**
     * Get name string.
     *
     * @return the string
     */
    public String getName(){
        return this.username;
    }

    /**
     * Login.
     *
     * @param email the email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void login(String email) throws EntityNotFoundException{
        this.email = email;
        if(this.spotifum.existUserWithEmail(email)){
            this.username = spotifum.getUser(email).getUsername();
        } else throw new EntityNotFoundException(email);
    }

    /**
     * Set user plan.
     *
     * @param plan the plan
     */
    public void setUserPlan(SubscriptionPlans plan){
        this.spotifum.setUserPlan(this.email, plan);
    }

    /**
     * Get user plan subscription plans.
     *
     * @return the subscription plans
     */
    public SubscriptionPlans getUserPlan(){
        return this.spotifum.getUser(this.email).getUserPlan();
    }

    /**
     * Create playlist menu.
     *
     * @param playlistName the playlist name
     * @return the menu
     */
    public Menu createPlaylist(String playlistName){
        return this.spotifum.createPlaylist(this.email, playlistName);
    }

    /**
     * Update playlist menu.
     *
     * @return the menu
     */
    public Menu updatePlaylist(){
        try{
            return this.spotifum.updatePlaylist(this.email);
        } catch (EntityNotFoundException e) {
            displayExceptions("playlist does not exist!");
        }
        return null;
    }

    /**
     * Delete playlist.
     */
    public void deletePlaylist(){
        try {
            this.spotifum.deletePlaylist(this.email);
        } catch (EntityNotFoundException e) {
            displayExceptions("playlist not found!");
        }
    }

    /**
     * Login artist.
     *
     * @param email the email
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void loginArtist(String email) throws IllegalArgumentException{
        this.email = email;
        if(this.spotifum.existArtistWithEmail(email)){
            this.username = spotifum.getArtist(email).getUsername();
        } else throw new IllegalArgumentException(email);
    }

    /**
     * Get user details string.
     *
     * @return the string
     */
    public String getUserDetails(){
        Map<UUID, Playlist> playlists = this.spotifum.getUserPlaylists(this.email);
        this.spotifum.getUser(this.email).setPlaylists(playlists);

        return this.spotifum.getUser(this.email).toString();
    }

    /**
     * Get artist details string.
     *
     * @return the string
     */
    public String getArtistDetails(){
        return this.spotifum.getArtist(this.email).toString();
    }

    /**
     * Update email artist.
     *
     * @param newEmail the new email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void updateEmailArtist(String newEmail) throws EntityNotFoundException {
        this.spotifum.updateEmailArtists(newEmail, this.email);
        this.email = newEmail;
    }

    /**
     * Update username artist.
     *
     * @param username the username
     */
    public void updateUsernameArtist(String username){
        this.spotifum.updateUsernameArtist(username, this.email);
        this.username = username;
    }

    /**
     * Update email.
     *
     * @param newEmail the new email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void updateEmail(String newEmail) throws EntityNotFoundException {
        this.spotifum.updateEmail(newEmail, this.email);
        this.email = newEmail;
    }

    /**
     * Update username.
     *
     * @param username the username
     */
    public void updateUsername(String username){
        this.spotifum.updateUsername(username, this.email);
        this.username = username;
    }

    /**
     * Update address.
     *
     * @param address the address
     */
    public void updateAddress(String address){
        this.spotifum.updateAddress(address, this.email);
    }

    /**
     * Update plan.
     *
     * @param plan the plan
     */
    public void updatePlan(SubscriptionPlans plan){
        this.spotifum.updatePlan(plan, this.email);
    }

    /**
     * Add music int.
     *
     * @param name      the name
     * @param artist    the artist
     * @param label     the label
     * @param lyrics    the lyrics
     * @param partiture the partiture
     * @param genre     the genre
     * @param duration  the duration
     * @param explicit  the explicit
     * @return the int
     */
    public int addMusic(String name, String artist, String label, String lyrics, List<String> partiture,  String genre, int duration, boolean explicit, boolean multimedia, String video){
        return this.spotifum.addMusic(this.email, name, artist, label, lyrics, partiture, genre, duration, explicit, multimedia, video);
    }

    /**
     * Remove music int.
     *
     * @return the int
     */
    public int removeMusic(){
        try{
            this.spotifum.removeMusic(this.email);
            return 1;
        } catch (EntityNotFoundException e) {
            displayExceptions("music not found!");
        }
        return 0;
    }

    /**
     * Register album int.
     *
     * @param albumName the album name
     * @return the int
     */
    public int registerAlbum(String albumName){
        try{
            this.spotifum.registerAlbum(this.email, albumName);
            return 1;
        } catch (EntityNotFoundException e) {
            displayExceptions(e.getMessage());
        }
        return 0;
    }

    /**
     * Remove album int.
     *
     * @return the int
     */
    public int removeAlbum(){
        try{
            this.spotifum.removeAlbum(this.email);
            return 1;
        } catch (EntityNotFoundException e) {
            displayExceptions("album id not found!");
        }

        return 0;
    }

    /**
     * Update album.
     */
    public void updateAlbum(){
        try {
            this.spotifum.updateAlbum(this.email);
        } catch (EntityNotFoundException e) {
            if (e.getMessage()==null) displayExceptions("album not found!");
            else displayExceptions(e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            displayExceptions("music already exists!");
        }
    }

    /**
     * Update music.
     */
    public void updateMusic(){
        try {
            this.spotifum.updateMusic(this.email);
        } catch (EntityNotFoundException e) {
            displayExceptions("music not found!");
        }
    }

    /**
     * Create play songs menu menu.
     *
     * @return the menu
     */
    public Menu createPlaySongsMenu(){
        Menu menu=null;
        try {
            menu = this.spotifum.createPlaySongsMenu(this.email);
        } catch (IllegalArgumentException e) {
            displayExceptions("playlist not found!");
        }
        return menu;
    }

    /**
     * Get most played music.
     */
    public void getMostPlayedMusic(){
        this.spotifum.getMostPlayedMusic();
    }

    /**
     * Get most played artist.
     */
    public void getMostPlayedArtist(){
        this.spotifum.getMostPlayedArtist();
    }

    /**
     * Get most listening time.
     */
    public void getMostListeningTime(){
        this.spotifum.getMostListeningTime();
    }

    /**
     * Get most listening time.
     *
     * @param initialDate the initial date
     * @param finalDate   the final date
     */
    public void getMostListeningTime(LocalDate initialDate, LocalDate finalDate){
        this.spotifum.getMostListeningTime(initialDate, finalDate);
    }

    /**
     * Get user leaderboard.
     */
    public void getUserLeaderboard(){
        this.spotifum.getUserLeaderboard();
    }

    /**
     * Get most played genres.
     */
    public void getMostPlayedGenres(){
        this.spotifum.getMostPlayedGenres();
    }

    /**
     * How much public playlists exist.
     */
    public void howMuchPublicPlaylistsExist(){
        this.spotifum.howMuchPublicPlaylistsExist();
    }

    /**
     * Who have most playlists.
     */
    public void whoHaveMostPlaylists(){
        this.spotifum.whoHaveMostPlaylists();
    }

    /**
     * Save state.
     *
     * @param fileName the file name
     * @throws FileNotFoundException the file not found exception
     */
    public void saveState(String fileName) throws FileNotFoundException {
        this.spotifum.saveState(fileName);
    }

    /**
     * Get current date.
     */
    public void getCurrentDate(){
        this.spotifum.getCurrentDate();
    }

    /**
     * Reset time.
     */
    public void resetTime(){
        this.spotifum.resetDate();
    }

    /**
     * Time simulation.
     *
     * @param days the days
     */
    public void timeSimulation(int days){
        this.spotifum.timeSimulation(days);
    }

    /**
     * Generate playlist.
     *
     * @param maxsongs       the maxsongs
     * @param maxtime        the maxtime
     * @param explicitmusics the explicitmusics
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void generatePlaylist(int maxsongs, int maxtime, boolean explicitmusics) throws IllegalArgumentException{
        this.spotifum.generatePlaylist(this.email, maxsongs, maxtime, explicitmusics);
    }
}
