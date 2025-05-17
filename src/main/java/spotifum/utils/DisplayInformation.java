package spotifum.utils;

import spotifum.menu.MenuItem;
import spotifum.musics.*;
import spotifum.playlists.*;
import spotifum.users.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Display information.
 */
public class DisplayInformation {

    /**
     * Display menu.
     *
     * @param items          the items
     * @param showExitOption the show exit option
     */
    public static void displayMenu(List<MenuItem> items, boolean showExitOption) {
        int boxWidth = 75;
        int cols = 2;
        int colWidth = (boxWidth) / cols;

        String border = "╭" + "─".repeat(boxWidth-2) + "╮";
        String divider = "├" + "─".repeat(boxWidth-2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth-2) + "╯";

        System.out.println(border);
        System.out.println(String.format("│ %-"+(boxWidth+7)+"s │",
                ConsoleColors.CYAN +"\uD83C\uDFB5 MENU"+ConsoleColors.RESET));
        System.out.println(divider);

        // Não filtrar os itens inválidos - vamos mostrar todos
        for (int i = 0; i < items.size(); i += cols) {
            StringBuilder line = new StringBuilder("│ ");

            for (int j = 0; j < cols; j++) {
                int idx = i + j;
                if (idx < items.size()) {
                    MenuItem item = items.get(idx);

                    if (!item.getName().equalsIgnoreCase("exit")) {
                        line.append(ConsoleColors.ORANGE).append(idx + 1).append(". ").append(ConsoleColors.RESET);

                        if (item.isValid()) {
                            line.append(item.getName());
                            int itemLength = String.valueOf(idx + 1).length() + 2 + item.getName().length() +1;
                            int spaces = colWidth - itemLength;
                            if (spaces > 0) {
                                line.append(" ".repeat(spaces));
                            }
                        } else {
                            String invalidText = "---------";
                            line.append(invalidText);
                            int itemLength = invalidText.length()+4;
                            int spaces = colWidth - itemLength;
                            if (spaces > 0) {
                                line.append(" ".repeat(spaces));
                            }
                        }
                    } else {
                        line.append(" ".repeat(colWidth-1));
                    }
                } else {
                    line.append(" ".repeat(colWidth-1));
                }
            }

            int lineLength = line.length() - 2;
            int remaining = boxWidth - 2 - lineLength;
            if (remaining > 0) {
                line.append(" ".repeat(remaining-1));
            }

            System.out.println(line.append("│"));
        }

        boolean hasManualExit = items.stream()
                .anyMatch(item -> item.toString().equalsIgnoreCase("exit"));

        if (showExitOption) {
            System.out.println(divider);
            int exitNum = items.size() + 1;
            String exitText = ConsoleColors.RESET + exitNum + ". " + ConsoleColors.RED + "exit" + ConsoleColors.RESET;
            System.out.printf("│ %-" + (boxWidth+11) + "s │\n", exitText);
        }

        if (hasManualExit) {
            System.out.println(divider);
            int exitNum = items.size();
            String exitLine = String.format("%s%d. %sexit%s", ConsoleColors.RESET, exitNum, ConsoleColors.RED, ConsoleColors.RESET);
            String formattedExitLine = String.format("│ %-"+(boxWidth+11)+"s │", exitLine);
            System.out.println(formattedExitLine);
        }

        System.out.println(bottom);
    }

    /**
     * Display exceptions.
     *
     * @param text the text
     */
    public static void displayExceptions(String text) {
        final int boxWidth = 75;

        String title = ConsoleColors.RED + "⚠️ EXCEPTION" + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-81s │\n", title);
        System.out.printf("│  %-81s │\n", ConsoleColors.RED + text + ConsoleColors.RESET);
        System.out.println(borderBottom);
    }

    /**
     * Display search results int.
     *
     * @param searchQuery the search query
     * @param musics      the musics
     * @return the int
     */
    public static int displaySearchResults(String searchQuery, Map<UUID, Music> musics) {
        int boxWidth = 75;
        String border = "╭" + "─".repeat(boxWidth-2) + "╮";
        String divider = "├" + "─".repeat(boxWidth-2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth-2) + "╯";

        System.out.println(border);
        System.out.println(String.format("│ %-"+(boxWidth+11)+"s │",
                ConsoleColors.PURPLE+"🔍 search: "+ ConsoleColors.RESET +searchQuery+ConsoleColors.RESET));
        System.out.println(divider);

        if (musics.isEmpty()) {
            System.out.println(String.format("│ %-"+(boxWidth+7)+"s │",
                    ConsoleColors.RED + "no music found"+ ConsoleColors.RESET));
            System.out.println(bottom);
            return 0;
        } else {
            musics.forEach((id, music) -> {
                String line = String.format("%s%s %s- %s [%s%s%s]",
                        ConsoleColors.CYAN, music.getName(),
                        ConsoleColors.RESET, music.getArtist(), ConsoleColors.ORANGE,music.getMid(), ConsoleColors.RESET);
                System.out.println(String.format("│ %-"+(boxWidth+22)+"s │", line));
            });
        }

        System.out.println(bottom);
        return 1;
    }

    /**
     * Display random playlists.
     *
     * @param randomPlaylists the random playlists
     */
    public static void displayRandomPlaylists(List<RandomPlaylist> randomPlaylists) {

        int boxWidth = 64;
        String title = ConsoleColors.CYAN + "🎲 RANDOM PLAYLISTS" + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-70s │\n", title);
        System.out.println(borderMiddle);

        if (randomPlaylists.isEmpty()) {
            System.out.printf("│  %-70s │\n", ConsoleColors.RED + "no playlists available" + ConsoleColors.RESET);
        } else {
            for (Playlist playlist : randomPlaylists) {
                String line = "• " + playlist.getName() + " [" + ConsoleColors.ORANGE + playlist.getId() + ConsoleColors.RESET + "]";
                System.out.printf("│  %-46s │\n", line);
            }
        }

        System.out.println(borderBottom);
    }

    /**
     * Displays albums that match a search name in a styled format.
     *
     * @param albums    the albums to display
     * @param albumName the album name searched
     * @return 1 if albums found, 0 otherwise
     */
    public static int displayAlbums(Map<UUID, Album> albums, String albumName) {
        final int boxWidth = 75;

        String title = ConsoleColors.CYAN + "📀 ALBUMS FOUND" + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-81s │\n", title);
        System.out.printf("│  %-81s │\n", ConsoleColors.GREEN + "Matching: "+ ConsoleColors.RESET + albumName);
        System.out.println(borderMiddle);

        if (albums.isEmpty()) {
            System.out.printf("│  %-81s │\n", ConsoleColors.RED + "no albums found" + ConsoleColors.RESET);
            System.out.println(borderBottom);
            return 0;
        } else {
            for (Album album : albums.values()) {
                String line = "• " + album.getName() + " (" + album.getCreator() + ") [" + ConsoleColors.ORANGE + album.getAlbumId() + ConsoleColors.RESET + "]";
                System.out.printf("│  %-85s │\n", line);
            }
        }

        System.out.println(borderBottom);
        return 1;
    }

    /**
     * Displays the current shuffle mode status of a premium playlist in a styled format.
     *
     * @param playlist the premium playlist whose shuffle mode is shown
     */
    public static void displayShuffleMode(PremiumPlaylist playlist) {
        int boxWidth = 64;
        String title = ConsoleColors.PURPLE + "🔀 SHUFFLE MODE" + ConsoleColors.RESET;
        String playlistName = ConsoleColors.CYAN + playlist.getName() + ConsoleColors.RESET;
        String status = playlist.isShuffleModeActive()
                ? ConsoleColors.GREEN + "ACTIVE" + ConsoleColors.RESET
                : ConsoleColors.RED + "INACTIVE" + ConsoleColors.RESET;

        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-70s │\n", title);
        System.out.println(borderMiddle);
        System.out.printf("│  %-70s │\n", "Playlist: " + playlistName);
        System.out.printf("│  %-70s │\n", "Shuffle Mode: " + status);
        System.out.println(borderMiddle);

        String message = playlist.isShuffleModeActive()
                ? "Tracks are being played in random order"
                : "Tracks are being played in default order";
        System.out.printf("│  %-59s │\n", message);

        System.out.println(borderBottom);
    }

    /**
     * Displays all tracks in a playlist with formatting.
     *
     * @param playlist the playlist whose tracks will be shown
     * @return 1 if tracks found, 0 otherwise
     */
    public static int displayPlaylistAvailableTracks(Playlist playlist) {
        final int boxWidth = 75;

        StringBuilder sb = new StringBuilder();

        String border = "╭" + "─".repeat(boxWidth-2) + "╮";
        String divider = "├" + "─".repeat(boxWidth-2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth-2) + "╯";

        sb.append(border).append("\n");
        sb.append(String.format("│  \uD83E\uDEA9 %-78s │\n", ConsoleColors.CYAN + "PLAYLIST DETAILS" + ConsoleColors.RESET));
        sb.append(String.format("│  \uD83D\uDCDA %-82s │\n", "Playlist: " + ConsoleColors.ORANGE + playlist.getName() + ConsoleColors.RESET));
        sb.append(String.format("│  \uD83D\uDC64 %-82s │\n", "Owner: " + ConsoleColors.ORANGE + playlist.getOwner() + ConsoleColors.RESET));
        sb.append(String.format("│  \uD83D\uDD13 %-78s │\n",
                "Public: " + (playlist.isPublic() ? ConsoleColors.CYAN + "yes" : ConsoleColors.RED + "no") + ConsoleColors.RESET));
        sb.append(divider).append("\n");

        if (playlist.getTracks().isEmpty()) {
            sb.append(String.format("│  %-81s │\n", ConsoleColors.RED + "no tracks available" + ConsoleColors.RESET));
            sb.append(bottom);
            System.out.println(sb.toString());
            return 0;
        } else {
            for (Music track : playlist.getTracks()) {
                String trackLine = "• " + track.getName() + " - " + ConsoleColors.CYAN + track.getArtist() + ConsoleColors.RESET + " ["+ConsoleColors.ORANGE+track.getMid()+ConsoleColors.RESET+"]";
                sb.append(String.format("│  %-96s │\n", trackLine));
            }
            sb.append(bottom);
            System.out.println(sb.toString());
            return 1;
        }
    }

    /**
     * Displays all playlists of a user in a styled box format.
     *
     * @param user      the user whose playlists will be shown
     * @param playlists the playlists to display
     */
    public static void displayUserPlaylists(User user, Map<UUID, Playlist> playlists) {

        int boxWidth = 75;
        String title = ConsoleColors.CYAN + "🎧 USER PLAYLISTS "+ConsoleColors.RESET+"[" + ConsoleColors.YELLOW + user.getUsername() + ConsoleColors.RESET + "]";
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-92s │\n", title);
        System.out.println(borderMiddle);

        if (playlists.isEmpty()) {
            System.out.printf("│  %-81s │\n", ConsoleColors.RED + "no playlists available" + ConsoleColors.RESET);
        } else {
            for (Playlist playlist : playlists.values()) {
                String line = "• " + playlist.getName() + " [" + ConsoleColors.ORANGE + playlist.getId() + ConsoleColors.RESET + "]";
                System.out.printf("│  %-85s │\n", line);
            }
        }

        System.out.println(borderBottom);
    }

    /**
     * Displays all albums available for a given artist in a styled format.
     *
     * @param artist the artist whose albums will be displayed
     */
    public static void displayAvailableAlbums(Artist artist) {
        final int boxWidth = 75;

        String title = ConsoleColors.CYAN + "💿 AVAILABLE ALBUMS" + ConsoleColors.RESET;
        String artistInfo = "Artist: " + ConsoleColors.ORANGE + artist.getUsername() + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-81s │\n", title);
        System.out.printf("│  %-85s │\n", artistInfo);
        System.out.println(borderMiddle);

        if (artist.getAlbums().isEmpty()) {
            System.out.printf("│  %-81s │\n", ConsoleColors.RED + "no albums available" + ConsoleColors.RESET);
        } else {
            for (Album album : artist.getAlbums().values()) {
                String line = "• " + album.getName() + " [" + ConsoleColors.ORANGE + album.getAlbumId() + ConsoleColors.RESET + "]";
                System.out.printf("│  %-85s │\n", line);
            }
        }

        System.out.println(borderBottom);
    }

    /**
     * Displays the tracks available for an artist, with optional filtering based on album association.
     *
     * @param artist            the artist whose tracks will be displayed
     * @param album             the album used for filtering (can be null)
     * @param verifyIfIsInAlbum if 1, only tracks not already in the album are displayed
     * @param updatingAlbum     if 1, only tracks currently in the album are displayed
     */
    public static void displayAvailableTracks(Artist artist, Album album, int verifyIfIsInAlbum, int updatingAlbum) {
        final int boxWidth = 75;

        String title = ConsoleColors.CYAN + "🎧 AVAILABLE TRACKS" + ConsoleColors.RESET;
        String artistInfo = "Artist: " + ConsoleColors.ORANGE + artist.getUsername() + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-81s │\n", title);
        System.out.printf("│  %-85s │\n", artistInfo);
        System.out.println(borderMiddle);

        Collection<Music> allTracks = artist.getTracks().values();
        List<Music> filteredTracks = new ArrayList<>();

        Set<UUID> albumTrackIds = new HashSet<>();
        if (album != null) {
            albumTrackIds = album.getMusics().stream()
                    .map(Music::getMid)
                    .collect(Collectors.toSet());
        }

        if (updatingAlbum == 1 && album != null) {
            for (Music music : allTracks) {
                if (albumTrackIds.contains(music.getMid())) {
                    filteredTracks.add(music);
                }
            }
        } else if (verifyIfIsInAlbum == 1 && album != null) {
            for (Music music : allTracks) {
                if (!albumTrackIds.contains(music.getMid())) {
                    filteredTracks.add(music);
                }
            }
        } else {
            filteredTracks.addAll(allTracks);
        }

        if (filteredTracks.isEmpty()) {
            System.out.printf("│  %-81s │\n", ConsoleColors.RED + "no tracks available" + ConsoleColors.RESET);
        } else {
            for (Music music : filteredTracks) {
                String line = "• " + music.getName() + " [" + ConsoleColors.ORANGE + music.getMid() + ConsoleColors.RESET + "]";
                System.out.printf("│  %-85s │\n", line);
            }
        }

        System.out.println(borderBottom);
    }

    /**
     * Displays the user's premium playlists with formatted borders and colors.
     * Returns 1 if any premium playlists are found, or 0 otherwise.
     *
     * @param publicPlaylists the public playlists
     * @param user            the user whose premium playlists will be shown
     * @param playlistName    the playlist name
     * @return 1 if premium playlists exist, 0 if none are found
     */
    public static int displayPublicPlaylists(Map<UUID, Playlist> publicPlaylists, User user, String playlistName) {
        final int boxWidth = 75;

        String title = ConsoleColors.CYAN + "🌐 PUBLIC PLAYLISTS" + ConsoleColors.RESET;
        String subtitle = "Matching: " + ConsoleColors.GREEN + playlistName + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-81s │\n", title);
        System.out.printf("│  %-81s │\n", subtitle);
        System.out.println(borderMiddle);

        if (publicPlaylists.isEmpty()) {
            System.out.printf("│  %-81s │\n", ConsoleColors.RED + "no public playlists found" + ConsoleColors.RESET);
            System.out.println(borderBottom);
            return 0;
        }

        for (Playlist p : publicPlaylists.values()) {
            String line = "• " + p.getName() + " - " + p.getOwner() + " [" + ConsoleColors.ORANGE + p.getId() + ConsoleColors.RESET + "]";
            System.out.printf("│  %-85s │\n", line);
        }

        System.out.println(borderBottom);
        return 1;
    }

    /**
     * Display stop song.
     *
     * @param music the music
     */
    public static void displayStopSong(Music music) {
        if (music != null) {
            System.out.println("\n╭────────────────────────────────────────────────╮");
            System.out.println("│ \uD83D\uDD0C CURRENTLY PLAYING TRACK                     │");
            System.out.println("├────────────────────────────────────────────────┤");

            String musicName = music.getName();
            String artist = music.getArtist();

            String display = String.format("%s - %s",
                    musicName.length() > 30 ? musicName.substring(0, 27) + "..." : ConsoleColors.ORANGE + musicName + ConsoleColors.RESET,
                    artist.length() > 15 ? artist.substring(0, 12) + "..." : artist);

            System.out.println(String.format("│ %-61s │", display));
            String progressBar = "────────────────" + ConsoleColors.BLUE + "⏸" + ConsoleColors.RESET + "──────────────────────────────" + ConsoleColors.RED + "⏹" + ConsoleColors.RESET;
            System.out.println("├" + progressBar + "┤");
            System.out.println("│ \uD83D\uDED1 YOU STOPPED THE TRACK                       │");
            System.out.println("╰────────────────────────────────────────────────╯");
        } else {
            System.out.println("[app-log] you aint playing a track");
        }
    }

    /**
     * Display resume song.
     *
     * @param music   the music
     * @param success the success
     */
    public static void displayResumeSong(Music music, boolean success) {
        if (success) {
            System.out.println("\n╭────────────────────────────────────────────────╮");
            System.out.println("│ \uD83D\uDD0C RESUMING TRACK                              │");
            System.out.println("├────────────────────────────────────────────────┤");

            String musicName = music.getName();
            String artist = music.getArtist();

            String display = String.format("%s - %s",
                    musicName.length() > 30 ? musicName.substring(0, 27) + "..." : ConsoleColors.ORANGE + musicName + ConsoleColors.RESET,
                    artist.length() > 15 ? artist.substring(0, 12) + "..." : artist);

            System.out.println(String.format("│ %-61s │", display));
            String progressBar = "────────────────" + ConsoleColors.GREEN + "▶" + ConsoleColors.RESET + "──────────────────────────────" + ConsoleColors.RED + "⏹" + ConsoleColors.RESET;
            System.out.println("├" + progressBar + "┤");
            System.out.println("│ \uD83D\uDD04 TRACK RESUMED                               │");
            System.out.println("╰────────────────────────────────────────────────╯");
        } else {
            System.out.println("\n╭──────────────────────────────────────────────╮");
            System.out.println("│ \uD83D\uDD0C NO TRACK TO RESUME                        │");
            System.out.println("├──────────────────────────────────────────────┤");
            System.out.println("│ \uD83D\uDEAB No previous track in memory               │");
            System.out.println("╰──────────────────────────────────────────────╯");
        }
    }

    /**
     * Display current date.
     *
     * @param currentDate the current date
     */
    public static void displayCurrentDate(LocalDate currentDate) {
        final String RESET = "\033[0m";
        final String CYAN = "\033[1;36m";
        final String YELLOW = "\033[1;33m";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        String formattedDate = currentDate.format(formatter);

        final int boxWidth = 35;
        String border = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String bottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(border);
        System.out.printf("│ %-42s │\n", "📅" + CYAN + " CURRENT DATE" + RESET);
        System.out.println("├" + "─".repeat(boxWidth - 2) + "┤");
        System.out.printf("│ %-42s │\n", YELLOW + formattedDate + RESET);
        System.out.println(bottom);
    }

    /**
     * Display playlist indexs.
     *
     * @param playlist the playlist
     */
    public static void displayPlaylistIndexs(Playlist playlist) {
        final int boxWidth = 75;

        String title = ConsoleColors.PURPLE + "🎵 PLAYLIST TRACKS (" + playlist.getTracks().size() + ")" + ConsoleColors.RESET;
        String subtitle = ConsoleColors.YELLOW + playlist.getName() + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-81s │\n", title);
        System.out.printf("│  %-81s │\n", subtitle);
        System.out.println(borderMiddle);

        if (playlist.getTracks().isEmpty()) {
            System.out.printf("│  %-81s │\n", ConsoleColors.RED + "no tracks available" + ConsoleColors.RESET);
            System.out.println(borderBottom);
            return;
        }

        for (int i = 0; i < playlist.getTracks().size(); i++) {
            Music track = playlist.getTracks().get(i);
            String line = "[" + ConsoleColors.BLUE + i + ConsoleColors.RESET + "] " + track.getName() + " - " +
                    ConsoleColors.ORANGE + track.getArtist() + ConsoleColors.RESET;
            System.out.printf("│  %-96s │\n", line);
        }

        System.out.println(borderBottom);
    }

    /**
     * Display all artists.
     *
     * @param artists the artists
     */
    public static void displayAllArtists(Map<String, Artist> artists){
        final int boxWidth = 100;
        String title = ConsoleColors.CYAN + "\uD83E\uDDD1\u200D\uD83C\uDFA4 ALL REGISTERED ARTISTS" + ConsoleColors.RESET;
        String borderTop = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String borderMiddle = "├" + "─".repeat(boxWidth - 2) + "┤";
        String borderBottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(borderTop);
        System.out.printf("│  %-109s │\n", title);
        System.out.println(borderMiddle);

        if (artists.isEmpty()) {
            System.out.printf("│  %-106s │\n", ConsoleColors.RED + "no artists found" + ConsoleColors.RESET);
        } else {
            for (Artist artist : artists.values()) {
                String email = artist.getEmail();
                String username = artist.getUsername();
                String line = "• " + email + " | " + ConsoleColors.ORANGE + username + ConsoleColors.RESET + " | Songs: " + ConsoleColors.ORANGE + artist.getTracks().size() + ConsoleColors.RESET + " | Albums: " + ConsoleColors.ORANGE + artist.getAlbums().size() + ConsoleColors.RESET;
                System.out.printf("│  %-140s │\n", line);
            }
        }

        System.out.println(borderBottom);
    }

    /**
     * Displays the generated playlist in a formatted box.
     *
     * @param generatedPlaylist the playlist to display
     */
    public static void displayGeneratedPlaylist(Playlist generatedPlaylist) {

        final int boxWidth = 50;
        String border = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String divider = "├" + "─".repeat(boxWidth - 2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(border);
        System.out.printf("│ %-57s │\n", "🌟" + ConsoleColors.CYAN + " GENERATED PLAYLIST " + ConsoleColors.RESET);
        System.out.println(divider);

        if (generatedPlaylist == null || generatedPlaylist.getTracks().isEmpty()) {
            System.out.printf("│ %-57s │\n", ConsoleColors.RED + "no songs in the generated playlist" + ConsoleColors.RESET);
        } else {
            System.out.printf("│ %-57s │\n", ConsoleColors.BLUE + "Name: " + ConsoleColors.RESET + generatedPlaylist.getName());
            System.out.printf("│ %-57s │\n", ConsoleColors.BLUE + "Owner: " + ConsoleColors.RESET + generatedPlaylist.getOwner());
            System.out.printf("│ %-57s │\n", ConsoleColors.BLUE + "Total songs: " + ConsoleColors.RESET + generatedPlaylist.getTracks().size());

            int totalSeconds = generatedPlaylist.getTracks().stream()
                    .mapToInt(Music::getDuration)
                    .sum();
            String duration = String.format("%d:%02d", totalSeconds / 60, totalSeconds % 60);
            System.out.printf("│ %-57s │\n", ConsoleColors.BLUE + "Total duration: " + ConsoleColors.RESET + duration + " mins");
            System.out.println(divider);

            System.out.printf("│ %-57s │\n", ConsoleColors.YELLOW + "TRACK LIST:" + ConsoleColors.RESET);
            for (int i = 0; i < generatedPlaylist.getTracks().size(); i++) {
                Music music = generatedPlaylist.getTracks().get(i);
                String trackLine = String.format("%2d. %s - %s",
                        i + 1,
                        ConsoleColors.ORANGE + music.getName() + ConsoleColors.RESET,
                        music.getArtist());
                System.out.printf("│ %-61s │\n", trackLine);
            }
        }

        System.out.println(bottom);
    }

    /**
     * Display public playlists.
     *
     * @param publicplaylists the publicplaylists
     * @param randomPlaylists the random playlists
     */
    public static void displayHowMuchPublicPlaylistsExist(Map<UUID, Playlist> publicplaylists, List<RandomPlaylist> randomPlaylists) {

        final int boxWidth = 60;
        String border = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String divider = "├" + "─".repeat(boxWidth - 2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(border);
        System.out.printf("│ %-67s │\n", ConsoleColors.CYAN + "HOW MANY PUBLIC PLAYLISTS EXIST" + ConsoleColors.RESET);
        System.out.println(divider);

        int total = publicplaylists.size() +  randomPlaylists.size();
        System.out.printf("│ %-67s │\n", ConsoleColors.GREEN + "Total: " + ConsoleColors.RESET + total);
        System.out.println(divider);

        if (total == 0) {
            System.out.printf("│ %-67s │\n", ConsoleColors.RED + "no public playlists found" + ConsoleColors.RESET);
        } else {
            System.out.printf("│ %-67s │\n", ConsoleColors.YELLOW + "SpotifUM Random Playlists:" + ConsoleColors.RESET);
            randomPlaylists.forEach(randomPlaylist -> {
                System.out.printf("│ • %-69s │\n", ConsoleColors.ORANGE + randomPlaylist.getName() + ConsoleColors.RESET + " - " + randomPlaylist.getOwner());
            });
            System.out.println(divider);
            System.out.printf("│ %-67s │\n", ConsoleColors.BLUE + "Public User Playlists:" + ConsoleColors.RESET);
            if (publicplaylists.size() > 0) {
                publicplaylists.values().forEach(playlist -> {
                    if (playlist.isPublic())
                    System.out.printf("│ • %-69s │\n", ConsoleColors.ORANGE + playlist.getName() + ConsoleColors.RESET + " - " + playlist.getOwner());
                });
            }
            else System.out.printf("│ %-67s │\n", ConsoleColors.RED + "no user public playlists found" + ConsoleColors.RESET);
        }

        System.out.println(bottom);
    }

    /**
     * Display top user.
     *
     * @param history the history
     */
    public static void displayMostListeningTimeUser(Map<User, Map<LocalDate, Integer>> history) {
        final int boxWidth = 50;
        String border = "╭" + "─".repeat(boxWidth-2) + "╮";
        String divider = "├" + "─".repeat(boxWidth-2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth-2) + "╯";

        System.out.println(border);
        System.out.printf("│ %-57s │\n", ConsoleColors.CYAN + "🏆 ALL-TIME TOP USER" + ConsoleColors.RESET);
        System.out.println(divider);

        Optional<Map.Entry<User, Integer>> topEntry = history.entrySet().stream()
                .map(entry -> Map.entry(
                        entry.getKey(),
                        entry.getValue().values().stream().mapToInt(Integer::intValue).sum()
                ))
                .max(Map.Entry.comparingByValue());

        if (topEntry.isPresent()) {
            User topUser = topEntry.get().getKey();
            int totalPlays = topEntry.get().getValue();
            System.out.printf("│ %-61s │\n", "• " + ConsoleColors.ORANGE + topUser.getUsername() + ConsoleColors.RESET);
            System.out.printf("│ %-57s │\n", ConsoleColors.BLUE + "  Total Plays: " + ConsoleColors.RESET + totalPlays);
        } else {
            System.out.printf("│ %-57s │\n", ConsoleColors.RED + "no plays recorded yet" + ConsoleColors.RESET);
        }

        System.out.println(bottom);
    }

    /**
     * Display top user.
     *
     * @param initialDate the initial date
     * @param finalDate   the final date
     * @param history     maps users to music history by date
     */
    public static void displayMostListeningTimeUser(LocalDate initialDate, LocalDate finalDate, Map<User, Map<LocalDate, Integer>> history) {

        final int boxWidth = 50;
        String border = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String divider = "├" + "─".repeat(boxWidth - 2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(border);
        System.out.printf("│ %-57s │\n", "🏆" + ConsoleColors.CYAN +" TOP USER BETWEEN " + initialDate + " AND " + finalDate + ConsoleColors.RESET);
        System.out.println(divider);

        Optional<Map.Entry<User, Integer>> topEntry = history.entrySet().stream()
                .map(entry -> {
                    int totalPlays = entry.getValue().entrySet().stream()
                            .filter(dateEntry -> !dateEntry.getKey().isBefore(initialDate))
                            .filter(dateEntry -> !dateEntry.getKey().isAfter(finalDate))
                            .mapToInt(Map.Entry::getValue)
                            .sum();
                    return Map.entry(entry.getKey(), totalPlays);
                })
                .filter(entry -> entry.getValue() > 0)
                .max(Map.Entry.comparingByValue());

        if (topEntry.isPresent()) {
            User topUser = topEntry.get().getKey();
            int totalPlays = topEntry.get().getValue();
            System.out.printf("│ %-61s │\n", "• " + ConsoleColors.ORANGE + topUser.getUsername() + ConsoleColors.RESET);
            System.out.printf("│ %-57s │\n", ConsoleColors.BLUE + "  Total Plays: " + ConsoleColors.RESET + totalPlays);
        } else {
            System.out.printf("│ %-57s │\n", ConsoleColors.RED + "no plays recorded in this period" + ConsoleColors.RESET);
        }

        System.out.println(bottom);
    }

    /**
     * Display most played artist.
     *
     * @param playedartists the playedartists
     */
    public static Artist displayMostPlayedArtist(Map<Artist, Integer> playedartists) {
        final int boxWidth = 50;

        List<Map.Entry<Artist, Integer>> sortedEntries = new ArrayList<>(playedartists.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        String border = "╭" + "─".repeat(boxWidth-2) + "╮";
        String divider = "├" + "─".repeat(boxWidth-2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth-2) + "╯";

        System.out.println(border);
        System.out.println(String.format("│ %-"+(boxWidth+6)+"s │",
                ConsoleColors.CYAN + "⭐ MOST PLAYED ARTIST" + ConsoleColors.RESET));
        System.out.println(divider);

        Artist topArtist = null;

        if (sortedEntries.isEmpty()) {
            System.out.println(String.format("│ %-"+(boxWidth+7)+"s │",
                    ConsoleColors.RED + "no artists played yet" + ConsoleColors.RESET));
        } else {
            topArtist = sortedEntries.get(0).getKey();
            int playCount = sortedEntries.get(0).getValue();

            System.out.println(String.format("│ %-"+(boxWidth+11)+"s │",
                    "• " + ConsoleColors.ORANGE + topArtist.getUsername() + ConsoleColors.RESET));
            System.out.println(divider);
            System.out.println(String.format("│ %-"+(boxWidth+7)+"s │",
                    ConsoleColors.BLUE + "Total plays: " + ConsoleColors.RESET + playCount));
        }

        System.out.println(bottom);
        return topArtist;
    }

    /**
     * Display most played genres.
     *
     * @param playedgenres the playedgenres
     */
    public static String displayMostPlayedGenres(Map<String, Integer> playedgenres) {
        final int boxWidth = 40;
        String border = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String bottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(border);
        System.out.printf("│ %-47s │\n", "🎵" + ConsoleColors.CYAN + " MOST PLAYED GENRES" + ConsoleColors.RESET);
        System.out.println("├" + "─".repeat(boxWidth - 2) + "┤");

        String topGenre = null;

        if (playedgenres.isEmpty()) {
            System.out.printf("│ %-47s │\n", ConsoleColors.RED + "no genres recorded" + ConsoleColors.RESET);
        } else {
            List<Map.Entry<String, Integer>> topGenres = playedgenres.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(3)
                    .toList();

            String[] medals = {"🥇", "🥈", "🥉"};

            for (int i = 0; i < topGenres.size(); i++) {
                Map.Entry<String, Integer> entry = topGenres.get(i);
                String genre = entry.getKey();
                int plays = entry.getValue();

                // Pegar o primeiro gênero (top 1)
                if (i == 0) {
                    topGenre = genre;
                }

                System.out.printf("│ %-62s │\n",
                        medals[i] + " " + ConsoleColors.YELLOW + genre + ConsoleColors.RESET +
                                " | " + ConsoleColors.ORANGE + "Plays: " + ConsoleColors.RESET + plays);
            }
        }

        System.out.println(bottom);
        return topGenre;
    }

    /**
     * Display most played music.
     *
     * @param playedtracks the playedtracks
     */
    public static Music displayMostPlayedMusic(Map<Music, Integer> playedtracks) {
        final int boxWidth = 50;

        List<Map.Entry<Music, Integer>> sortedEntries = new ArrayList<>(playedtracks.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        String border = "╭" + "─".repeat(boxWidth-2) + "╮";
        String divider = "├" + "─".repeat(boxWidth-2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth-2) + "╯";

        System.out.println(border);
        System.out.println(String.format("│ %-"+(boxWidth+6)+"s │",
                ConsoleColors.CYAN + "⭐ MOST PLAYED TRACK" + ConsoleColors.RESET));
        System.out.println(divider);

        Music mostPlayed = null;

        if (sortedEntries.isEmpty()) {
            System.out.println(String.format("│ %-"+(boxWidth+7)+"s │",
                    ConsoleColors.RED + "no tracks played yet" + ConsoleColors.RESET));
        } else {
            mostPlayed = sortedEntries.get(0).getKey();
            int playCount = sortedEntries.get(0).getValue();

            System.out.println(String.format("│ %-"+(boxWidth-4)+"s │",
                    "• " + mostPlayed.getName()));
            System.out.println(String.format("│ %-"+(boxWidth+11)+"s │",
                    "  by " + ConsoleColors.ORANGE + mostPlayed.getArtist() + ConsoleColors.RESET));
            System.out.println(divider);
            System.out.println(String.format("│ %-"+(boxWidth+7)+"s │",
                    ConsoleColors.BLUE + "Plays: " + ConsoleColors.RESET + playCount));
        }

        System.out.println(bottom);
        return mostPlayed;
    }

    /**
     * Display user leader board.
     *
     * @param leaderboard the leaderboard
     */
    public static void displayUserLeaderBoard(Map<User, Map<Music, Double>> leaderboard) {

        final int boxWidth = 50;
        String border = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String divider = "├" + "─".repeat(boxWidth - 2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(border);
        System.out.printf("│ %-57s │\n", "🏆" + ConsoleColors.CYAN + " USER WITH MOST POINTS" + ConsoleColors.RESET);
        System.out.println(divider);

        Optional<Map.Entry<User, Double>> topEntry = leaderboard.entrySet().stream()
                .map(entry -> {
                    double totalPoints = entry.getValue().values().stream()
                            .mapToDouble(Double::doubleValue)
                            .sum();
                    return Map.entry(entry.getKey(), totalPoints);
                })
                .filter(entry -> entry.getValue() > 0)
                .max(Map.Entry.comparingByValue());

        if (topEntry.isPresent()) {
            User topUser = topEntry.get().getKey();
            double totalPoints = topEntry.get().getValue();

            System.out.printf("│ %-61s │\n", "• " + ConsoleColors.ORANGE + topUser.getUsername() + ConsoleColors.RESET);
            System.out.printf("│ %-57s │\n", ConsoleColors.BLUE + "  Plan: " + ConsoleColors.RESET + topUser.getUserPlan().toString());
            System.out.printf("│ %-57s │\n", ConsoleColors.GREY + "  Total Points: " + ConsoleColors.RESET + String.format("%.2f", totalPoints));
        } else {
            System.out.printf("│ %-57s │\n", ConsoleColors.RED + "no plays recorded" + ConsoleColors.RESET);
        }

        System.out.println(bottom);
    }

    /**
     * Display who have most public playlists.
     *
     * @param playlists the playlists
     */
    public static User displayWhoHaveMostPlaylists(Map<User, List<Playlist>> playlists) {
        final int boxWidth = 50;
        String border = "╭" + "─".repeat(boxWidth - 2) + "╮";
        String divider = "├" + "─".repeat(boxWidth - 2) + "┤";
        String bottom = "╰" + "─".repeat(boxWidth - 2) + "╯";

        System.out.println(border);
        System.out.printf("│ %-57s │\n", "📊" + ConsoleColors.CYAN + " TOP PLAYLIST CREATORS" + ConsoleColors.RESET);
        System.out.println(divider);

        if (playlists.isEmpty()) {
            System.out.printf("│ %-57s │\n", ConsoleColors.RED + "no playlists found" + ConsoleColors.RESET);
            System.out.println(bottom);
            return null;
        } else {
            User topUser = playlists.entrySet().stream()
                    .max((e1, e2) -> Integer.compare(e1.getValue().size(), e2.getValue().size()))
                    .map(Map.Entry::getKey)
                    .orElse(null);

            playlists.entrySet().stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                    .limit(3)
                    .forEachOrdered(entry -> {
                        User user = entry.getKey();
                        int count = entry.getValue().size();
                        String medal = "";

                        if (entry == playlists.entrySet().stream().findFirst().orElse(null)) {
                            medal = "🥇 ";
                        } else if (entry == playlists.entrySet().stream().skip(1).findFirst().orElse(null)) {
                            medal = "🥈 ";
                        } else {
                            medal = "🥉 ";
                        }

                        if (count>0)
                            System.out.printf("│ %-61s │\n",
                                    medal + ConsoleColors.ORANGE + user.getUsername() + ConsoleColors.RESET +
                                            " - " + count + " playlists");
                    });

            System.out.println(bottom);
            return topUser;
        }
    }
}
