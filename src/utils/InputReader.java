package utils;

/** Interface for reading user input with validation. */
public interface InputReader {

    /**  user presses Enter to continue. */
    void waitForEnter();

    /**
     * Reads an integer entered by the user that is in the range.
     *
     * @param prompt the message to display
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return the validated integer input
     */
    int readInt(String prompt, int min, int max);

    /**
     * Reads a double greater than or equal to the minimum.
     *
     * @param prompt the message to display
     * @param min the minimum allowed value
     * @return the validated double input
     */
    double readDouble(String prompt, double min);

    /**
     * Reads a non-empty string.
     *
     * @param prompt the message to display
     * @return the validated string input
     */
    String readString(String prompt);

    public String readAccountNumber(String prompt);
    public String readContact(String prompt);


}