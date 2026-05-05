package domain;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public abstract class Item {
    protected String id;
    protected String title;
    protected Status status;

    protected static int nextId = 1;

    public Item(String title) {
        this.id = String.format("%05d", nextId++);
        this.title = title;
        this.status = Status.IN_STORE;
        // TODO: add to item list in library
    }

    public enum Status {
        BORROWED,
        IN_STORE,
        LOST
    }
}
