package domain;

import lombok.*;

import java.util.Comparator;

import static domain.Library.items;

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
        items.add(this);
    }

    public enum Status {
        BORROWED,
        IN_STORE,
        LOST
    }

    @NoArgsConstructor
    public static class ItemIdComparator implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.id.compareTo(o2.id);
        }
    }

    @NoArgsConstructor
    public static class ItemTitleComparator implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.title.compareTo(o2.title);
        }
    }
}
