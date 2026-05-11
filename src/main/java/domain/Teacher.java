package domain;

import exceptions.ItemLimitReachedException;
import exceptions.ItemUnavailableException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Teacher extends User {
    private static final int MAX_ITEMS = 10;

    public Teacher(String id, String name, List<Item> borrowedItems) {
        super(id, name, borrowedItems);
    }

    /**
     * Allows teachers to borrow an item, up to 10 total
     * @param item the book to be borrowed
     * @return whether the operation was successful
     */
    @Override
    public boolean borrow(Item item) throws ItemLimitReachedException, ItemUnavailableException {
        if (borrowedItems.size() == MAX_ITEMS) {
            throw new ItemLimitReachedException();
        }
        if (item.status != Item.Status.IN_STORE) {
            throw new ItemUnavailableException();
        }
        borrowedItems.add(item);
        item.setStatus(Item.Status.BORROWED);
        return true;
    }
}
