package spotifum.musics;

import java.io.Serializable;
import java.util.*;

/**
 * The type Music.
 */
public class Music implements MusicInterface, Serializable {
    private final UUID mid;
    private String name;
    private String artist;
    private String label;
    private String lyrics;
    private List<String> music;
    private String genre;
    private int duration;

    /**
     * Instantiates a new Music.
     *
     * @param name     the name
     * @param artist   the artist
     * @param label    the label
     * @param lyrics   the lyrics
     * @param music    the music
     * @param genre    the genre
     * @param duration the duration
     */
    public Music(String name, String artist, String label, String lyrics, List<String> music, String genre, int duration) {
        this.name = name;
        this.artist = artist;
        this.label = label;
        this.lyrics = lyrics;
        this.music = music;
        this.genre = genre;
        this.duration = duration;
        this.mid = UUID.randomUUID();
    }

    /**
     * Instantiates a new Music.
     *
     * @param c the c
     */
    public Music(Music c){
        this.name = c.getName();
        this.artist = c.getArtist();
        this.label = c.getLabel();
        this.lyrics = c.getLyrics();
        this.music = c.getMusic();
        this.genre = c.getGenre();
        this.duration = c.getDuration();
        this.mid = c.getMid();
    }

    /**
     * Instantiates a new Music.
     */
    public Music(){
        this.mid = UUID.randomUUID();
        this.name = "";
        this.artist = "";
        this.label = "";
        this.lyrics = "";
        this.music = new ArrayList<>();
        this.genre = "";
        this.duration = 0;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets artist.
     *
     * @param artist the artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Sets label.
     *
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Sets lyrics.
     *
     * @param lyrics the lyrics
     */
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * Sets music.
     *
     * @param music the music
     */
    public void setMusic(List<String> music) {
        this.music = music != null ? new ArrayList<>(music) : new ArrayList<>();
    }

    /**
     * Sets genre.
     *
     * @param genre the genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets mid.
     *
     * @return the mid
     */
    public UUID getMid() {
        return this.mid;
    }

    /**
     * Get name string.
     *
     * @return the string
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get artist string.
     *
     * @return the string
     */
    public String getArtist(){
        return this.artist;
    }

    /**
     * Get label string.
     *
     * @return the string
     */
    public String getLabel(){
        return this.label;
    }

    /**
     * Get lyrics string.
     *
     * @return the string
     */
    public String getLyrics(){
        return this.lyrics;
    }

    /**
     * Get music list.
     *
     * @return the list
     */
    public List<String> getMusic(){
        return this.music != null ? new ArrayList<>(this.music) : new ArrayList<>();
    }

    /**
     * Get genre string.
     *
     * @return the string
     */
    public String getGenre(){
        return this.genre;
    }

    /**
     * Get duration int.
     *
     * @return the int
     */
    public int getDuration(){
        return this.duration;
    }

    /**
     * Formats the duration in seconds to a MM:SS string.
     *
     * @param duration the duration in seconds
     * @return the formatted duration string
     */
    private String formatDuration(int duration){
        int  minutes = duration/60;
        int seconds = duration%60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Returns a formatted string representation of the music.
     *
     * @return the formatted string with music details
     */
    public String toString() {
        String header = String.format(
                """
                ╭────────────────────────────────────────────────╮
                │%-48s│
                │%-48s│
                │%-48s│
                ├────────────────────────────────────────────────┤
                """,
                "  🎵 " + String.format("%-42s", name),
                "  🎤 " + String.format("%-42s", artist),
                "  ⏱ " + String.format("%-42s", formatDuration(duration))
        );

        String body;
        if (lyrics == null || lyrics.isEmpty()) {
            body = "│" + " ".repeat(46) + "  │\n" +
                    "│  %-44s  │\n".formatted("(No lyrics available)") +
                    "│" + " ".repeat(46) + "  │\n";
        } else {
            body = formatLyrics(lyrics) + "\n";
        }

        String footer = "╰────────────────────────────────────────────────╯";

        return header + body + footer;
    }

    /**
     * Formats lyrics text to fit within display width.
     *
     * @param lyrics the lyrics text to format
     * @return the formatted lyrics string
     */
    private String formatLyrics(String lyrics) {
        int maxWidth = 46;
        StringBuilder result = new StringBuilder();
        String[] lines = lyrics.split("\n");

        for (String line : lines) {
            if (line.isEmpty()) {
                result.append("│  %-46s│\n".formatted(""));
                continue;
            }

            while (line.length() > maxWidth) {
                int breakPoint = line.lastIndexOf(' ', maxWidth);
                if (breakPoint <= 0) breakPoint = maxWidth;
                String part = line.substring(0, breakPoint).trim();
                result.append("│  %-46s│\n".formatted(part));
                line = line.substring(breakPoint).trim();
            }
            result.append("│  %-46s│".formatted(line));
        }

        return result.toString();
    }

    /**
     * Creates and returns a copy of this music.
     *
     * @return a clone of this music
     */
    public Music clone(){
        return new Music(this);
    }

    /**
     * Compares this music to another object for equality.
     * Musics are equal if they have the same ID or same properties.
     *
     * @param o the object to compare with
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Music musica = (Music) o;

        return (this.duration == musica.getDuration() &&
                this.name.equals(musica.getName()) &&
                this.artist.equals(musica.getArtist()) &&
                this.genre.equals(musica.getGenre()) &&
                this.label.equals(musica.getLabel()) &&
                this.lyrics.equals(musica.getLyrics())) || this.mid.equals(musica.getMid());
    }
}