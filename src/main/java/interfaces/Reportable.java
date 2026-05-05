package interfaces;

public interface Reportable {
    /**
     * Creates a string including details for every user in the library
     * @return the created string
     */
    static String genItemReport() {}

    /**
     * Creates a string including details for every item in the library
     * @return the created string
     */
    static String genUserReport() {}
}
