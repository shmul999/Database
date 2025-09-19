package querylang.parsing;

import querylang.db.Database;
import querylang.db.User;
import querylang.queries.*;
import querylang.result.InsertQueryResult;
import querylang.result.SelectQueryResult;
import querylang.util.FieldGetter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {
    @Test
    void parseClear_check() {
        QueryParser parser = new QueryParser();
        ParsingResult<Query> result = parser.parse("CLEAR");

        assertTrue(result.isPresent());
        assertTrue(result.getValue() instanceof ClearQuery);
    }

    @Test
    void parseClear_ThrowsError() {
        QueryParser parser = new QueryParser();
        ParsingResult<Query> result = parser.parse("CLEAR 123");

        assertTrue(result.isError());
        assertEquals("'CLEAR' doesn't accept arguments", result.getErrorMessage());
    }

    @Test
    void parseInsert_correct() {
        QueryParser parser = new QueryParser();
        ParsingResult<Query> result = parser.parse("INSERT (John, Doe, New York, 30)");

        assertTrue(result.isPresent());
        assertTrue(result.getValue() instanceof InsertQuery);
        Database db = new Database();
        InsertQueryResult queryResult = (InsertQueryResult) result.getValue().execute(db);

        assertTrue(queryResult.message().contains("was added successfully"));
        assertEquals(1, db.size());
    }

    @Test
    void parseInsert_Error() {
        QueryParser parser = new QueryParser();
        ParsingResult<Query> result = parser.parse("INSERT (Ivan, Ivanov, Moscow, not_num)");

        assertTrue(result.isError());
        assertEquals("Age must be a number", result.getErrorMessage());
    }

    @Test
    void parseRemove_correct() {
        QueryParser parser = new QueryParser();
        ParsingResult<Query> result = parser.parse("REMOVE 123");

        assertTrue(result.isPresent());
        assertTrue(result.getValue() instanceof RemoveQuery);
    }

    @Test
    void parseRemove_Error() {
        QueryParser parser = new QueryParser();
        ParsingResult<Query> result = parser.parse("REMOVE not_num");

        assertTrue(result.isError());
        assertEquals("ID must be integer", result.getErrorMessage());
    }


    @Test
    void parseSelect_FilteredFields() {
        QueryParser parser = new QueryParser();
        ParsingResult<Query> result = parser.parse("SELECT (firstName, age)");

        assertTrue(result.isPresent());
        assertTrue(result.getValue() instanceof SelectQuery);

        Database db = new Database();
        db.add(new User(1, "Ivan", "Ivanov", "Moscow", 30));

        SelectQueryResult queryResult = (SelectQueryResult) result.getValue().execute(db);
        String message = queryResult.message();

        assertTrue(message.contains("Ivan"));
        assertTrue(message.contains("30"));
        assertFalse(message.contains("Ivanov"));
        assertFalse(message.contains("Moscow"));
    }

}
