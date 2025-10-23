package model;

import java.util.ArrayList;
import java.util.List;

import model.FinancialEntry.TransactionType;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents a single user whose financial state we want to track. Made up of financial entries which 
// contribute to the overall balance.
public class User implements Writable {

    private String name;
    private int overallBalance;
    private List<FinancialEntry> history;

    // MODIFIES: THIS
    // EFFECTS: Initializes user with name and overall balance set to zero
    public User(String name) {
        this.name = name;
        overallBalance = 0;
        history = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setBalance(int balance) {
        this.overallBalance = balance;
    }

    public int getBalance() {
        return overallBalance;
    }

    public void setBalancePlus(int number) {
        this.overallBalance += number;
    }

    // EFFECTS: Returns list of all titles in history
    public List<String> getEntryTitles() {
        List<String> result = new ArrayList<>();

        for (FinancialEntry item : history) {
            result.add(item.getTitle());
        }

        return result;
    }

    // EFFECTS: Returns a single entry !You must -1 the user input index
    public FinancialEntry getEntry(int i) {

        return history.get(i - 1);
    }

    // EFFECTS: Returns history - THIS FUNCTION IS FOR TESTING!
    public List<FinancialEntry> getHistory() {
        return history;
    }

    // MODIFIES: THIS
    // EFFECTS: Adds new entry to end of history
    public void addEntry(FinancialEntry entry) {
        if (entry.getType() == TransactionType.DEPOSIT) {
            this.overallBalance += entry.getAmount();
        } else {
            this.overallBalance -= entry.getAmount();
        }
        this.history.add(entry);
    }

    // REQUIRES index must be not be a number bigger than history.size or less than
    // 1
    // MODIFIES: THIS
    // EFFECTS: delete the entry at the index of history
    public void deleteEntry(int index) {
        if (history.get(index - 1).getType() == TransactionType.DEPOSIT) {
            this.overallBalance -= history.get(index - 1).getAmount();
        } else {
            this.overallBalance += history.get(index - 1).getAmount();
        }

        history.remove(index - 1);

    }

    @Override
    // EFFECTS: returns this user as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("overallBalance", overallBalance);

        JSONArray hist = new JSONArray();
        for (FinancialEntry e : history) {
            hist.put(e.toJson());
        }
        json.put("history", hist);

        return json;
    }

    // REQUIRES: json has keys "name","overallBalance","history"
    // MODIFIES: none
    // EFFECTS: constructs a User and fills history without double-applying balance
    // updates
    public static User fromJson(JSONObject json) {
        User u = new User(json.getString("name"));
        u.setBalance(json.getInt("overallBalance"));
        JSONArray arr = json.getJSONArray("history");
        for (Object o : arr) {
            FinancialEntry fe = FinancialEntry.fromJson((JSONObject) o);
            // IMPORTANT: add to history *without* re-updating balance,
            // since we already set overallBalance from JSON.
            u.getHistory().add(fe);
        }
        return u;
    }

}
