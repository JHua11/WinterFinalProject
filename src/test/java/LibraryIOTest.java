import domain.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static domain.Library.items;
import static domain.Library.users;
import static org.junit.jupiter.api.Assertions.*;


    import domain.*;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.function.Try;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class LibraryIOTest {
    private static final String ITEMS_PATH = "src/main/resources/items.csv";
    private static final String USERS_PATH = "src/main/resources/users.csv";

    // Helpers
    private String readFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) return "";
        return Files.readString(file.toPath());
    }

    private void writeFile(String path, String content) throws IOException {
        try(FileWriter filewriter = new FileWriter(new File(path))) {
            filewriter.write(content);
        } catch (IOException e) {
            // ignore
        }
    }

    @BeforeEach
    void setUp() {
        try (FileWriter ItemFileWriter = new FileWriter(new File(ITEMS_PATH), false);
             FileWriter UserFileWriter = new FileWriter(new File(USERS_PATH), false)) { // reset the files
            items = new ArrayList<>();
            users = new ArrayList<>();
            Item.setNextId(1);
            User.setNextId(1);
        } catch (IOException e) {
            //ignore
        }
    }

    @AfterEach
    void erase() {
        try (FileWriter ItemFileWriter = new FileWriter(new File(ITEMS_PATH), false);
             FileWriter UserFileWriter = new FileWriter(new File(USERS_PATH), false)) {
            items = new ArrayList<>();
            users = new ArrayList<>();
            Item.setNextId(1);
            User.setNextId(1);
        } catch (IOException e) {
            //ignore
        }
    }

    @DisplayName("Load Book")
    @Test
    void loadBook() throws IOException {
        writeFile(ITEMS_PATH, "BOOK,00001,The Hobbit,IN_STORE,3647281928374,Tolkien,Fantasy\n");

        Library.load();

        assertEquals(1, items.size());
        Book book = (Book) items.getFirst();
        assertEquals("00001", book.getId());
        assertEquals("The Hobbit", book.getTitle());
        assertEquals(Item.Status.IN_STORE, book.getStatus());
        assertEquals("3647281928374", book.getIsbn());
        assertEquals("Tolkien", book.getAuthor());
        assertEquals("Fantasy", book.getGenre());
    }

    @DisplayName("Load DVD")
    @Test
    void load_DVD() throws IOException {
        writeFile(ITEMS_PATH, "DVD,00001,Inception,BORROWED,Nolan,148\n");

        Library.load();

        assertEquals(1, items.size());
        DVD dvd = (DVD) items.getFirst();
        assertEquals("Inception", dvd.getTitle());
        assertEquals(Item.Status.BORROWED, dvd.getStatus());
        assertEquals("Nolan", dvd.getDirector());
        assertEquals(148, dvd.getDuration());
    }

    @DisplayName("Load Magazine")
    @Test
    void load_Mag() throws IOException {
        writeFile(ITEMS_PATH, "MAGAZINE,00001,Time,LOST,42,TimePublisher\n");

        Library.load();

        assertEquals(1, items.size());
        Magazine mag = (Magazine) items.getFirst();
        assertEquals("Time", mag.getTitle());
        assertEquals(Item.Status.LOST, mag.getStatus());
        assertEquals(42, mag.getIssueNumber());
        assertEquals("TimePublisher", mag.getPublisher());
    }

    @DisplayName("Load nextId")
    @Test
    void load_setsItemNextIdCorrectly() throws IOException {
        writeFile(ITEMS_PATH,
                "BOOK,00001,The Hobbit,IN_STORE,3647281928374,Tolkien,Fantasy\n" +
                        "DVD,00005,Inception,IN_STORE,Nolan,148\n");

        Library.load();

        assertEquals(6, Item.getNextId()); // max id was 5, so next should be 6
    }

    @DisplayName("Load student")
    @Test
    void load_Student() throws IOException {
        writeFile(USERS_PATH, "STUDENT,00001,Alice\n");

        Library.load();

        assertEquals(1, users.size());
        assertInstanceOf(Student.class, users.getFirst());
        assertEquals("00001", users.getFirst().getId());
        assertEquals("Alice", users.getFirst().getName());
    }

    @DisplayName("Load teacher ")
    @Test
    void load_Teacher() throws IOException {
        writeFile(USERS_PATH, "TEACHER,00001,Yi\n");

        Library.load();

        assertEquals(1, users.size());
        assertInstanceOf(Teacher.class, users.getFirst());
        assertEquals("00001", users.getFirst().getId());
        assertEquals("Yi", users.getFirst().getName());
    }

    @DisplayName("Load admin")
    @Test
    void loadAdmin() throws IOException {
        writeFile(USERS_PATH, "ADMIN,00001,Bob\n");

        Library.load();

        Admin admin = (Admin) users.getFirst();
        assertEquals("00001", admin.getId());
        assertEquals("Bob", admin.getName());
    }

    @DisplayName("Load borrowed items")
    @Test
    void load_BorrowedItems() throws IOException {
        writeFile(USERS_PATH, "STUDENT,00001,Alice,00001\n");
        writeFile(ITEMS_PATH, "BOOK,00001,The Hobbit,BORROWED,3647281928374,Tolkien,Fantasy\n");

        Library.load();

        User alice = users.getFirst();
        assertEquals(1, alice.getBorrowedItems().size());

        Item item = alice.getBorrowedItems().getFirst();
        assertEquals("00001", alice.getBorrowedItems().getFirst().getId());
        assertEquals("The Hobbit", alice.getBorrowedItems().getFirst().getTitle());
        assertInstanceOf(Book.class, item);
    }

    @DisplayName("Export -> load items")
    @Test
    void export_load_Items() throws IOException {
        items.addAll(Arrays.asList(
                new Book("00001", "The Hobbit", Item.Status.IN_STORE, "3647281928374", "Tolkien", "Fantasy"),
                new DVD("00002", "Inception", Item.Status.BORROWED, "Nolan", 148),
                new Magazine("00003", "Time", Item.Status.LOST, 42, "TimePublisher")
        ));

        Library.export();

        // reset items
        items = new ArrayList<>();
        Library.load();

        assertEquals(3, items.size());
        assertInstanceOf(Book.class, items.get(0));
        assertEquals("3647281928374", ((Book) items.get(0)).getIsbn());
        assertInstanceOf(DVD.class, items.get(1));
        assertEquals("Nolan", ((DVD)items.get(1)).getDirector());
        assertInstanceOf(Magazine.class, items.get(2));
        assertEquals("TimePublisher", ((Magazine)items.get(2)).getPublisher());
    }

    @DisplayName("Export -> load user + item")
    @Test
    void export_load_Users() throws IOException {
        Book book = new Book("00001", "The Hobbit", Item.Status.BORROWED, "3647281928374", "Tolkien", "Fantasy");
        items.add(book);
        Student alice = new Student("00001", "Alice", new ArrayList<>(Arrays.asList(book)));
        users.add(alice);

        Library.export();

        items = new ArrayList<>();
        users = new ArrayList<>();
        Library.load();

        assertEquals(1, users.size());
        assertEquals(1, users.getFirst().getBorrowedItems().size());
        assertEquals(alice, users.getFirst());
        assertEquals(book, users.getFirst().getBorrowedItems().getFirst());
    }
}


