package persistence;

import model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Reads Finance app state (list of users) from JSON stored in a file.
public class FinanceJsonReader {
    private final String source;

    // REQUIRES: source is a valid path (e.g., "./data/finances.json")
    // EFFECTS: constructs reader to read from source file
    public FinanceJsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads users from file and returns them;
    // throws IOException if an error occurs reading data from file
    public List<User> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject root = new JSONObject(jsonData);
        return parseUsers(root);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder content = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get(source), StandardCharsets.UTF_8)) {
            content.append(line);
        }
        return content.toString();
    }

    // EFFECTS: parses users from JSON root and returns list
    private List<User> parseUsers(JSONObject root) {
        List<User> users = new ArrayList<>();
        JSONArray arr = root.optJSONArray("users");

        for (Object o : arr) {
            JSONObject ju = (JSONObject) o;
            users.add(User.fromJson(ju)); // uses your model's fromJson
        }

        return users;
    }
}