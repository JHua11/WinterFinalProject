# Library Management System
**Final Project Report — Deliverable 4**

---

## 1. Introduction

This project implements a Library Management System in Java using object-oriented programming principles. The system allows three types of users — Students, Teachers, and Admins — to interact with a library collection consisting of Books, DVDs, and Magazines.

The core features of the system include borrowing and returning items with limit enforcement, searching the library collection using both stream-based and recursive algorithms, generating administrative reports, and persisting data through CSV file import and export.

The project was built using Maven and follows standard Java conventions, with JUnit Jupiter used for unit testing.

---

## 2. Design & Architecture

### 2.1 Class Hierarchy

The system is organized around two abstract base classes: `Item` and `User`. Both implement `Comparable` for natural ordering by ID, and both use static `nextId` counters to auto-generate unique five-digit IDs.

| Class | Type | Description |
|---|---|---|
| `Item` | Abstract | Base class for all library items; holds `id`, `title`, `status` |
| `Book` | Concrete | Extends `Item`; adds `ISBN`, `author`, `genre` |
| `DVD` | Concrete | Extends `Item`; adds `director`, `duration` |
| `Magazine` | Concrete | Extends `Item`; adds `issueNumber`, `publisher` |
| `User` | Abstract | Base class for all users; holds `id`, `name`, `borrowedItems` |
| `Student` | Concrete | Extends `User`; can borrow Books only, up to 5 |
| `Teacher` | Concrete | Extends `User`; can borrow any item, up to 10 |
| `Admin` | Concrete | Extends `User`, implements `Reportable`; manages backup and reports |
| `Library` | Utility | Static class holding all items and users; handles I/O and search |

### 2.2 Key Design Decisions

- `Item` and `User` each provide two constructors: one that accepts a full set of fields (used when loading from CSV), and one that only takes the essential fields and auto-generates the ID (used when creating new objects at runtime).
- The `Reportable` interface defines static methods for generating filtered reports (in-store, borrowed, lost items, and all users). `Admin` implements this interface.
- Custom exceptions (`ItemLimitReachedException`, `ItemUnavailableException`) are used to signal invalid borrow operations, making error handling explicit and testable.
- Two inner `Comparator` classes (`ItemIdComparator` and `ItemTitleComparator`) allow flexible sorting of search results without modifying the natural ordering defined by `compareTo`.

---

## 3. Features Implemented

### 3.1 User Management

The system supports three user types with distinct borrowing privileges:

| User Type | Can Borrow | Limit |
|---|---|---|
| Student | Books only | 5 items |
| Teacher | Books, DVDs, Magazines | 10 items |
| Admin | Cannot borrow | N/A |

Each user has a unique auto-generated ID, a name, and a list of currently borrowed items. Users are stored in `Library.users` and sorted by ID.

### 3.2 Item Management

Three item types are supported, each with type-specific attributes. Items are assigned a status of `IN_STORE`, `BORROWED`, or `LOST`. Multiple copies of the same item can exist with different IDs.

- **Book:** ISBN (validated as 13 digits), title, author, genre
- **DVD:** title, director, duration in minutes
- **Magazine:** title, issue number, publisher

### 3.3 Borrowing and Returning

The `borrow()` method is defined abstractly in `User` and overridden in each subclass. It enforces:

- Item type restrictions (Students can only borrow Books)
- Borrowing limits per user type
- Item availability (only `IN_STORE` items can be borrowed)

On a successful borrow, the item's status is set to `BORROWED` and it is added to the user's `borrowedItems` list. On return, the status is reset to `IN_STORE` and the item is removed from the list.

### 3.4 Searching

Two search implementations are provided, both of which are case-insensitive, search across all item fields (title, author, ISBN, director, publisher, etc.), deduplicate copies of the same item, and return results organized by item type in a `Map<ItemType, Set<Item>>`.

- **`streamSearch`:** Uses the Java Streams API with `filter()` and a `seen` set for avoiding duplication.
- **`recursiveSearch`:** Uses a recursive helper method that processes the item list one element at a time, passing a `seen` set through each recursive call.

Both methods accept a `sortByTitle` parameter. When `true`, results within each type are sorted alphabetically by title using `ItemTitleComparator`; otherwise they are sorted by ID using `ItemIdComparator`.

### 3.5 Admin Reports

The `Reportable` interface defines four static report methods, all accessible through `Admin`:

- `genInStoreItemReport()`: lists all items currently in store
- `genBorrowedItemReport()`: lists all currently borrowed items
- `genLostItemReport()`: lists all items marked as lost
- `genUserReport()`: lists all users and their details

### 3.6 Data Persistence

The `Library` class provides `load()` and `export()` methods for CSV-based persistence. Items and users are stored in separate CSV files under `src/main/resources/`.

On load, items are parsed first so that borrowed item references can be resolved when loading users. The `nextId` counters for both `Item` and `User` are updated after loading to prevent ID conflicts with newly created objects.

On export, both lists are sorted by ID before being written. `Admin.backUpData()` exposes this functionality to admin users.

---

## 4. Exception Handling

Two custom runtime exceptions handle invalid borrow operations:

| Exception | Thrown When |
|---|---|
| `ItemLimitReachedException` | User attempts to borrow beyond their allowed limit |
| `ItemUnavailableException` | User attempts to borrow an item that is `BORROWED` or `LOST` |

Both exceptions extend `RuntimeException` and provide a no-argument constructor and a message constructor for flexibility.

---

## 5. Sorting

The project provides sorting strategies for both users and items:

- `Item` and `User` implement `Comparable`, providing natural ordering by ID via `compareTo()`.
- `ItemIdComparator` and `ItemTitleComparator` allow alternative sort orders to be passed to collections or search methods at runtime.
- `Collections.sort()` is called on both lists before CSV export to ensure consistent file output.

---

## 6. Testing

Unit tests are written using JUnit Jupiter and organized into four test classes:

| Test Class | Coverage |
|---|---|
| `LibraryIOTest` | CSV load and export: all item types, all user types, borrowed item linking, `nextId` sync, export-to-import round-trip |
| `StreamSearchTest` | `streamSearch`: keyword matching by all fields, case-insensitivity, no results, uniqueness of results, sort by title and ID, type separation |
| `RecursiveSearchTest` | `recursiveSearch`: identical coverage to `StreamSearchTest` to verify behavioral consistency between both implementations |
| `BorrowReturnTest` | borrow/return: student and teacher limits, type restrictions, unavailable item exceptions, return status reset, admin cannot borrow |

A key testing challenge was that `Library.items` and `Library.users` are static fields, meaning state persists between tests. This was addressed by resetting both lists and the `nextId` counters in `@BeforeEach` and `@AfterEach` methods in each test class.

For `LibraryIOTest`, the real CSV resource files are used. Their contents are cleared before each test and restored after, avoiding interference with production data.

---

## 7. Challenges & Solutions

| Challenge | Solution |
|---|---|
| Recursive search mutated the caller's list via `removeFirst()` | Replaced with `subList(1, size())` to pass a view without modifying the original list |
| Search returning duplicate copies of the same item | Added a `seen` `HashSet` keyed on ISBN for Books and `title+publisher/director` for DVDs and Magazines; `seen.add()` returns `false` if already present, acting as a filter |
| Static `Library` state leaking between unit tests | Reset `Library.items`, `Library.users`, `Item.nextId`, and `User.nextId` in `@BeforeEach` and `@AfterEach` in every test class |
| `FileWriter` not flushing before `Scanner` reads in tests | Wrapped `writeFile` helper in try-with-resources so the writer is closed and flushed before `load()` reads the file |
| `nextId` counter resetting on restart, conflicting with loaded IDs | After loading each file, `nextId` is set to the last loaded ID plus one, since items are exported in sorted order |
| Lombok class-level `@Setter` generating protected setters for protected fields | Added field-level `@Setter` annotations on `nextId` fields to explicitly generate public setters needed for test setup |

---

## 8. Conclusion

The Library Management System successfully implements all required features: user and item management, borrowing and returning with limit enforcement, recursive and stream-based search with deduplication, admin reporting, and CSV-based data persistence.

The project demonstrates practical use of inheritance, polymorphism, abstract classes, interfaces, generics, Java collections, streams, recursion, exception handling, and file I/O.
