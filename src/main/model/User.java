package model;

import java.util.ArrayList;
import java.util.List;

import model.FinancialEntry.TransactionType;

public class User {

    private String name;
    private int overallBalance;
    private List<FinancialEntry> history;

    public User(String name) {
        this.name = name;
        overallBalance = 0;
        history = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return overallBalance;
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

}
