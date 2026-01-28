package utils;

/** Interface for printing tabular data. */
public interface TablePrinter {

    /**
     * Prints a formatted table with headers and data rows.
     *
     * @param headers the column headers
     * @param data the table data rows
     */
    void printTable(String[] headers, String[][] data);
}