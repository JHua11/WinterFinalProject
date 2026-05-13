package domain;

import interfaces.Reportable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

import static domain.Library.users;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends User implements Reportable {
    public Admin(String id, String name) {
        super(id, name, new ArrayList<>());
    }

    public Admin(String name) {
        super(name);
    }

    @Override
    public boolean borrow(Item item) {
        return false; // admin accounts cannot borrow
    }

    /**
     * Exports user and item data into the csv files
     */
    public static void backUpData() {
        Library.export();
    }
}
