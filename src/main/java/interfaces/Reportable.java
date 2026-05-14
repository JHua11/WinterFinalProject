package interfaces;

import domain.Item;
import domain.Library;
import domain.User;

import java.util.List;

import static domain.Library.items;

public interface Reportable {
    /**
     * Creates a string including details for every in store item in the library
     * @return the created string
     */
    static String genInStoreItemReport() {
        List<Item> inStoreItems = items.stream()
                .filter(item -> item.getStatus().equals(Item.Status.IN_STORE))
                .toList();
        StringBuilder report = new StringBuilder();
        for (Item item : inStoreItems) {
            report.append(item).append("\n");
        }
        return report.toString();
    }

    /**
     * Creates a string including details for every borrowed item in the library
     * @return the created string
     */
    static String genBorrowedItemReport() {
        List<Item> borrowedItems = items.stream()
                .filter(item -> item.getStatus().equals(Item.Status.BORROWED))
                .toList();
        StringBuilder report = new StringBuilder();
        for (Item item : borrowedItems) {
            report.append(item).append("\n");
        }
        return report.toString();
    }

    /**
     * Creates a string including details for every lost item in the library
     * @return the created string
     */
    static String genLostItemReport() {
        List<Item> lostItems = items.stream()
                .filter(item -> item.getStatus().equals(Item.Status.LOST))
                .toList();
        StringBuilder report = new StringBuilder();
        for (Item item : lostItems) {
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
