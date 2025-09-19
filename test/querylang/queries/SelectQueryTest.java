package querylang.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.result.SelectQueryResult;
import querylang.util.FieldGetter;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SelectQueryTest {
    private Database database;
    private List<User> Users;

    @BeforeEach
    void setUp() {
        database = new Database();
        Users = List.of(
                new User(0, "Ivan", "Ivanov", "Moscow", 30),
                new User(1, "Petr", "Petrov", "St.Ptsb", 10)
        );
        Users.forEach(database::add);
    }
    @Test
    void execute_correctSort() {
        List<FieldGetter> getters = List.of(User::lastName);
        SelectQuery query = new SelectQuery(
                getters,
                user -> true,
                Comparator.comparing(User::lastName).reversed()
        );

        SelectQueryResult result = query.execute(database);
        String[] lines = result.message().split("\n");

        assertEquals("Petrov", lines[0]);
        assertEquals("Ivanov", lines[1]);
    }

    @Test
    void execute_NullPtr() {
        SelectQuery query = new SelectQuery(
                List.of(User::firstName),
                user -> true,
                null
        );
        assertThrows(NullPointerException.class, () -> query.execute(null));
    }
}