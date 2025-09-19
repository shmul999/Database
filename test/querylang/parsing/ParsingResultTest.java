package querylang.parsing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParsingResultTest {

    @Test
    void of_correct() {
        ParsingResult<String> result = ParsingResult.of("success");
        assertTrue(result.isPresent());
        assertFalse(result.isError());
        assertEquals("success", result.getValue());
    }

    @Test
    void of_throwsException() {
        assertThrows(NullPointerException.class, () -> ParsingResult.of(null));
    }

    @Test
    void error_correct() {
        ParsingResult<String> result = ParsingResult.error("failure");
        assertFalse(result.isPresent());
        assertTrue(result.isError());
        assertEquals("failure", result.getErrorMessage());
    }

    @Test
    void error_throwsException() {
        assertThrows(NullPointerException.class, () -> ParsingResult.error(null));
    }

    @Test
    void map_correct() {
        ParsingResult<String> original = ParsingResult.of("test");
        ParsingResult<Integer> mapped = original.map(String::length);

        assertTrue(mapped.isPresent());
        assertEquals(4, mapped.getValue());
    }

    @Test
    void map_Error() {
        ParsingResult<String> original = ParsingResult.error("error");
        ParsingResult<Integer> mapped = original.map(String::length);

        assertTrue(mapped.isError());
        assertEquals("error", mapped.getErrorMessage());
    }

    @Test
    void map_throwsException() {
        ParsingResult<String> result = ParsingResult.of("test");
        assertThrows(NullPointerException.class, () -> result.map(null));
    }

}