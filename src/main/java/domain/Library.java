package domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Library {
    public static List<User> users;
    public static List<Item> items;

    /**
     * exports user and item data to csv files
     */
    public static void export() {}

    /**
     * loads user and item data from csv files
     */
    public static void load() {}

    /**
     * searches available items in library that contain a given keyword
     * (in title, isbn, author. director, issue number, etc) using stream
     * @param keyword the keyword
     * @return a map of the relevant items, sorted by their type
     */
    public static Map<ItemType, Set<Item>> streamSearch(String keyword) {}

    /**
     * searches available items in library that contain a given keyword
     * (in title, isbn, author. director, issue number, etc) using recursion
     * @param keyword the keyword
     * @return a map of the relevant items, sorted by their type
     */
    public static Map<ItemType, Set<Item>> recursiveSearch(String keyword) {}

    public enum ItemType {
        BOOK,
        DVD,
        MAGAZINE
    }
}
