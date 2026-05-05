package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class Teacher extends User {

    /**
     * Allows teachers to borrow an item, up to 10 total
     * @param item the book to be borrowed
     * @return whether the operation was successful
     */
    @Override
    public boolean borrow(Item item) {

    }
}
