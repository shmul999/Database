package querylang.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.result.InsertQueryResult;
import static org.junit.jupiter.api.Assertions.*;

class InsertQueryTest {
    private Database database;
    private User user;

    @BeforeEach
    void setUp() {
        database = new Database();
        user = new User(0, "Ivan", "Ivanov", "Moscow", 30);
    }

    @Test
    void execute_correct() {
        InsertQuery insertQuery = new InsertQuery(user);

        InsertQueryResult result = insertQuery.execute(database);

        assertNotNull(result);
        assertEquals("User with id 0 was added successfully", result.message());

        assertEquals(1, database.size());
        User insertedUser = database.getAll().get(0);
        assertEquals(0, insertedUser.id());
        assertEquals("Ivan", insertedUser.firstName());
        assertEquals("Moscow", insertedUser.city());
    }

    @Test
    void execute_NullPtr() {
        InsertQuery insertQuery = new InsertQuery(user);
        assertThrows(NullPointerException.class, () -> {
            insertQuery.execute(null);
        });
    }

}