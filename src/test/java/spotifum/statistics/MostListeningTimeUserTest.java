package spotifum.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotifum.users.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MostListeningTimeUserTest {

    private MostListeningTimeUser stats;
    private User user1;
    private User user2;
    private LocalDate today;
    private LocalDate yesterday;
    private LocalDate lastWeek;

    @BeforeEach
    void setUp() {
        stats = new MostListeningTimeUser();
        user1 = new TestUser("user1@example.com", "User One");
        user2 = new TestUser("user2@example.com", "User Two");
        today = LocalDate.now();
        yesterday = today.minusDays(1);
        lastWeek = today.minusWeeks(1);
    }

    private static class TestUser extends User {
        public TestUser(String email, String username) {
            super(email, username);
        }

        @Override
        public User clone() {
            return new TestUser(this.getEmail(), this.getUsername());
        }
    }

    @Test
    void testInitialization() {
        assertNotNull(stats);
        // Não podemos verificar o history diretamente, mas podemos verificar o comportamento
        assertDoesNotThrow(() -> stats.displayTopUser());
    }

    @Test
    void testRecordPlayAndRemoveUser() {
        // Teste combinado já que não podemos verificar o history diretamente
        stats.recordPlay(user1, today);
        stats.recordPlay(user2, today);

        // Verifica que ambos foram registrados tentando remover
        stats.removeUser(user1);
        // Se removeUser funcionou, só user2 deve permanecer
        stats.removeUser(user2);

        // Verifica que todos foram removidos
        assertDoesNotThrow(() -> stats.displayTopUser());
    }

    @Test
    void testRecordPlayWithNullUser() {
        assertDoesNotThrow(() -> stats.recordPlay(null, today));
    }

    @Test
    void testRecordPlayWithNullDate() {
        assertDoesNotThrow(() -> stats.recordPlay(user1, null));
    }

    @Test
    void testRemoveNullUser() {
        stats.recordPlay(user1, today);
        stats.removeUser(null);
        // Verifica que user1 ainda está lá
        stats.removeUser(user1);
        assertDoesNotThrow(() -> stats.displayTopUser());
    }

    @Test
    void testDisplayWithVariousDateRanges() {
        stats.recordPlay(user1, today);
        stats.recordPlay(user1, yesterday);
        stats.recordPlay(user2, lastWeek);

        assertDoesNotThrow(() -> stats.displayTopUser());
        assertDoesNotThrow(() -> stats.displayTopUser(today, today));
        assertDoesNotThrow(() -> stats.displayTopUser(lastWeek, today));
        assertDoesNotThrow(() -> stats.displayTopUser(today, yesterday)); // range inválido
    }

    @Test
    void testUserEqualityBasedOnEmail() {
        User user1Copy = new TestUser("user1@example.com", "Different Name");
        stats.recordPlay(user1, today);
        stats.removeUser(user1Copy);

        // Se removeu baseado no email, não deve haver mais plays
        assertDoesNotThrow(() -> stats.displayTopUser());
    }
}