package domain;

import java.io.File;
import java.io.FileNotFoundException;
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
    public static void load() {
        loadItems();
        loadUsers();
    }

    /**
     * helper method for load() that handles data from items.csv
     */
    public static void loadItems() {
        String path = "src/main/resources/items.csv";
        File file = new File(path);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] elements = line.split(",");

                String type = elements[0];

                Item.Status status = switch (elements[3]) {
                    case "BORROWED" -> Item.Status.BORROWED;
                    case "IN_STORE" -> Item.Status.IN_STORE;
                    case "LOST" -> Item.Status.LOST;
                    default -> null;
                };

                switch (type) {
                    case "BOOK" -> items.add(new Book(elements[1], elements[2], status, elements[4], elements[5], elements[6]));
                    case "DVD" -> items.add(new DVD(elements[1], elements[2], status, elements[4], Integer.parseInt(elements[5])));
                    case "MAGAZINE" -> items.add(new Magazine(elements[1], elements[2], status, Integer.parseInt(elements[4]), elements[5]));
                    default -> {}
                }
            }
        } catch (FileNotFoundException e) {
            // ignore
        }
    }

    /**
     * helper method for load() that handles data from users.csv
     */
    public static void loadUsers() {
        String path = "src/main/resources/users.csv";
        File file = new File(path);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] elements = line.split(",");

                String type = elements[0];

                List<Item> borrowedItems = new ArrayList<>();
                String[] itemIDs = Arrays.copyOfRange(elements, 3, elements.length);
                for (String id : itemIDs) {
                    for (Item item : items) {
                        if (item.id.equals(id)) {
                            borrowedItems.add(item);
                            break;
                        }
                    }
                }

                switch (type) {
                    case "STUDENT" -> users.add(new Student(elements[1], elements[2], borrowedItems));
                    case "TEACHER" -> users.add(new Teacher(elements[1], elements[2], borrowedItems));
                    case "ADMIN" -> users.add(new Admin(elements[1], elements[2]));
                    default -> {}
                }
            }
        } catch (FileNotFoundException e) {
            // ignore
        }
    }


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
        return new HashMap<>();
        // TODO : implement.
    }

    public enum ItemType {
        BOOK,
        DVD,
        MAGAZINE
    }
}
