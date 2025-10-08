package model;

import java.util.ArrayList;
import java.util.List;

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


    //EFFECTS: Returns list of all titles in history
    public List<String> getEntryTitles() {
        return null;
    }

    //EFFECTS: Returns a single entry !You must -1 the user input index
    public FinancialEntry getEntry(int i) {

        return null;
    }

    //EFFECTS: Returns history - THIS FUNCTION IS FOR TESTING!
    public List<FinancialEntry> getHistory() {
        return history;
    }

    //MODIFIES: THIS
    //EFFECTS: Adds new entry to end of history
    public void addEntry(FinancialEntry entry) {
        this.history.add(entry);
    }


    //REQUIRES index must be not be a number bigger than history.size or less than 1
    //MODIFIES: THIS
    //EFFECTS: delete the entry at the index of history
    public void deleteEntry(int index) {

    }



}
