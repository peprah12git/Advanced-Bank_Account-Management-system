package utils;

/** Console-based implementation of TablePrinter with ASCII borders. */
public class ConsoleTablePrinter implements TablePrinter {

    @Override
    public void printTable(String[] headers, String[][] data) {
        if (headers == null || headers.length == 0) {
            return;
        }

        if (data == null || data.length == 0) {
            printBorder(calculateColumnWidths(headers, null));
            printRow(headers, calculateColumnWidths(headers, null));
            printBorder(calculateColumnWidths(headers, null));
            System.out.println(
                    "| No data available. ".concat(" ".repeat(Math.max(0, calculateTotalWidth(headers) - 20)))
                            + "|");
            printBorder(calculateColumnWidths(headers, null));
            return;
        }

        int[] colWidths = calculateColumnWidths(headers, data);
        printBorder(colWidths);
        printRow(headers, colWidths);
        printBorder(colWidths);
        for (String[] row : data) {
            printRow(row, colWidths);
        }
        printBorder(colWidths);
    }

    private int[] calculateColumnWidths(String[] headers, String[][] data) {
        int[] widths = new int[headers.length];

        // user the header widths as default.
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        // find any column width larger than that of the header and set it as width
        if (data != null) {
            for (String[] row : data) {
                for (int i = 0; i < row.length && i < widths.length; i++) {
                    if (row[i] != null && row[i].length() > widths[i]) {
                        widths[i] = row[i].length();
                    }
                }
            }
        }
        return widths;
    }

    private int calculateTotalWidth(String[] headers) {
        int total = 0;
        for (String h : headers) {
            total += h.length() + 3; // add 3 for "| " at the beginning and " " at the end.
        }
        return total + 1; // add 1 for "|" at the end.
    }

    private void printBorder(int[] colWidths) {
        StringBuilder sb = new StringBuilder();
        for (int width : colWidths) {
            sb.append("+");
            sb.append("-".repeat(width + 2));
        }
        sb.append("+");
        System.out.println(sb.toString());
    }

    private void printRow(String[] row, int[] colWidths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < colWidths.length; i++) {
            sb.append("| ");
            String cell = (i < row.length && row[i] != null) ? row[i] : "";
            sb.append(String.format("%-" + (colWidths[i] + 1) + "s", cell));
        }
        sb.append("|");
        System.out.println(sb.toString());
    }
}