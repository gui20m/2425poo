package spotifum.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotifum.musics.Artist;
import spotifum.musics.Music;
import static org.junit.jupiter.api.Assertions.*;

class MostPlayedMusicTest {

    private MostPlayedMusic mostPlayedMusic;
    private Music music1;
    private Music music2;
    private Music music3;

    @BeforeEach
    void setUp() {
        mostPlayedMusic = new MostPlayedMusic();

        // Criar músicas para teste
        Artist artist1 = new Artist();
        Artist artist2 = new Artist();
        music1 = new Music();
        music2 = new Music();
        music3 = new Music();
    }

    @Test
    void testRecordPlayWithNewMusic() {
        // Testar registro de play para uma música nova
        mostPlayedMusic.recordPlay(music1);

        // Verificar se a música foi registrada
        Music result = mostPlayedMusic.getMostPlayedMusic();
        assertEquals(music1, result);
    }

    @Test
    void testRecordPlayWithExistingMusic() {
        // Registrar múltiplas plays para a mesma música
        mostPlayedMusic.recordPlay(music1);
        mostPlayedMusic.recordPlay(music1);
        mostPlayedMusic.recordPlay(music1);

        // Verificar se a contagem está correta
        Music result = mostPlayedMusic.getMostPlayedMusic();
        assertEquals(music1, result);
    }

    @Test
    void testMultipleMusics() {
        // Registrar plays para várias músicas
        mostPlayedMusic.recordPlay(music1);
        mostPlayedMusic.recordPlay(music1);
        mostPlayedMusic.recordPlay(music2);
        mostPlayedMusic.recordPlay(music3);
        mostPlayedMusic.recordPlay(music3);
        mostPlayedMusic.recordPlay(music3);
        mostPlayedMusic.recordPlay(music3);

        // Verificar se a música mais tocada está correta
        Music result = mostPlayedMusic.getMostPlayedMusic();
        assertEquals(music3, result);
    }

    @Test
    void testEmptyMusics() {
        // Testar quando não há músicas registradas
        Music result = mostPlayedMusic.getMostPlayedMusic();
        assertNull(result);
    }

    @Test
    void testSamePlayCountForMultipleMusics() {
        // Testar quando várias músicas têm a mesma contagem de plays
        mostPlayedMusic.recordPlay(music1);
        mostPlayedMusic.recordPlay(music2);
        mostPlayedMusic.recordPlay(music3);

        // Deve retornar a primeira música com a contagem máxima (ordem de inserção)
        Music result = mostPlayedMusic.getMostPlayedMusic();
        assertNotNull(result);
        assertTrue(result.equals(music1) || result.equals(music2) || result.equals(music3));
    }

}