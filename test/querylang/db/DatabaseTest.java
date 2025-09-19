package querylang.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database db;
    private final User testUser = new User(0, "Ivan", "Ivanov", "Moscow", 30);

    @BeforeEach
    void setUp() {
        db = new Database();
    }

    @Test
    void returnsEmptyList() {
        List<User> users = db.getAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void check_id() {
        int id1 = db.add(testUser);
        int id2 = db.add(testUser);
        assertEquals(0, id1);
        assertEquals(1, id2);
    }

    @Test
    void check_user() {
        int id = db.add(testUser);
        User storedUser = db.getAll().get(0);

        assertEquals(id, storedUser.id());
        assertEquals("Ivan", storedUser.firstName());
        assertEquals("Ivanov", storedUser.lastName());
        assertEquals("Moscow", storedUser.city());
        assertEquals(30, storedUser.age());
    }

    @Test
    void UserNotFound() {
        assertFalse(db.remove(999));
    }

    @Test
    void correct_remover() {
        int id = db.add(testUser);
        assertTrue(db.remove(id));
        assertTrue(db.getAll().isEmpty());
    }

    @Test
    void correct_size() {
        assertEquals(0, db.size());
        db.add(testUser);
        assertEquals(1, db.size());
        db.add(testUser);
        assertEquals(2, db.size());
    }

    @Test
    void correct_clear() {
        db.add(testUser);
        db.add(testUser);
        db.clear();
        assertEquals(0, db.size());
        assertTrue(db.getAll().isEmpty());
    }

}