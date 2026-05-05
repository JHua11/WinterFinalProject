package domain;

import lombok.*;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Book extends Item {
    private String isbn;
    private String author;
    private String genre;

    public Book(String id, String title, Status status, String isbn, String author, String genre) {
        super(id, title, status);
        this.isbn = isbn;
        this.author = author;
        this.genre = genre;
    }

    public Book(String title, String isbn, String author, String genre) {
        super(title);
        this.isbn = isbn;
        this.author = author;
        this.genre = genre;
    }
}
