import domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibrarySearchTest {
    @BeforeEach
    void setUp() {
        Library.items = new ArrayList<>();
        Library.users = new ArrayList<>();
        Item.setNextId(1);
    }

    @AfterEach
    void reset() {
        Library.items = new ArrayList<>();
        Item.setNextId(1);
    }

    // -- streamSearch() tests --

    @Test
    @DisplayName("(stream) Finds item by title")
    void streamSearch_byTitle() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Hobbit", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
    }

    @Test
    @DisplayName("(stream) Finds item by author")
    void streamSearch_byAuthor() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Tolkien", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
    }

    @Test
    @DisplayName("(stream) Finds DVD by director")
    void streamSearch_byDirector() {
        Library.items.add(new DVD("00001", "Inception", Item.Status.IN_STORE, "Nolan", 148));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Nolan", false);

        assertEquals(1, results.get(Library.ItemType.DVD).size());
    }

    @Test
    @DisplayName("(stream) Finds magazine by publisher")
    void streamSearch_byPublisher() {
        Library.items.add(new Magazine("00001", "Time", Item.Status.IN_STORE, 42, "TimePublisher"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("TimePublisher", false);

        assertEquals(1, results.get(Library.ItemType.MAGAZINE).size());
    }

    @Test
    @DisplayName("(stream) Search is case-insensitive")
    void streamSearch_caseInsensitive() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("hobbit", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
    }

    @DisplayName("(stream) No results for unmatched keyword")
    @Test
    void streamSearch_noResults() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("xyz", false);

        assertTrue(results.get(Library.ItemType.BOOK).isEmpty());
        assertTrue(results.get(Library.ItemType.DVD).isEmpty());
        assertTrue(results.get(Library.ItemType.MAGAZINE).isEmpty());
    }

    @DisplayName("(stream) Ignore copies of same book")
    @Test
    void streamSearch_ignoreDuplicatesBooks() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));
        Library.items.add(new Book("00002", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Hobbit", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
    }

    @DisplayName("(stream) Ignore copies of same DVD")
    @Test
    void streamSearch_ignoreDuplicatesDVDs() {
        Library.items.add(new DVD("00001", "Inception", Item.Status.IN_STORE, "Nolan", 148));
        Library.items.add(new DVD("00002", "Inception", Item.Status.IN_STORE, "Nolan", 148));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Inception", false);

        assertEquals(1, results.get(Library.ItemType.DVD).size());
    }

    @DisplayName("(stream) Ignore copies of same magazine")
    @Test
    void streamSearch_ignoreDuplicatesMags() {
        Library.items.add(new Magazine("00001", "Time", Item.Status.IN_STORE, 42, "TimePublisher"));
        Library.items.add(new Magazine("00002", "Time", Item.Status.IN_STORE, 42, "TimePublisher"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Time", false);

        assertEquals(1, results.get(Library.ItemType.MAGAZINE).size());
    }

    @DisplayName("(stream) Results sorted by title")
    @Test
    void streamSearch_sortedByTitle() {
        Library.items.add(new Book("00001", "Zoo", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));
        Library.items.add(new Book("00002", "Apple", Item.Status.IN_STORE, "9780261102217", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Tolkien", true);

        List<Item> books = new ArrayList<>(results.get(Library.ItemType.BOOK));
        assertEquals("Apple", books.get(0).getTitle());
        assertEquals("Zoo", books.get(1).getTitle());
    }

    @DisplayName("(stream) Results sorted by id by default")
    @Test
    void streamSearch_sortedById() {
        Library.items.add(new Book("00002", "Zoo", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));
        Library.items.add(new Book("00001", "Apple", Item.Status.IN_STORE, "9780261102217", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Tolkien", false);

        List<Item> books = new ArrayList<>(results.get(Library.ItemType.BOOK));
        assertEquals("00001", books.get(0).getId());
        assertEquals("00002", books.get(1).getId());
    }

    @DisplayName("(stream) Results separated by type")
    @Test
    void streamSearch_separatedByType() {
        Library.items.add(new Book("00001", "Tolkien Stories", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));
        Library.items.add(new DVD("00002", "Tolkien Documentary", Item.Status.IN_STORE, "Nolan", 148));
        Library.items.add(new Magazine("00003", "Tolkien Monthly", Item.Status.IN_STORE, 42, "Publisher"));

        Map<Library.ItemType, Set<Item>> results = Library.streamSearch("Tolkien", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
        assertEquals(1, results.get(Library.ItemType.DVD).size());
        assertEquals(1, results.get(Library.ItemType.MAGAZINE).size());
    }

    // -- recursiveSearch() --

    @Test
    @DisplayName("(recursive) Finds item by title")
    void recursiveSearch_byTitle() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Hobbit", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
    }

    @Test
    @DisplayName("(recursive) Finds item by author")
    void recursiveSearch_byAuthor() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Tolkien", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
    }

    @Test
    @DisplayName("(recursive) Finds DVD by director")
    void recursiveSearch_byDirector() {
        Library.items.add(new DVD("00001", "Inception", Item.Status.IN_STORE, "Nolan", 148));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Nolan", false);

        assertEquals(1, results.get(Library.ItemType.DVD).size());
    }

    @Test
    @DisplayName("(recursive) Finds magazine by publisher")
    void recursiveSearch_byPublisher() {
        Library.items.add(new Magazine("00001", "Time", Item.Status.IN_STORE, 42, "TimePublisher"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("TimePublisher", false);

        assertEquals(1, results.get(Library.ItemType.MAGAZINE).size());
    }

    @Test
    @DisplayName("(recursive) Search is case-insensitive")
    void recursiveSearch_caseInsensitive() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("hobbit", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
    }

    @DisplayName("(recursive) No results for unmatched keyword")
    @Test
    void recursiveSearch_noResults() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("xyz", false);

        assertTrue(results.get(Library.ItemType.BOOK).isEmpty());
        assertTrue(results.get(Library.ItemType.DVD).isEmpty());
        assertTrue(results.get(Library.ItemType.MAGAZINE).isEmpty());
    }

    @DisplayName("(recursive) Ignore copies of same book")
    @Test
    void recursiveSearch_ignoreDuplicatesBooks() {
        Library.items.add(new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));
        Library.items.add(new Book("00002", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Hobbit", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
    }

    @DisplayName("(recursive) Ignore copies of same DVD")
    @Test
    void recursiveSearch_ignoreDuplicatesDVDs() {
        Library.items.add(new DVD("00001", "Inception", Item.Status.IN_STORE, "Nolan", 148));
        Library.items.add(new DVD("00002", "Inception", Item.Status.IN_STORE, "Nolan", 148));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Inception", false);

        assertEquals(1, results.get(Library.ItemType.DVD).size());
    }

    @DisplayName("(recursive) Ignore copies of same magazine")
    @Test
    void recursiveSearch_ignoreDuplicatesMags() {
        Library.items.add(new Magazine("00001", "Time", Item.Status.IN_STORE, 42, "TimePublisher"));
        Library.items.add(new Magazine("00002", "Time", Item.Status.IN_STORE, 42, "TimePublisher"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Time", false);

        assertEquals(1, results.get(Library.ItemType.MAGAZINE).size());
    }

    @DisplayName("(recursive) Results sorted by title")
    @Test
    void recursiveSearch_sortedByTitle() {
        Library.items.add(new Book("00001", "Zoo", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));
        Library.items.add(new Book("00002", "Apple", Item.Status.IN_STORE, "9780261102217", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Tolkien", true);

        List<Item> books = new ArrayList<>(results.get(Library.ItemType.BOOK));
        assertEquals("Apple", books.get(0).getTitle());
        assertEquals("Zoo", books.get(1).getTitle());
    }

    @DisplayName("(recursive) Results sorted by id by default")
    @Test
    void recursiveSearch_sortedById() {
        Library.items.add(new Book("00002", "Zoo", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));
        Library.items.add(new Book("00001", "Apple", Item.Status.IN_STORE, "9780261102217", "Tolkien", "Fantasy"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Tolkien", false);

        List<Item> books = new ArrayList<>(results.get(Library.ItemType.BOOK));
        assertEquals("00001", books.get(0).getId());
        assertEquals("00002", books.get(1).getId());
    }

    @DisplayName("(recursive) Results separated by type")
    @Test
    void recursiveSearch_separatedByType() {
        Library.items.add(new Book("00001", "Tolkien Stories", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"));
        Library.items.add(new DVD("00002", "Tolkien Documentary", Item.Status.IN_STORE, "Nolan", 148));
        Library.items.add(new Magazine("00003", "Tolkien Monthly", Item.Status.IN_STORE, 42, "Publisher"));

        Map<Library.ItemType, Set<Item>> results = Library.recursiveSearch("Tolkien", false);

        assertEquals(1, results.get(Library.ItemType.BOOK).size());
        assertEquals(1, results.get(Library.ItemType.DVD).size());
        assertEquals(1, results.get(Library.ItemType.MAGAZINE).size());
    }
}
