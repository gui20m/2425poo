package spotifum.musics;

import spotifum.exceptions.*;
import spotifum.musics.types.ExplicitMusic;
import spotifum.musics.types.MultimediaMusic;
import spotifum.playlists.*;
import spotifum.users.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static spotifum.utils.DisplayInformation.*;

/**
 * The type Artist manager.
 */
public class ArtistManager implements Serializable {
    private Map<String, Artist> artists;

    /**
     * Instantiates a new Artist manager.
     */
    public ArtistManager(){
        this.artists = new HashMap<>();
    }

    /**
     * Create artist.
     *
     * @param email    the email
     * @param username the username
     * @return the artist
     */
    public Artist createArtist(String email, String username){
        return new Artist(email, username);
    }

    /**
     * Insert artist.
     *
     * @param artist the artist
     * @throws EntityAlreadyExistsException the entity already exists exception
     */
    public void insertArtist(Artist artist) throws EntityAlreadyExistsException {
        String email = artist.getEmail().toLowerCase();
        if (artists.containsKey(email)) {
            throw new EntityAlreadyExistsException("artist with email \"" + email + "\" already exists!");
        }
        artists.put(email, artist);
    }

    /**
     * Update artist email.
     *
     * @param artist   the artist
     * @param newEmail the new email
     * @param oldEmail the old email
     */
    public void updateArtistEmail(Artist artist, String newEmail, String oldEmail) {
        if (artists.remove(oldEmail)==null);
        this.artists.put(newEmail, artist);
    }

    /**
     * Update artist.
     *
     * @param artist the artist
     * @throws EntityNotFoundException the entity not found exception
     */
    public void updateArtist(Artist artist) throws EntityNotFoundException {
        String email = artist.getEmail().toLowerCase();
        Artist a = artists.get(email);

        if (a==null) throw new EntityNotFoundException("[app-log] artist with email: " + email + " does not exist!");

        this.artists.put(email, artist);
    }

    /**
     * Exist artist with email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean existArtistWithEmail(String email){
        return this.artists.containsKey(email.toLowerCase());
    }

    /**
     * Gets artist.
     *
     * @param email the email
     * @return the artist
     * @throws EntityNotFoundException the entity not found exception
     */
    public Artist getArtist(String email) throws EntityNotFoundException {
        Artist a = this.artists.get(email.toLowerCase());

        if (a == null) {
            throw new EntityNotFoundException(email);
        }

        return a;
    }

    /**
     * Search artist.
     *
     * @param name      the name
     * @param musicName the music name
     * @return the artist
     */
    public Artist searchArtist(String name, UUID musicName){
        for  (Artist a : this.artists.values()) {
            if (a.getUsername().equals(name)) {
                for (Music m : a.getTracks().values()){
                    if (m.getMid().equals(musicName)) {
                        return a;
                    }
                }
            }
        }
        return null;
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
     * @param explicit  the explicit boolean
     * @param multimedia the multimedia boolean
     * @return the int
     * @throws EntityNotFoundException the entity not found exception
     */
    public int addMusic(String email, String name, String artist, String label, String lyrics, List<String> partiture, String genre, int duration, boolean explicit, boolean multimedia, String video) throws EntityNotFoundException{
        Artist a = this.artists.get(email.toLowerCase());

        if (a == null) throw new EntityNotFoundException(email);
        Music newMusic = new Music(name, artist, label, lyrics, partiture, genre, duration);
        try {
            if (!explicit && !multimedia) a.addMusic(newMusic);
            if (explicit && !multimedia) a.addMusic(new ExplicitMusic(newMusic));
            if (multimedia) a.addMusic(new MultimediaMusic(newMusic, explicit, video));
        }catch (Exception e){
            displayExceptions(e.getMessage());
        }

        return 1;
    }

    /**
     * Remove artist.
     *
     * @param email the email
     * @throws EntityNotFoundException the entity not found exception
     */
    public void removeArtist(String email) throws EntityNotFoundException {
        if (!this.artists.containsKey(email)) {
            throw new EntityNotFoundException();
        }
        this.artists.remove(email);
    }

    public void getAllArtists() {
        displayAllArtists(this.artists);
    }

    /**
     * Search musics map.
     *
     * @param name the name
     * @return the map
     */
    public Map<UUID, Music> searchMusics(String name) {
        return artists.values().stream()
                .flatMap(artist -> artist.getTracks().values().stream())
                .filter(music -> music.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toMap(
                        Music::getMid,
                        Music::clone,
                        (existing, replacement) -> existing));
    }

    /**
     * Get artists songs map.
     *
     * @return the map
     */
    public Map<UUID, Music> getArtistsSongs(){
        HashMap<UUID, Music> musics = new HashMap<>();
        for (Artist  artist : this.artists.values()){
            for (Music music : artist.getTracks().values()){
                musics.put(music.getMid(), music.clone());
            }
        }
        return musics;
    }

    /**
     * Search albuns map.
     *
     * @param name the name
     * @return the map
     */
    public Map<UUID, Album> searchAlbuns(String name){
        HashMap<UUID, Album> albums = new HashMap<>();
        for (Artist  artist : this.artists.values()){
            for (Album album : artist.getAlbums().values()){
                album.setCreator(artist.getUsername());
                if (album.getName().toLowerCase().contains(name.toLowerCase())){
                    albums.put(album.getAlbumId(), album);
                }
            }
        }
        return albums;
    }

    /**
     * Generate playlist playlist.
     *
     * @param user           the user
     * @param algorithm      the algorithm
     * @param maxSongs       the max songs
     * @param maximumTime    the maximum time
     * @param explicitMusics the explicit musics
     * @return the playlist
     */
    public Playlist generatePlaylist(User user, Map<Music, Double> algorithm, int maxSongs, int maximumTime, boolean explicitMusics) {
        if (algorithm == null || algorithm.isEmpty()) return null;

        Playlist generatedPlaylist = new FavouritePlaylist("Generated Playlist", user.getUsername());
        List<Music> sortedSongs = algorithm.entrySet().stream()
                .sorted(Map.Entry.<Music, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int totalTime = 0;
        int addedSongs = 0;

        for (Music music : sortedSongs) {
            if (canAddMusic(music, explicitMusics, addedSongs, maxSongs, totalTime, maximumTime) && !generatedPlaylist.getTracks().contains(music)) {
                generatedPlaylist.addMusic(music);
                totalTime += music.getDuration();
                addedSongs++;
            }
            if (shouldStop(addedSongs, maxSongs, totalTime, maximumTime)) break;
        }

        if (addedSongs < maxSongs || totalTime < maximumTime) {
            Map<String, Artist> foundArtists = new HashMap<>();

            for (Music music : sortedSongs) {
                Artist artist = foundArtists.computeIfAbsent(
                        music.getArtist(),
                        name -> searchArtist(name, music.getMid())
                );

                if (artist != null) {
                    for (Music artistMusic : artist.getTracks().values()) {
                        if (canAddMusic(artistMusic, explicitMusics, addedSongs, maxSongs, totalTime, maximumTime) &&
                                !generatedPlaylist.getTracks().contains(artistMusic)) {

                            generatedPlaylist.addMusic(artistMusic);
                            totalTime += artistMusic.getDuration();
                            addedSongs++;
                        }
                        if (shouldStop(addedSongs, maxSongs, totalTime, maximumTime)) break;
                    }
                }
                if (shouldStop(addedSongs, maxSongs, totalTime, maximumTime)) break;
            }
        }

        displayGeneratedPlaylist(generatedPlaylist);

        return generatedPlaylist.getTracks().isEmpty() ? null : generatedPlaylist;
    }

    /**
     * Checks if a music can be added to the playlist based on constraints.
     *
     * @param music the music to check
     * @param explicitOnly whether to only allow explicit musics
     * @param added number of songs already added
     * @param max maximum allowed songs
     * @param time current playlist duration
     * @param maxTime maximum allowed duration
     * @return true if the music can be added, false otherwise
     */
    private boolean canAddMusic(Music music, boolean explicitOnly,
                                int added, int max, int time, int maxTime) {
        return (!explicitOnly || music instanceof ExplicitMusic) &&
                added < max &&
                (time + music.getDuration()) <= maxTime;
    }

    /**
     * Checks if playlist generation should stop based on constraints.
     *
     * @param added number of songs already added
     * @param max maximum allowed songs
     * @param time current playlist duration
     * @param maxTime maximum allowed duration
     * @return true if generation should stop, false otherwise
     */
    private boolean shouldStop(int added, int max, int time, int maxTime) {
        return added >= max || time >= maxTime;
    }

    public void deleteMusic(Music music){
        for (Artist a : this.artists.values()){
            for (Music mus : a.getTracks().values()){
                if (mus.equals(music)){
                    a.removeMusic(music);
                }
            }
        }
    }
}
