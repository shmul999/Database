package querylang.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.result.ClearQueryResult;
import static org.junit.jupiter.api.Assertions.*;

class ClearQueryTest {
    private Database database;
    private ClearQuery clearQuery;

    @BeforeEach
    void setUp() {
        database = new Database();
        clearQuery = new ClearQuery();
    }

    @Test
    void execute_correct() {
        database.add(new User(0, "Ivan", "Ivanov", "Moscow", 30));
        database.add(new User(0, "Petr", "Petrov", "St.Ptsb", 25));

        ClearQueryResult result = clearQuery.execute(database);
        assertEquals("2 users were removed successfully", result.message());

        assertEquals(0, database.size());
        assertTrue(database.getAll().isEmpty());
    }

    @Test
    void execute_checkEmpty() {
        ClearQueryResult result = clearQuery.execute(database);

        assertNotNull(result);
        assertEquals("0 users were removed successfully", result.message());
        assertEquals(0, database.size());
    }

    @Test
    void execute_NullPtr() {
        assertThrows(NullPointerException.class, () -> {
            clearQuery.execute(null);
        });
    }

}