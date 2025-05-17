package spotifum.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotifum.musics.Artist;
import static org.junit.jupiter.api.Assertions.*;

class MostPlayedArtistTest {

    private MostPlayedArtist mostPlayedArtist;
    private Artist artist1;
    private Artist artist2;
    private Artist artist3;

    @BeforeEach
    void setUp() {
        mostPlayedArtist = new MostPlayedArtist();

        // Criar artistas para teste
        artist1 = new Artist();
        artist2 = new Artist();
        artist3 = new Artist();
    }

    @Test
    void testRecordPlayWithNewArtist() {
        // Testar registro de play para um artista novo
        mostPlayedArtist.recordPlay(artist1);

        Artist artist = mostPlayedArtist.getMostPlayedArtist();
        assertEquals(artist1, artist);
    }

    @Test
    void testMultipleArtists() {
        // Registrar plays para vários artistas
        mostPlayedArtist.recordPlay(artist1);
        mostPlayedArtist.recordPlay(artist1);
        mostPlayedArtist.recordPlay(artist2);
        mostPlayedArtist.recordPlay(artist3);
        mostPlayedArtist.recordPlay(artist3);
        mostPlayedArtist.recordPlay(artist3);
        mostPlayedArtist.recordPlay(artist3);

        Artist artist = mostPlayedArtist.getMostPlayedArtist();
        assertEquals(artist3, artist);
    }

    @Test
    void testEmptyArtists() {
        // Testar quando não há artistas registrados
        Artist artist = mostPlayedArtist.getMostPlayedArtist();
        assertNull(artist);
    }

    @Test
    void testSamePlayCountForMultipleArtists() {
        // Testar quando vários artistas têm a mesma contagem de plays
        mostPlayedArtist.recordPlay(artist1);
        mostPlayedArtist.recordPlay(artist2);
        mostPlayedArtist.recordPlay(artist3);

        Artist artist = mostPlayedArtist.getMostPlayedArtist();
        assertEquals(artist1, artist);
    }
}