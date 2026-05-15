import domain.*;
import exceptions.ItemLimitReachedException;
import exceptions.ItemUnavailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class BorrowReturnTest {

    @DisplayName("Student can borrow book")
    @Test
    void studentBorrow() {
        Book book = new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy");
        Student student = new Student("Alice");
        assertTrue(student.borrow(book));
        assertEquals(1, student.getBorrowedItems().size());
    }

    @DisplayName("Student cannot borrow non-book")
    @Test
    void studentCannotBorrowNonBook() {
        DVD dvd = new DVD("Inception", "Nolan", 148);
        Magazine magazine = new Magazine("Time", 42, "TimePublisher");
        Student student = new Student("Alice");
        assertFalse(student.borrow(dvd));
        assertFalse(student.borrow(magazine));
    }

    @DisplayName("Student cannot borrow over 5 books")
    @Test
    void studentTooManyBooks() {
        Book book = new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy");
        Student student = new Student("Alice");
        student.getBorrowedItems().addAll(Arrays.asList(
                new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy")
        ));
        assertThrows(ItemLimitReachedException.class, () -> student.borrow(book));
        assertEquals(5, student.getBorrowedItems().size());
    }

    @DisplayName("Teacher can borrow any item")
    @Test
    void teacherBorrow() {
        Book book = new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy");
        DVD dvd = new DVD("Inception", "Nolan", 148);
        Magazine magazine = new Magazine("Time", 42, "TimePublisher");
        Teacher teacher = new Teacher("Yi");
        assertTrue(teacher.borrow(book));
        assertTrue(teacher.borrow(dvd));
        assertTrue(teacher.borrow(magazine));
        assertEquals(3, teacher.getBorrowedItems().size());
    }

    @DisplayName("Teacher cannot borrow over 10 items")
    @Test
    void teacherTooManyItems() {
        Teacher teacher = new Teacher("Yi");
        DVD dvd = new DVD("Inception", "Nolan", 148);
        teacher.getBorrowedItems().addAll(Arrays.asList(
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy"),
                    new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy")
                )
        );
        assertThrows(ItemLimitReachedException.class, () -> teacher.borrow(dvd));
        assertEquals(10, teacher.getBorrowedItems().size());
    }

    @DisplayName("Admin cannot borrow")
    @Test
    void adminBorrow() {
        Admin admin = new Admin("Bob");
        Book book = new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy");
        assertFalse(admin.borrow(book));
        assertEquals(0, admin.getBorrowedItems().size());
    }

    @DisplayName("Cannot borrow lost/borrowed item")
    @Test
    void borrowUnavailableItem() {
        DVD dvd = new DVD("Inception", "Nolan", 148);
        dvd.setStatus(Item.Status.LOST);
        Book book = new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy");
        book.setStatus(Item.Status.BORROWED);
        Teacher teacher = new Teacher("Yi");
        Student student = new Student("Alice");
        assertThrows(ItemUnavailableException.class, () -> teacher.borrow(dvd));
        assertThrows(ItemUnavailableException.class, () -> teacher.borrow(book));
        assertThrows(ItemUnavailableException.class, () -> student.borrow(dvd));
        assertThrows(ItemUnavailableException.class, () -> student.borrow(book));
        assertEquals(0, teacher.getBorrowedItems().size());
        assertEquals(0, student.getBorrowedItems().size());
    }

    @DisplayName("Return sets status to INSTORE and removes from borrowedItems")
    @Test
    void returnItem() {
        Teacher teacher = new Teacher("Yi");
        Book book = new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy");
        teacher.getBorrowedItems().add(book);
        book.setStatus(Item.Status.BORROWED);
        teacher.returnItem(book);
        assertEquals(Item.Status.IN_STORE, book.getStatus());
        assertEquals(0, teacher.getBorrowedItems().size());
    }

    @DisplayName("Returning item not borrowed returns false")
    @Test
    void returnNoItem() {
        Teacher teacher = new Teacher("Yi");
        Book book = new Book("The Hobbit", "3647281928374", "Tolkien", "Fantasy");
        assertFalse(teacher.returnItem(book));
        assertEquals(Item.Status.IN_STORE, book.getStatus());
    }
}
