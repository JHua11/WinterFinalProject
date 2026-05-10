package interfaces;

import domain.Item;
import domain.Library;
import domain.User;

public interface Reportable {
    /**
     * Creates a string including details for every item in the library
     * @return the created string
     */
    static String genItemReport() {
        StringBuilder report = new StringBuilder();
        for (Item item : Library.items) {
            report.append(item).append("\n");
        }
        return report.toString();
    }

    /**
     * Creates a string including details for every user in the library
     * @return the created string
     */
    static String genUserReport() {
        StringBuilder report = new StringBuilder();
        for (User user : Library.users) {
            report.append(user).append("\n");
        }
        return report.toString();
    }
}
