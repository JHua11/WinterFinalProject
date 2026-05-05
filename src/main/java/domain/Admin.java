package domain;

import interfaces.Reportable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends User implements Reportable {

    public Admin(String name) {
        this.id = String.format("%05d", nextId++);
        this.name = name;
        // TODO: add to list of users in library
    }

    /**
     * Exports user and item data into the csv files
     */
    public static void backUpData() {

    }
}
