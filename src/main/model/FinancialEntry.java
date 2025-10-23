package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a singular financial entry that will be stored in a user's history.
// A financial entry can be a deposit or withdrawal for a certain amount.
public class FinancialEntry implements Writable {

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL
    }

    private String title;
    private TransactionType type;
    private int amount;
    private Boolean wasPlanned;

    // MODIFIES: THIS
    // EFFECTS: Initializes Financial entry with set title type amount and was planned boolean
    public FinancialEntry(String title, TransactionType type, int amount, Boolean wasPlanned) {
        this.title = title;
        this.type = type;
        this.amount = amount;
        this.wasPlanned = wasPlanned;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for type
    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    // Getter and Setter for amount
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    // Getter and Setter for wasPlanned
    public Boolean getWasPlanned() {
        return wasPlanned;
    }

    public void setWasPlanned(Boolean wasPlanned) {
        this.wasPlanned = wasPlanned;
    }

    @Override
    // EFFECTS: returns this entry as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("type", type.name());
        json.put("amount", amount);
        json.put("wasPlanned", wasPlanned);
        return json;
    }

    // REQUIRES: json has valid keys as written by toJson
    // EFFECTS: builds a FinancialEntry from JSON
    public static FinancialEntry fromJson(JSONObject json) {
        String title = json.getString("title");
        TransactionType type = TransactionType.valueOf(json.getString("type"));
        int amount = json.getInt("amount");
        boolean wasPlanned = json.getBoolean("wasPlanned");
        FinancialEntry e = new FinancialEntry(title, type, amount, wasPlanned);
        return e;
    }

}
