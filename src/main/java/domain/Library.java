package domain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Library {
    public static List<User> users;
    public static List<Item> items;

    /**
     * exports user and item data to csv files
     */
    public static void export() {
        exportItems();
        exportUsers();
    }

    /**
     * helper method for export() that handles the export to the items.csv file
     */
    private static void exportItems() {
        String path = "src/main/resources/items.csv";
        File file = new File(path);
        Collections.sort(items);
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Item item : items) {
                if (item instanceof Book) {
                    fileWriter.write(String.format("BOOK,%s,%s,%s,%s,%s,%s\n", item.id, item.title, item.status,
                            ((Book) item).getIsbn(), ((Book) item).getAuthor(), ((Book) item).getGenre()));
                } else if (item instanceof DVD) {
                    fileWriter.write(String.format("DVD,%s,%s,%s,%s,%d\n", item.id, item.title, item.status,
                            ((DVD) item).getDirector(), ((DVD) item).getDuration()));
                } else if (item instanceof Magazine) {
                    fileWriter.write(String.format("MAGAZINE,%s,%s,%s,%d,%s\n", item.id, item.title, item.status,
                            ((Magazine) item).getIssueNumber(), ((Magazine) item).getPublisher()));
                }
            }
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * helper method for export() that handles the export to the users.csv file
     */
    private static void exportUsers() {
        String path = "src/main/resources/users.csv";
        File file = new File(path);
        Collections.sort(users);
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (User user : users) {
                if (user instanceof Student) {
                    fileWriter.write(String.format("STUDENT,%s,%s", user.id, user.name));
                } else if (user instanceof Teacher) {
                    fileWriter.write(String.format("TEACHER,%s,%s", user.id, user.name));
                } else if (user instanceof Admin) {
                    fileWriter.write(String.format("ADMIN,%s,%s", user.id, user.name));
                }
                for (Item item : user.borrowedItems) {
                    fileWriter.write(item.id);
                }
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            // ignore
        }
    }

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
    public static Map<ItemType, Set<Item>> streamSearch(String keyword) {
        Map<ItemType, Set<Item>> map = new TreeMap<>();
        map.put(ItemType.BOOK, new TreeSet<>());
        map.put(ItemType.DVD, new TreeSet<>());
        map.put(ItemType.MAGAZINE, new TreeSet<>());
        List<Item> filteredItems = items.stream()
                .filter(item -> (item.toString().toLowerCase().contains(keyword.toLowerCase())))
                .toList();
        for (Item item : filteredItems) {
            if (item instanceof Book) {
                map.get(ItemType.BOOK).add(item);
            } else if (item instanceof DVD) {
                map.get(ItemType.DVD).add(item);
            } else if (item instanceof Magazine) {
                map.get(ItemType.MAGAZINE).add(item);
            }
        }
        return map;
    }

    /**
     * searches available items in library that contain a given keyword
     * (in title, isbn, author. director, issue number, etc) using recursion
     * @param keyword the keyword
     * @return a map of the relevant items, sorted by their type
     */
    public static Map<ItemType, Set<Item>> recursiveSearch(String keyword) {

    }

    public enum ItemType {
        BOOK,
        DVD,
        MAGAZINE
    }
}
