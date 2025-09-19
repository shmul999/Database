package querylang.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private final User user1 = new User(1, "John", "Doe", "New York", 30);
    private final User user1Copy = new User(1, "John", "Doe", "New York", 30);
    private final User user2 = new User(2, "Jane", "Smith", "Boston", 25);
    private final User userDifferentId = new User(99, "John", "Doe", "New York", 30);
    private final User userDifferentFirstName = new User(1, "Jon", "Doe", "New York", 30);
    private final User userDifferentLastName = new User(1, "John", "Roe", "New York", 30);
    private final User userDifferentCity = new User(1, "John", "Doe", "Chicago", 30);
    private final User userDifferentAge = new User(1, "John", "Doe", "New York", 31);
    private final User userNullFields = new User(0, null, null, null, 0);

    @Test
    void equals_UserToUser() {
        assertEquals(user1, user1);
    }

    @Test
    void userEqualsToCopy() {
        assertEquals(user1, user1Copy);
        assertEquals(user1Copy, user1);
    }


    @Test
    void not_equalss() {
        assertNotEquals(user1, user2);
    }

    @Test
    void equals_checkAllFields() {
        assertNotEquals(user1, userDifferentId);
        assertNotEquals(user1, userDifferentFirstName);
        assertNotEquals(user1, userDifferentLastName);
        assertNotEquals(user1, userDifferentCity);
        assertNotEquals(user1, userDifferentAge);
    }


    @Test
    void equals_checkNull() {
        assertNotEquals(user1, null);
    }

    @Test
    void hashCode_correct() {
        assertEquals(user1.hashCode(), user1Copy.hashCode());
    }

    @Test
    void hashCode_different() {
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void toString_correct() {
        String str = user1.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("firstName=John"));
        assertTrue(str.contains("lastName=Doe"));
        assertTrue(str.contains("city=New York"));
        assertTrue(str.contains("age=30"));
    }


    @Test
    void toString_shouldFollowExpectedFormat() {
        assertEquals("User[id=1, firstName=John, lastName=Doe, city=New York, age=30]",
                user1.toString());
    }
}