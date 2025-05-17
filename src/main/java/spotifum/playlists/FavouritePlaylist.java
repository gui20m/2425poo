package spotifum.playlists;

/**
 * The type Favourite playlist.
 */
public class FavouritePlaylist extends PremiumPlaylist {
    /**
     * Instantiates a new Favourite playlist.
     *
     * @param playlistName the playlist name
     * @param owner        the owner
     */
    public FavouritePlaylist(String playlistName, String owner) {
        super(playlistName, owner);
    }

    /**
     * Instantiates a new Favourite playlist.
     *
     * @param playlist the playlist
     */
    public FavouritePlaylist(FavouritePlaylist playlist) {
        super(playlist, playlist.getOwner());
    }

    /**
     * Creates and returns a copy of this FavouritePlaylist.
     *
     * @return a clone of this playlist
     */
    public FavouritePlaylist clone(){
        return new FavouritePlaylist(this);
    }
}
