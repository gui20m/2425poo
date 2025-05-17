package spotifum;

import spotifum.exceptions.*;
import spotifum.musics.*;
import spotifum.playlists.*;
import spotifum.statistics.StatisticsManager;
import spotifum.users.*;

import java.time.LocalDate;
import java.util.*;
import java.io.*;

import spotifum.menu.*;
import spotifum.views.SpotifUMView;

import static spotifum.utils.DisplayInformation.displayExceptions;
import static spotifum.utils.DisplayInformation.*;

/**
 * The type Spotif um.
 */
public class SpotifUM implements Serializable {
    private UserManager usermanager;
    private ArtistManager artistmanager;
    private StatisticsManager statisticsManager;
    private SpotifUMView view;
    private Music currentPlayingMusic;
    private Music previousPlayingMusic;
    private LocalDate date;
    private List<RandomPlaylist> randomPlaylists;

    /**
     * Instantiates a new Spotif um.
     *
     * @param spotifumView the spotifum view
     */
    public SpotifUM(SpotifUMView spotifumView) {
        this.usermanager = new UserManager();
        this.artistmanager = new ArtistManager();
        this.statisticsManager = new StatisticsManager(usermanager, artistmanager);
        this.randomPlaylists = new  ArrayList<>();
        this.view = spotifumView;
        this.currentPlayingMusic = null;
        this.previousPlayingMusic= null;
        this.date = LocalDate.now();
    }

    /**
     * Reset date.
     */
    public void resetDate(){
        this.date = LocalDate.now();
        getCurrentDate();
    }

    /**
     * Set current playing music.
     *
     * @param music the music
     */
    public void setCurrentPlayingMusic(Music music){
        this.currentPlayingMusic = music;
    }

    /**
     * Set previous playing music.
     *
     * @param music the music
     */
    public void setPreviousPlayingMusic(Music music){
        this.previousPlayingMusic = music;
    }

    /**
     * Time simulation.
     *
     * @param days the days
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void timeSimulation(int days) throws IllegalArgumentException{
        if (days <0){
            throw new IllegalArgumentException("[app-log] days must be greater than zero!");
        }
        this.date = this.date.plusDays(days);
    }

    /**
     * Gets current date.
     */
    public void getCurrentDate() {
        displayCurrentDate(this.date);
    }

    /**
     * Create user.
     *
     * @param email    the email
     * @param username the username
     */
    public void createUser(String email, String username){
        User user = this.usermanager.createUser(email, username);
        this.usermanager.insertUser(user);
    }

    /**
     * Remove user.
     *
     * @param email the email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void removeUser(String email) throws EntityNotFoundException {
        User u = getUser(email);
        this.statisticsManager.removeUser(u);
        this.usermanager.removeUser(email);
    }

    /**
     * Remove artist.
     *
     * @param email the email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void removeArtist(String email) throws EntityNotFoundException {
        Artist artist = getArtist(email);
        this.artistmanager.removeArtist(email);
        Map<UUID, Music> tracks = artist.getTracks();
        this.usermanager.removeTracksFromPlaylists(tracks);
    }

    /**
     * Search musics map.
     *
     * @param musicName the music name
     * @return the map
     */
    public Map<UUID, Music> searchMusics(String musicName){
        return this.artistmanager.searchMusics(musicName);
    }

    /**
     * Delete music.
     *
     * @param musicName the music name
     * @param musics    the musics
     */
    public void deleteMusic(String musicName, Map<UUID, Music> musics){
        do{
            int n = displaySearchResults(musicName, musics);
            if (n==0) return;

            System.out.print("➤ remove music id [\"q\" to leave]: ");
            String input = new Scanner(System.in).nextLine();
            if (input.equalsIgnoreCase("q")) break;
            try {
                UUID selectedId = UUID.fromString(input);
                Music selectedMusic = musics.get(selectedId);

                if (selectedMusic != null) {
                    this.artistmanager.deleteMusic(selectedMusic);
                    this.usermanager.deleteMusicFromPlaylists(selectedMusic);
                    System.out.println("[app-log] music successfully removed!");
                    break;
                }
            } catch (IllegalArgumentException e) {
                displayExceptions("invalid music id!");
            }
        } while(true);
    }

    /**
     * Get all users.
     */
    public void getAllUsers(){
        this.usermanager.getAllUsers();
    }

    /**
     * Get all artists.
     */
    public void getAllArtists(){
        this.artistmanager.getAllArtists();
    }

    /**
     * Create artist.
     *
     * @param email    the email
     * @param username the username
     */
    public void createArtist(String email, String username){
        Artist artist = this.artistmanager.createArtist(email, username);
        this.artistmanager.insertArtist(artist);
    }

    /**
     * Update email artists.
     *
     * @param newEmail the new email
     * @param email    the email
     */
    public void updateEmailArtists(String newEmail, String email){
        Artist artist = getArtist(email);
        artist.setEmail(newEmail);
        this.artistmanager.updateArtistEmail(artist, newEmail, email);
        System.out.println(artist.toString());
    }

    /**
     * Update username artist.
     *
     * @param username the username
     * @param email    the email
     */
    public void updateUsernameArtist(String username, String email){
        Artist artist = getArtist(email);
        artist.setUsername(username);
        this.artistmanager.updateArtist(artist);
        System.out.println(artist.toString());
    }

    /**
     * Update email.
     *
     * @param newEmail the new email
     * @param email    the email
     */
    public void updateEmail(String newEmail, String email){
        User user = getUser(email);
        user.setEmail(newEmail);
        this.usermanager.updateUserEmail(user, newEmail, email);
        System.out.println(user.toString());
    }

    /**
     * Update username.
     *
     * @param username the username
     * @param email    the email
     */
    public void updateUsername(String username, String email){
        User u = getUser(email);
        u.setUsername(username);
        this.usermanager.updateUser(u);
        System.out.println(u.toString());
    }

    /**
     * Update address.
     *
     * @param address the address
     * @param email   the email
     */
    public void updateAddress(String address, String email){
        User u = getUser(email);
        u.setAddress(address);
        this.usermanager.updateUser(u);
        System.out.println(u.toString());
    }

    /**
     * Set user plan.
     *
     * @param email the email
     * @param plan  the plan
     */
    public void setUserPlan(String email, SubscriptionPlans plan){
        User user = getUser(email);
        user.setUserPlan(plan);
        this.usermanager.updateUser(user);
    }

    /**
     * Update plan.
     *
     * @param plan  the plan
     * @param email the email
     */
    public void updatePlan(SubscriptionPlans plan, String email) {
        User user = getUser(email);
        User upgradedUser = null;

        if (SubscriptionPlans.PremiumBase.equals(plan)) {
            if (user instanceof FreeUser || user instanceof PremiumTopUser) {
                upgradedUser = new PremiumBaseUser(user);
            }
        } else if (SubscriptionPlans.PremiumTop.equals(plan)) {
            if (user instanceof FreeUser || user instanceof PremiumBaseUser) {
                upgradedUser = new PremiumTopUser(user);
            }
        } else if (SubscriptionPlans.Free.equals(plan)){
            if (user instanceof PremiumBaseUser || user instanceof PremiumTopUser) {
                upgradedUser = new FreeUser(user);
            }
        }

        if (upgradedUser != null) {
            this.usermanager.updateUser(upgradedUser);
            System.out.println("["+user.getUsername()+"] successfully upgraded plan to "+plan.toString()+"!");
            System.out.println(upgradedUser.toString());
        } else {
            System.out.println("[app-log] upgrade not allowed from current plan!");
        }
    }

    /**
     * Gets user playlists.
     *
     * @param email the email
     * @return the user playlists
     */
    public Map<UUID, Playlist> getUserPlaylists(String email) {
        Map<UUID, Playlist>  playlists = new HashMap<>();
        User user = getUser(email);
        if (user instanceof FreeUser || user instanceof PremiumBaseUser || user instanceof PremiumTopUser) {
            playlists = user.getPlaylists();
        }
        return playlists;
    }

    /**
     * Create playlist menu.
     *
     * @param email        the email
     * @param playlistName playlist name
     * @return the menu
     */
    public Menu createPlaylist(String email, String playlistName){
        User user = getUser(email.toLowerCase());

        PremiumPlaylist playlist = new PremiumPlaylist(playlistName, user.getUsername());
        playlist.setOwner(user.getUsername());
        if (user instanceof PremiumBaseUser) {
            user.createPlaylist(playlist);
        }
        System.out.println(playlist.toString());
        Menu playlistMenu = createPlaylistMenu(user, playlist);

        return playlistMenu;
    }

    /**
     * Create playlist menu menu.
     *
     * @param user     the user
     * @param playlist the playlist
     * @return the menu
     * @throws EntityNotFoundException the entity not found exception
     */
    public Menu createPlaylistMenu(User user, PremiumPlaylist playlist) throws EntityNotFoundException {
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("change name", ()->changePlaylistName(user,playlist)));
        items.add(new MenuItem("add music", ()->addMusicToPlaylist(user, playlist)));
        items.add(new MenuItem("add music at index", ()->addMusicToPlaylistAtIndex(user, playlist)));
        items.add(new MenuItem("add album", ()->addAlbumToPlaylist(user, playlist)));
        items.add(new MenuItem("remove music", ()->removeMusicFromPlaylist(user, playlist)));
        items.add(new MenuItem("udpate visiblity", ()->updateVisiblity(user, playlist)));

        return new Menu(items, true);

    }

    /**
     * Changes the name of a playlist for a given user.
     *
     * @param user     the user who owns the playlist
     * @param playlist the playlist to be renamed
     */
    private void changePlaylistName(User user, Playlist playlist){
        System.out.println(playlist.toString());
        System.out.print("➤ new name: ");
        Scanner scanner = new Scanner(System.in);
        String playlistName = scanner.nextLine();
        playlist.setName(playlistName);
        user.updatePlaylist(playlist);
        this.usermanager.updateUser(user);
    }

    /**
     * Adds an album to a premium playlist after displaying options.
     *
     * @param user     the user who owns the playlist
     * @param playlist the target playlist
     */
    private void addAlbumToPlaylist(User user, Playlist playlist) {
        System.out.print("➤ album name: ");
        Scanner scanner = new Scanner(System.in);
        String albumName = scanner.nextLine();
        Map<UUID, Album> albums = this.artistmanager.searchAlbuns(albumName);
        int n = displayAlbums(albums, albumName);
        if (n==0) return;
        System.out.print("➤ add album id [\"q\" to leave]: ");
        String addAlbumId = scanner.nextLine();
        if (addAlbumId.equalsIgnoreCase("q")) return;
        try{
            UUID selectedId = UUID.fromString(addAlbumId);
            Album selectedAlbum = albums.get(selectedId);
            if (selectedAlbum != null && playlist instanceof PremiumPlaylist){
                playlist.addAlbum(selectedAlbum);
                System.out.println("[app] album successfully added!");
            }
        } catch (IllegalArgumentException e){
            displayExceptions("invalid album id!");
        }
        user.updatePlaylist(playlist);
        this.usermanager.updateUser(user);
        System.out.println(playlist.toString());
    }

    /**
     * Adds a music track to a premium playlist.
     *
     * @param user     the user who owns the playlist
     * @param playlist the target playlist
     */
    private void addMusicToPlaylist(User user, Playlist playlist) {
        System.out.print("➤ music name: ");
        String musicName = new Scanner(System.in).nextLine();

        Map<UUID, Music> musics = this.artistmanager.searchMusics(musicName);

        int n = displaySearchResults(musicName, musics);
        if (n==0) return;

        System.out.print("➤ add music id [\"q\" to leave]: ");
        String input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("q")) return;
        try {
            UUID selectedId = UUID.fromString(input);
            Music selectedMusic = musics.get(selectedId);

            if (selectedMusic != null && playlist instanceof PremiumPlaylist) {
                playlist.addMusic(selectedMusic);
                System.out.println("[app-log] music successfully added!");
            }
        } catch (IllegalArgumentException e) {
            displayExceptions("invalid music id!");
        }
        user.updatePlaylist(playlist);
        this.usermanager.updateUser(user);
        System.out.println(playlist.toString());
    }

    /**
     * Adds a music track to a specific index in a premium playlist.
     *
     * @param user     the user who owns the playlist
     * @param playlist the target playlist
     */
    private void addMusicToPlaylistAtIndex(User user, Playlist playlist) {
        System.out.print("➤ music name: ");
        String musicName = new Scanner(System.in).nextLine();

        Map<UUID, Music> musics = this.artistmanager.searchMusics(musicName);

        int n = displaySearchResults(musicName, musics);
        if (n==0) return;

        System.out.print("➤ add music id [\"q\" to leave]: ");
        String input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("q")) return;
        System.out.print("➤ index to add music [0+]: ");
        int  index = new Scanner(System.in).nextInt();
        try {
            UUID selectedId = UUID.fromString(input);
            Music selectedMusic = musics.get(selectedId);

            if (selectedMusic != null && index>=0 && playlist instanceof PremiumPlaylist) {
                playlist.addMusicToIndex(selectedMusic, index);
                System.out.println("[app-log] music successfully added!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[app-log] invalid uuid format!");
        }
        user.updatePlaylist(playlist);
        this.usermanager.updateUser(user);
        System.out.println(playlist.toString());
    }

    /**
     * Removes a music track from a playlist using its UUID.
     *
     * @param user     the user who owns the playlist
     * @param playlist the target playlist
     */
    private void removeMusicFromPlaylist(User user, Playlist playlist) throws EntityNotFoundException {
        List<Music> musics = playlist.getTracks();
        int n = displayPlaylistAvailableTracks(playlist);
        if (n==0) return;
        System.out.print("➤ music id to remove [\"q\" to leave]: ");
        Scanner scanner = new Scanner(System.in);
        String musicId = scanner.nextLine().trim();

        if (musicId.equalsIgnoreCase("q")) {
            return;
        }

        UUID uuidToRemove = UUID.fromString(musicId);
        boolean removed = false;

        for (Music music : new ArrayList<>(musics)) {
            if (music != null && uuidToRemove.equals(music.getMid())) {
                playlist.removeMusic(music);
                removed = true;
                break;
            }
        }

        if (removed) {
            user.updatePlaylist(playlist);
            this.usermanager.updateUser(user);
            System.out.println("[app-log] music removed successfully!");
            System.out.println(playlist.toString());
        } else throw new EntityNotFoundException();
    }

    /**
     * Update playlist menu.
     *
     * @param email the email
     * @return the menu
     * @throws EntityNotFoundException the entity not found exception
     */
    public Menu updatePlaylist(String email) throws EntityNotFoundException{
        User user = getUser(email.toLowerCase());
        if (user instanceof PremiumBaseUser || user instanceof PremiumTopUser) {
            Map<UUID, Playlist> playlists = user.getPlaylists();
            displayUserPlaylists(user, playlists);
            System.out.print("➤ playlist to manage [\"q\" to leave]: ");
            Scanner scanner = new Scanner(System.in);
            String playlistid = scanner.nextLine();
            Playlist playlist;
            if (!playlistid.equalsIgnoreCase("q")) {
                playlist = (Playlist) playlists.get(UUID.fromString(playlistid));
            } else {
                return null;
            }
            Playlist finalPlaylist = playlist;
            if (finalPlaylist!=null) {
                System.out.println(finalPlaylist.toString());
                Menu menu = createPlaylistMenu(user, (PremiumPlaylist)playlist);
                return menu;
            }
        }
        return null;
    }

    /**
     * Updates the visibility of a playlist (public or private) based on user input.
     *
     * @param user     the user who owns the playlist
     * @param playlist the playlist to update
     */
    private void updateVisiblity(User user, Playlist playlist) {
        System.out.println(playlist.toString());
        Scanner scanner = new Scanner(System.in);
        String visibility;
        do{
            System.out.print("➤ public playlist [y/n | \"q\" to leave]: ");
            visibility = scanner.nextLine();
        } while(!visibility.equalsIgnoreCase("y") && !visibility.equalsIgnoreCase("n"));
        if (visibility.equalsIgnoreCase("q")) return;
        if (visibility.equalsIgnoreCase("y"))
            playlist.setPublic(true);
        if (visibility.equalsIgnoreCase("n"))
            playlist.setPublic(false);

        this.usermanager.updateUser(user);
        System.out.println(playlist.toString());
    }

    /**
     * Delete playlist.
     *
     * @param email the email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void deletePlaylist(String email) throws EntityNotFoundException {
        User user = getUser(email.toLowerCase());
        if (user instanceof PremiumBaseUser || user instanceof PremiumTopUser) {
            Map<UUID, Playlist> playlists = user.getPlaylists();
            displayUserPlaylists(user, playlists);
            System.out.print("➤ playlist id to remove [\"q\" to leave]: ");
            Scanner scanner = new Scanner(System.in);
            String playlistId = scanner.nextLine();

            if (playlistId.equalsIgnoreCase("q")) return;
            if (!playlistId.matches("[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")) {
                throw new EntityNotFoundException("invalid playlist id!");
            }
            if (playlists.remove(UUID.fromString(playlistId))==null) throw new EntityNotFoundException();
            user.setPlaylists(playlists);
            this.usermanager.updateUser(user);
            System.out.println(user.toString());
        }
    }

    /**
     * Remove music int.
     *
     * @param email the email
     * @return the int
     * @throws EntityNotFoundException the entity not found exception
     */
    public int removeMusic(String email) throws EntityNotFoundException{
        Artist artist = this.artistmanager.getArtist(email.toLowerCase());
        if (artist == null) {
            System.out.println("[app] artist not found!");
            return 0;
        }

        displayAvailableTracks(artist, null,0, 0);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("➤ remove music id [\"q\" to leave]: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                return 0;
            }

                UUID musicId = UUID.fromString(input);
                Music musicToRemove = artist.getTracks().get(musicId);

                if (musicToRemove != null) {
                    artist.removeMusic(musicToRemove);
                    this.artistmanager.updateArtist(artist);
                    System.out.println(artist.toString());
                    return 1;
                } else throw new EntityNotFoundException();
        }
    }

    /**
     * Register album int.
     *
     * @param email the email
     * @param album the album
     * @return the int
     * @throws EntityNotFoundException the entity not found exception
     */
    public int registerAlbum(String email, String album) throws EntityNotFoundException{
        Artist artist = this.artistmanager.getArtist(email.toLowerCase());
        Album al = new Album(album, artist.getUsername());
        artist.addAlbum(al);
        this.artistmanager.updateArtist(artist);
        System.out.println(artist.toString());
        String id;
        addTrack(artist, al);
        return 1;
    }

    /**
     * Remove album int.
     *
     * @param email the email
     * @return the int
     * @throws EntityNotFoundException the entity not found exception
     */
    public int removeAlbum(String email) throws EntityNotFoundException {
        Artist artist = this.artistmanager.getArtist(email.toLowerCase());
        displayAvailableAlbums(artist);
        Scanner scanner = new Scanner(System.in);
        String id;
        System.out.print("➤ remove album id [\"q\" to leave]: ");
        id = scanner.nextLine();

        if (!id.matches("[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")) {
            throw new EntityNotFoundException();
        }
        artist.removeAlbum(UUID.fromString(id));

        this.artistmanager.updateArtist(artist);
        return 1;
    }

    /**
     * Update music.
     *
     * @param email the email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void updateMusic(String email) throws EntityNotFoundException {
        Artist artist = this.artistmanager.getArtist(email.toLowerCase());
        displayAvailableTracks(artist, null,0, 0);
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ update music id [\"q\" to leave]: ");
        String id = scanner.nextLine();
        if (id.equalsIgnoreCase("q")) return;
        Music music = artist.getTracks().get(UUID.fromString(id));
        if (music!=null){
            System.out.println(music.toString());
            Menu musicParams = createMusicUpdateParams(music);
            musicParams.run();
            System.out.println(music.toString());
            this.artistmanager.updateArtist(artist);
        } else throw new EntityNotFoundException();
    }

    /**
     * Creates a menu with options to update parameters of a given music object.
     *
     * @param music the music object to update
     * @return a menu containing editable music fields
     */
    private Menu createMusicUpdateParams(Music music){
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("edit name", ()->editName(music)));
        items.add(new MenuItem("edit label", ()->editLabel(music)));
        items.add(new MenuItem("edit lyrics", ()->editLyrics(music)));
        items.add(new MenuItem("edit partiture", ()->editPartiture(music)));
        items.add(new MenuItem("edit genre", ()->editGenre(music)));

        return new Menu(items, true);
    }

    /**
     * Edits the name of a music track based on user input.
     *
     * @param music the music object whose name will be updated
     */
    private void editName(Music music){
        System.out.print("➤ new name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        music.setName(name);
        System.out.println(music.toString());
    }

    /**
     * Edits the label of a music track based on user input.
     *
     * @param music the music object whose label will be updated
     */
    private void editLabel(Music music){
        System.out.print("➤ new label: ");
        Scanner scanner = new Scanner(System.in);
        String label = scanner.nextLine();
        music.setLabel(label);
        System.out.println(music.toString());
    }

    /**
     * Edits the lyrics of a music track based on user input.
     *
     * @param music the music object whose lyrics will be updated
     */
    private void editLyrics(Music music){
        System.out.print("➤ new lyrics: ");
        Scanner scanner = new Scanner(System.in);
        String lyrics = scanner.nextLine();
        music.setLyrics(lyrics);
        System.out.println(music.toString());
    }

    /**
     * Edits the partiture (musical notation) of a music track based on user input.
     * Keeps collecting lines until the user enters "q".
     *
     * @param music the music object whose partiture will be updated
     */
    private void editPartiture(Music music){
        Scanner scanner = new Scanner(System.in);
        String partiture;
        List<String> newpartiture = new ArrayList<>();
        do {
            System.out.print("➤ new partiture: ");
            partiture = scanner.nextLine();
        } while (!partiture.equalsIgnoreCase("q"));
        music.setMusic(newpartiture);
        System.out.println(music.toString());
    }

    /**
     * Edits the genre of a music track based on user input.
     *
     * @param music the music object whose genre will be updated
     */
    private void editGenre(Music music){
        System.out.print("➤ new genre: ");
        Scanner scanner = new Scanner(System.in);
        String genre = scanner.nextLine();
        music.setGenre(genre);
        System.out.println(music.toString());
    }

    /**
     * Update album.
     *
     * @param email the email
     * @throws EntityNotFoundException      the entity not found exception
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    public void updateAlbum(String email) throws EntityNotFoundException, EntityAlreadyExistsException {
        Artist artist = this.artistmanager.getArtist(email.toLowerCase());
        displayAvailableAlbums(artist);
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ update album id: ");
        String id = scanner.nextLine();
        if (id.equalsIgnoreCase("q")) return;
        Album album = artist.getAlbums().get(UUID.fromString(id));
        if (album != null) {
            System.out.println(album.toString());
            Menu albumParams = createAlbumUpdateParams(artist, album);
            albumParams.run();
            System.out.println(album.toString());
            artist.updateAlbum(album);
            this.artistmanager.updateArtist(artist);
        } else throw new EntityNotFoundException();
    }

    /**
     * Creates a menu with options to update album parameters such as name and tracks.
     *
     * @param artist the artist who owns the album
     * @param album the album to be updated
     * @return a menu containing editable album fields
     */
    private Menu createAlbumUpdateParams(Artist artist, Album album) throws EntityNotFoundException, EntityAlreadyExistsException {
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("edit name", ()->updateAlbumName(album)));
        items.add(new MenuItem("edit tracks", ()->updateAlbumTracks(artist, album)));

        return new Menu(items, true);
    }

    /**
     * Updates the name of an album based on user input.
     *
     * @param album the album whose name will be updated
     */
    private void updateAlbumName(Album album){
        System.out.print("➤ new name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        album.setName(name);
    }

    /**
     * Update album tracks.
     *
     * @param artist the artist
     * @param album  the album
     * @throws EntityNotFoundException      the entity not found exception
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    public void updateAlbumTracks(Artist artist, Album album) throws EntityNotFoundException, EntityAlreadyExistsException {
        System.out.println(album.toString());
        Menu tracksMenu = createTracksMenu(artist, album);
        tracksMenu.run();
        this.artistmanager.updateArtist(artist);
        System.out.println(album.toString());
    }

    /**
     * Creates a menu with options to add or remove tracks from an album.
     *
     * @param artist the artist who owns the album
     * @param album the album to be updated
     * @return a menu with track modification options
     */
    private Menu createTracksMenu(Artist artist, Album album) throws EntityNotFoundException, EntityAlreadyExistsException {
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("add track", ()->addTrack(artist, album)));
        items.add(new MenuItem("add track in index", ()->addTrackInIndex(artist, album)));
        items.add(new MenuItem("remove track", ()->removeTrack(artist, album)));

        return new Menu(items, true);
    }

    /**
     * Allows the user to add one or more tracks to an album by selecting track IDs.
     *
     * @param artist the artist who owns the tracks
     * @param album the album to which tracks will be added
     */
    private void addTrack(Artist artist, Album album) throws EntityNotFoundException, EntityAlreadyExistsException {
        Scanner scanner = new Scanner(System.in);
        String response;
        do {
            displayAvailableTracks(artist, album, 1, 0);
            System.out.print("➤ add track id [\"q\" to leave]: ");
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("q")) break;
            try {
                Music music = artist.getTracks().get(UUID.fromString(response));
                if (music == null) throw new EntityNotFoundException();
                album.addMusic(music);
                artist.addAlbum(album);
                this.artistmanager.updateArtist(artist);
            } catch (Exception e) {
                throw new EntityNotFoundException("music not found!");
            }
        } while(true);
        System.out.println(album.toString());
    }

    /**
     * Allows the user to add a track to a specific index in the album's track list.
     *
     * @param artist the artist who owns the tracks
     * @param album the album to which the track will be inserted
     */
    private void addTrackInIndex(Artist artist, Album album) throws EntityNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String response;
        displayAvailableTracks(artist, album, 1, 0);
        System.out.print("➤ add track id [\"q\" to leave]: ");
        response = scanner.nextLine();
        if (response.equalsIgnoreCase("q")) return;
        System.out.print("➤ index to add [0+ | \"q\" to leave]: ");
        String index = scanner.nextLine();
        if (index.equalsIgnoreCase("q")) return;
        int idx = Integer.parseInt(index);
        Music music = artist.getTracks().get(UUID.fromString(response));
        if (music==null) throw new EntityNotFoundException();
        album.addMusicAtIndex(music, idx);
        System.out.println(album.toString());
    }

    /**
     * Allows the user to remove tracks from an album by providing track IDs.
     *
     * @param artist the artist who owns the album
     * @param album the album from which tracks will be removed
     */
    private void removeTrack(Artist artist, Album album) throws EntityNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            displayAvailableTracks(artist, album, 1, 1);
            System.out.print("➤ remove track id [\"q\" to leave]: ");
            input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                break;
            }
            UUID trackId = UUID.fromString(input);
            boolean removed = album.getMusics().removeIf(m -> m.getMid().equals(trackId));
            if (!removed) {
                throw new EntityNotFoundException();
            }

        } while (true);
        System.out.println(album.toString());
    }

    /**
     * Create play songs menu.
     *
     * @param email the email
     * @return the menu
     * @throws EntityNotFoundException the entity not found exception
     */
    public Menu createPlaySongsMenu(String email) throws EntityNotFoundException {
        User user = getUser(email.toLowerCase());

        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("select random playlist", ()-> getRandomPlaylists(user)));
        if (user instanceof PremiumBaseUser || user instanceof PremiumTopUser) {
            items.add(new MenuItem("select my playlists", ()-> getPremiumPlaylists(user)));
            items.add(new MenuItem("search music", ()->playIndividualSong(user)));
            items.add(new MenuItem("search public playlists", ()->playPublicPlaylists(user)));
        }
        items.add(new MenuItem("resume playing", ()->previousPlayingMusic!=null, ()->resumeSong()));
        items.add(new MenuItem("stop playing", ()->currentPlayingMusic!=null, ()->stopSong()));
        items.add(new MenuItem("exit", () -> {
            System.out.println("[app] CURRENTLY PLAYING MUSIC");
            if (this.currentPlayingMusic != null) {
                System.out.println(this.currentPlayingMusic.toString());
            } else System.out.println("[app] currently aint playing any music");
            if (view.getPreviousMenu() != null) {
                view.setCurrentMenu(view.getPreviousMenu());
            }
        }));

        return new Menu(items, false);
    }

    /**
     * Allows the user to search and play a public playlist by name.
     * Prompts for a playlist ID after search results are shown.
     *
     * @param user the user requesting to play a public playlist
     */
    private void playPublicPlaylists(User user) throws EntityNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("➤ search playlist name [\"q\" to leave]: ");
        String playlistName = scanner.nextLine().trim();
        if (playlistName.equalsIgnoreCase("q")) return;

        System.out.println("[app] PUBLIC PLAYLISTS FOUND");
        Map<UUID, Playlist> publicPlaylists = this.usermanager.getPublicPlaylists(user, playlistName);
        while (true) {
            int n = displayPublicPlaylists(publicPlaylists, user, playlistName);
            if (n == 0) return;
            try {
                System.out.print("➤ playlist id to play [\"q\" to leave]: ");
                String playlistId = scanner.nextLine().trim();
                if (playlistId.equalsIgnoreCase("q")) {
                    return;
                }
                if (!playlistId.matches("[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")) {
                    displayExceptions("invalid  playlist id");
                    continue;
                }
                UUID uuid = UUID.fromString(playlistId);
                if (!publicPlaylists.containsKey(uuid)) {
                    displayExceptions("playlist not found!");
                    continue;
                }
                Menu playlistMenu = createManagePlaylist(user, publicPlaylists.get(uuid));
                playlistMenu.run();
                break;
            } catch (Exception e) {
                throw new EntityNotFoundException();
            }
        }
    }

    /**
     * Allows the user to search for a song and play it by entering the track's ID.
     * Plays the selected song and records playback statistics.
     *
     * @param user the user who is playing the song
     */
    private void playIndividualSong(User user) throws EntityNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("➤ music name: ");
        String musicName = scanner.nextLine().trim();
        Map<UUID, Music> musics = this.artistmanager.searchMusics(musicName);

        while (true) {
            int n = displaySearchResults(musicName, musics);
            if (n == 0) return;
            try {
                System.out.print("➤ play music id [\"q\" to leave]: ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("q")) {
                    return;
                }

                UUID selectedId = UUID.fromString(input);
                Music selectedMusic = musics.get(selectedId);

                this.currentPlayingMusic = selectedMusic;
                this.statisticsManager.recordPlay(user, selectedMusic);
                this.statisticsManager.recordPlay(selectedMusic);
                this.statisticsManager.recordPlay(user, this.date);

                Artist artist = this.artistmanager.searchArtist(selectedMusic.getArtist(), selectedMusic.getMid());
                if (artist != null) {
                    this.statisticsManager.recordPlay(artist);
                }

                System.out.println("[app] CURRENTLY PLAYING MUSIC");
                System.out.println(this.currentPlayingMusic.toString());
                break;

            } catch (Exception e) {
                displayExceptions("music not found!");
            }
        }
    }

    /**
     * Opens and displays the user's premium playlists in a selectable menu.
     *
     * @param user the user whose premium playlists will be accessed
     */
    private void getPremiumPlaylists(User user){
        Menu myplaylists = createMyPlaylists(user);
        myplaylists.run();
    }

    /**
     * Creates a menu of the user's premium playlists, allowing further interaction with each.
     *
     * @param user the user whose playlists will be converted into menu options
     * @return a Menu object containing the user's premium playlists
     */
    private Menu createMyPlaylists(User user){
        List<MenuItem> items =  new ArrayList<>();
        for (Playlist playlist : user.getPlaylists().values()) {
            items.add(new MenuItem(playlist.getName(), ()->createManagePlaylist(user, (PremiumPlaylist)playlist).run()));
        }
        return new Menu(items, true);
    }

    /**
     * Allows the user to select and play a random playlist by ID.
     * Displays the playlist and starts playback of its content.
     *
     * @param user the user requesting to play a random playlist
     */
    private void getRandomPlaylists(User user) {
        System.out.println("[app] playing musics..");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayRandomPlaylists(this.randomPlaylists);
            System.out.print("➤ select playlist id [\"q\" to leave]: ");
            String playlistId = scanner.nextLine().trim();
            if (playlistId.equalsIgnoreCase("q")) {
                return;
            }
            try {
                UUID uuid = UUID.fromString(playlistId);
                RandomPlaylist playlist = null;
                for (RandomPlaylist randomplaylist : this.randomPlaylists) {
                    if (randomplaylist.getId().equals(uuid)) {
                        playlist = randomplaylist;
                        break;
                    }
                }
                if (playlist != null) {
                    System.out.println("[app] PLAYLIST SELECTED: " + playlist.getName());
                    System.out.println(playlist.toString());
                    Menu manageplaylist = createManagePlaylist(user, playlist);
                    manageplaylist.run();
                    System.out.println("[app] CURRENTLY PLAYING MUSIC");
                    if (this.currentPlayingMusic != null) {
                        System.out.println(this.currentPlayingMusic.toString());
                    } else {
                        System.out.println("[app] currently aint playing any music");
                    }
                    return;
                }
            } catch (IllegalArgumentException e) {
                displayExceptions("playlist not found!");
            }
        }
    }

    /**
     * Creates a menu for managing a playlist. The available options vary based on the playlist type.
     * Supports RandomPlaylist and PremiumPlaylist types.
     *
     * @param user the user interacting with the playlist
     * @param playlist the playlist to be managed
     * @return a Menu object with the appropriate management options
     */
    private Menu createManagePlaylist(User user, Playlist playlist){
        if (playlist instanceof RandomPlaylist) {
            List<MenuItem> items = new ArrayList<>();

            items.add(new MenuItem("see playlist", () -> System.out.println(playlist.toString())));
            items.add(new MenuItem("start playing", () -> playSong(user, playlist)));
            items.add(new MenuItem("stop playing", () -> stopSong()));

            return new Menu(items, true);
        }
        if (playlist instanceof PremiumPlaylist){
            List<MenuItem> items = new ArrayList<>();

            items.add(new MenuItem("see playlist", () -> System.out.println(playlist.toString())));
            items.add(new MenuItem("start playing", () -> playSong(user, playlist)));
            items.add(new MenuItem("next track", () -> playNextSong(user, playlist)));
            items.add(new MenuItem("previous track", () -> playPreviousSong(user,playlist)));
            items.add(new MenuItem("play music at index", () -> chooseMusic(user, playlist)));
            items.add(new MenuItem("set shuffle mode", ()->defineShuffleMode(user, playlist)));
            items.add(new MenuItem("stop playing", () -> stopSong()));

            return new Menu(items, true);
        }
        return null;
    }

    /**
     * Allows the user to activate or deactivate shuffle mode for a premium playlist.
     * If the playlist is not owned by the user, a personalized copy is created.
     *
     * @param user the user updating the shuffle setting
     * @param playlist the premium playlist to modify
     */
    private void defineShuffleMode(User user, Playlist playlist){
        System.out.println("[app] defining shuffle mode..");
        displayShuffleMode(((PremiumPlaylist)playlist));
        Scanner scanner = new Scanner(System.in);
        String aw;
        do{
            System.out.print("➤ shuffle mode [y/n]: ");
            aw =  scanner.nextLine();
        } while (!aw.equalsIgnoreCase("y") && !aw.equalsIgnoreCase("n"));
        boolean wantToShuffle = aw.equalsIgnoreCase("y");
        if (!user.getUsername().equals(playlist.getOwner())) {
            playlist = new PremiumPlaylist((PremiumPlaylist)playlist, user.getUsername());
        }
        ((PremiumPlaylist) playlist).setShuffleMode(wantToShuffle);
        displayShuffleMode(((PremiumPlaylist)playlist));
        this.usermanager.updateUser(user);
    }

    /**
     * Allows the user to choose and play a specific track from a premium playlist using its index.
     * Displays the currently playing track and updates statistics.
     *
     * @param user the user playing the song
     * @param playlist the premium playlist from which to select the track
     */
    private void chooseMusic(User user, Playlist playlist){
        displayPlaylistIndexs(playlist);
        System.out.print("➤ playing index: ");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();
        Music nowplaying = ((PremiumPlaylist)playlist).jumpToTrack(index);
        if (nowplaying!=null){
            System.out.println("[app] CURRENTLY PLAYING MUSIC");
            this.statisticsManager.recordPlay(user,nowplaying);
            this.statisticsManager.recordPlay(nowplaying);
            this.statisticsManager.recordPlay(user, this.date);
            Artist artist = this.artistmanager.searchArtist(nowplaying.getArtist(), nowplaying.getMid());
            if (artist!=null) this.statisticsManager.recordPlay(artist);
            System.out.println(nowplaying.toString());
        } else  System.out.println("[app] current playing no music");
    }

    /**
     * Plays the previous track in a premium playlist, if available.
     * Displays the currently playing track and updates statistics.
     *
     * @param user the user playing the track
     * @param playlist the premium playlist from which the track is played
     */
    private void playPreviousSong(User user, Playlist playlist){
        if (playlist instanceof PremiumPlaylist){
            Music nowplaying = playlist.previousTrack();
            this.currentPlayingMusic = nowplaying;
            if (nowplaying!=null){
                System.out.println("[app] CURRENTLY PLAYING MUSIC");
                this.statisticsManager.recordPlay(user,nowplaying);
                this.statisticsManager.recordPlay(nowplaying);
                this.statisticsManager.recordPlay(user, this.date);
                Artist artist = this.artistmanager.searchArtist(nowplaying.getArtist(), nowplaying.getMid());
                if (artist!=null) this.statisticsManager.recordPlay(artist);
                System.out.println(nowplaying.toString());
            } else  System.out.println("[app] current playing no music");
        }
    }

    /**
     * Plays the next track in a premium playlist, if available.
     * Displays the currently playing track and updates statistics.
     *
     * @param user the user playing the track
     * @param playlist the premium playlist from which the track is played
     */
    private void playNextSong(User user, Playlist playlist){
        if (playlist instanceof PremiumPlaylist){
            Music nowplaying = playlist.nextTrack();
            this.currentPlayingMusic = nowplaying;
            if (nowplaying!=null){
                System.out.println("[app] CURRENTLY PLAYING MUSIC");
                this.statisticsManager.recordPlay(user,nowplaying);
                this.statisticsManager.recordPlay(nowplaying);
                this.statisticsManager.recordPlay(user, this.date);
                Artist artist = this.artistmanager.searchArtist(nowplaying.getArtist(), nowplaying.getMid());
                if (artist!=null) this.statisticsManager.recordPlay(artist);
                System.out.println(nowplaying.toString());
            } else  System.out.println("[app] current playing no music");
        }
    }

    /**
     * Plays the current track of a playlist. If the playlist is random, a track is selected at random.
     * Displays the currently playing music and updates statistics.
     *
     * @param user the user playing the music
     * @param playlist the playlist from which music will be played
     */
    private void playSong(User user, Playlist playlist){
        if (playlist instanceof RandomPlaylist){
            Random random = new Random();
            playlist.setCurrentTrackIndex(random.nextInt(playlist.getTracks().size()));
        }
        Music nowplaying = playlist.playCurrent();
        this.currentPlayingMusic = nowplaying;
        if (nowplaying!=null){
            System.out.println("[app] CURRENTLY PLAYING MUSIC");
            this.statisticsManager.recordPlay(user,nowplaying);
            this.statisticsManager.recordPlay(nowplaying);
            this.statisticsManager.recordPlay(user, this.date);
            Artist artist = this.artistmanager.searchArtist(nowplaying.getArtist(), nowplaying.getMid());
            if (artist!=null) this.statisticsManager.recordPlay(artist);
            System.out.println(nowplaying.toString());
        } else  System.out.println("[app] current playing no music");
    }

    /**
     * Stops the currently playing music and displays a message indicating the track has been stopped.
     * If no music is playing, a message is shown instead.
     */
    private void stopSong() {
        displayStopSong(this.currentPlayingMusic);
        this.previousPlayingMusic = this.currentPlayingMusic;
        this.currentPlayingMusic = null;
    }

    /**
     * Resumes the previously stopped track, if one exists.
     * Displays a message indicating that the track is being resumed,
     * including track name and artist. If no previous track is available,
     * an appropriate message is shown.
     */
    private void resumeSong() {
        if (this.previousPlayingMusic != null) {
            this.currentPlayingMusic = this.previousPlayingMusic;
            this.previousPlayingMusic = null;
            displayResumeSong(this.currentPlayingMusic, true);
        } else {
            displayResumeSong(null, false);
        }
    }

    /**
     * Save state.
     *
     * @param fileName the file name
     * @throws FileNotFoundException the file not found exception
     */
    public void saveState(String fileName) throws FileNotFoundException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
                if (currentPlayingMusic != null) {this.previousPlayingMusic = currentPlayingMusic;}
                oos.writeObject(this);
        } catch (Exception e){
            System.out.println("[app-log] save error: " + e.getMessage());
            throw new FileNotFoundException();
        }
    }

    private void generateRandomPlaylists(){
        for (int i=0; this.randomPlaylists.size()<5; i++){
            RandomPlaylist randomPlaylist = new RandomPlaylist(this.artistmanager.getArtistsSongs());
            randomPlaylist.setName("random playlist #"+i);
            this.randomPlaylists.add(randomPlaylist);
        }
    }

    /**
     * Load state.
     *
     * @param fileName the file name
     * @throws FileNotFoundException  the file not found exception
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void loadState(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        SpotifUM spotifum = (SpotifUM) ois.readObject();
        this.usermanager = spotifum.usermanager;
        this.artistmanager = spotifum.artistmanager;
        this.statisticsManager = spotifum.statisticsManager;
        this.previousPlayingMusic = spotifum.previousPlayingMusic;
        generateRandomPlaylists();
    }

    /**
     * Exist user with email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean existUserWithEmail(String email){
        return this.usermanager.existUserWithEmail(email);
    }

    /**
     * Exist artist with email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean existArtistWithEmail(String email){
        return this.artistmanager.existArtistWithEmail(email);
    }

    /**
     * Get user user.
     *
     * @param email the email
     * @return the user
     */
    public User getUser(String email){
        return this.usermanager.getUser(email);
    }

    /**
     * Get artist artist.
     *
     * @param email the email
     * @return the artist
     */
    public Artist getArtist(String email){
        return this.artistmanager.getArtist(email);
    }

    /**
     * Add music int.
     *
     * @param email     the email
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
    public int addMusic(String email, String name, String artist, String label, String lyrics, List<String> partiture, String genre, int duration, boolean explicit, boolean multimedia, String video){
        return this.artistmanager.addMusic(email, name,artist, label, lyrics,partiture, genre, duration,explicit, multimedia, video);
    }

    /**
     * Get most played music.
     */
    public void getMostPlayedMusic(){
        this.statisticsManager.getMostPlayedMusic();
    }

    /**
     * Get most played artist.
     */
    public void getMostPlayedArtist(){
        this.statisticsManager.getMostPlayedArtist();
    }

    /**
     * Get most listening time.
     */
    public void getMostListeningTime(){
        this.statisticsManager.getMostListeningTime();
    }

    /**
     * Get user leaderboard.
     */
    public void getUserLeaderboard(){
        this.statisticsManager.getUserLeaderboard();
    }

    /**
     * Get most played genres.
     */
    public void getMostPlayedGenres(){
        this.statisticsManager.getMostPlayedGenres();
    }

    /**
     * Get most listening time.
     *
     * @param initialDate the initial date
     * @param finalDate   the final date
     */
    public void getMostListeningTime(LocalDate initialDate, LocalDate finalDate){
        this.statisticsManager.getMostListeningTime(initialDate, finalDate);
    }

    /**
     * How much public playlists exist.
     */
    public void howMuchPublicPlaylistsExist(){
        Map<UUID, Playlist> publicplaylists = this.usermanager.getPublicPlaylists();
        this.statisticsManager.howMuchPublicPlaylistsExist(publicplaylists, this.randomPlaylists);
    }

    /**
     * Who have most playlists.
     */
    public void whoHaveMostPlaylists(){
        Map<User, List<Playlist>> usersPlaylists = this.usermanager.getUsersPlaylists();
        this.statisticsManager.whoHaveMostPlaylists(usersPlaylists);
    }

    /**
     * Generate playlist.
     *
     * @param email          the email
     * @param maxsongs       the maxsongs
     * @param maxtime        the maxtime
     * @param explicitmusics the explicitmusics
     */
    public void generatePlaylist(String email,  int maxsongs, int maxtime, boolean explicitmusics){
        User user = this.usermanager.getUser(email);
        Map<Music, Double> algorithm = this.statisticsManager.getUserHistory(user);
        Playlist generatedPlaylist = this.artistmanager.generatePlaylist(user, algorithm, maxsongs, maxtime, explicitmusics);
        user.createPlaylist(generatedPlaylist);
        this.usermanager.updateUser(user);
    }
}
