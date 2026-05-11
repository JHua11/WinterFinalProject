package domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public abstract class Item implements Comparable<Item> {
    protected String id;
    protected String title;
    protected Status status;

    protected static int nextId = 1;

    @Override
    public int compareTo(Item o) {
        return id.compareTo(o.id);
    }

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
