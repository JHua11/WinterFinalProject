package domain;

import exceptions.ItemLimitReachedException;
import exceptions.ItemUnavailableException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Student extends User {
    private static final int MAX_BOOKS = 5;
    /**
     * Allows students to borrow a book, up to 5 total
     * @param book the book to be borrowed
     * @return whether the operation was successful
     */
    @Override
    public boolean borrow(Item book) throws ItemLimitReachedException, ItemUnavailableException {
        if (borrowedItems.size() == MAX_BOOKS) {
            throw new ItemLimitReachedException();
        }
        if (book.status != Item.Status.IN_STORE) {
            throw new ItemUnavailableException();
        }
        if (!(book instanceof Book)) {
            return false;
        }
        borrowedItems.add(book);
        book.setStatus(Item.Status.BORROWED);
        return true;
    }
}
