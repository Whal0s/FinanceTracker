package persistence;

import ui.FinanceTrackerApp;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class FinanceJsonWriter {
    private static final int TAB = 6;

    private PrintWriter writer;
    private final String destination;

    // REQUIRES: destination is a valid, writable path (e.g., "./data/finances.json")
    // EFFECTS: constructs writer to write to destination file
    public FinanceJsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination cannot be opened
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of app to file
    public void write(Writable w) {
        JSONObject json = w.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}