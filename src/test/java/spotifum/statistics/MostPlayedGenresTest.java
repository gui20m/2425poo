package spotifum.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotifum.musics.Artist;
import spotifum.musics.Music;
import static org.junit.jupiter.api.Assertions.*;

class MostPlayedGenresTest {

    private MostPlayedGenres mostPlayedGenres;
    private Music popSong1;
    private Music popSong2;
    private Music rockSong;
    private Music jazzSong;

    @BeforeEach
    void setUp() {
        mostPlayedGenres = new MostPlayedGenres();

        Artist artist1 = new Artist();
        Artist artist2 = new Artist();

        // Criar músicas de diferentes gêneros para teste
        popSong1 = new Music();
        popSong1.setGenre("Pop");

        popSong2 = new Music();
        popSong2.setGenre("Pop");

        rockSong = new Music();
        rockSong.setGenre("Rock");

        jazzSong = new Music();
        jazzSong.setGenre("Jazz");
    }

    @Test
    void testRecordPlayWithNewGenre() {
        // Testar registro de play para um novo gênero
        mostPlayedGenres.recordPlay(popSong1);

        // Verificar se o gênero foi registrado
        String result = mostPlayedGenres.getMostPlayedGenres();
        assertEquals("Pop", result);
    }

    @Test
    void testRecordPlayWithExistingGenre() {
        // Registrar múltiplas plays para o mesmo gênero
        mostPlayedGenres.recordPlay(popSong1);
        mostPlayedGenres.recordPlay(popSong2);

        // Verificar se a contagem está correta
        String result = mostPlayedGenres.getMostPlayedGenres();
        assertEquals("Pop", result);
    }

    @Test
    void testMultipleGenres() {
        // Registrar plays para vários gêneros
        mostPlayedGenres.recordPlay(popSong1);
        mostPlayedGenres.recordPlay(popSong2); // Pop: 2 plays
        mostPlayedGenres.recordPlay(rockSong);  // Rock: 1 play
        mostPlayedGenres.recordPlay(jazzSong);
        mostPlayedGenres.recordPlay(jazzSong);  // Jazz: 2 plays

        // Verificar se o gênero mais tocado está correto (Pop e Jazz têm 2 plays, deve retornar um deles)
        String result = mostPlayedGenres.getMostPlayedGenres();
        assertTrue(result.equals("Pop") || result.equals("Jazz"));
    }

    @Test
    void testEmptyGenres() {
        // Testar quando não há gêneros registrados
        String result = mostPlayedGenres.getMostPlayedGenres();
        assertNull(result);
    }

    @Test
    void testTieBetweenGenres() {
        // Testar empate exato entre gêneros
        mostPlayedGenres.recordPlay(popSong1);    // Pop: 1
        mostPlayedGenres.recordPlay(rockSong);     // Rock: 1
        mostPlayedGenres.recordPlay(jazzSong);     // Jazz: 1

        // Deve retornar um dos gêneros com contagem máxima
        String result = mostPlayedGenres.getMostPlayedGenres();
        assertNotNull(result);
        assertTrue(result.equals("Pop") || result.equals("Rock") || result.equals("Jazz"));
    }

}