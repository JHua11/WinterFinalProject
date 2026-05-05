package domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Teacher extends User {
    private static final int MAX_ITEMS = 10;

    /**
     * Allows teachers to borrow an item, up to 10 total
     * @param item the book to be borrowed
     * @return whether the operation was successful
     */
    @Override
    public boolean borrow(Item item) {

    }
}
