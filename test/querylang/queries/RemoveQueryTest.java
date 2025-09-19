package querylang.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.result.RemoveQueryResult;
import static org.junit.jupiter.api.Assertions.*;

class RemoveQueryTest {
    private Database database;
    private int id;

    @BeforeEach
    void setUp() {
        database = new Database();
        id = database.add(new User(0, "Ivan", "Ivanov", "Moscow", 30));
    }

    @Test
    void execute_correct() {
        RemoveQuery removeQuery = new RemoveQuery(id);

        RemoveQueryResult result = removeQuery.execute(database);

        assertNotNull(result);
        assertEquals("User with id " + id + " removed", result.message());
        assertFalse(database.getAll().contains(new User(id, "John", "Doe", "New York", 30)));
    }

    @Test
    void execute_invalidId() {
        int invalid_id = 999;
        RemoveQuery removeQuery = new RemoveQuery(invalid_id);

        RemoveQueryResult result = removeQuery.execute(database);

        assertNotNull(result);;
        assertEquals("User with this id don't exist", result.message());
        assertEquals(1, database.size());
    }

    @Test
    void execute_NullPtr() {
        RemoveQuery removeQuery = new RemoveQuery(id);
        assertThrows(NullPointerException.class, () -> {
            removeQuery.execute(null);
        });
    }
}