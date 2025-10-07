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
        return "";
    }

    public int getBalance() {
        return 0;
    }


    //EFFECTS: Returns list of all titles in history
    public List<String> getEntryTitles() {
        return null;
    }

    public FinancialEntry getEntry(int i) {

        return null;
    }

    //MODIFIES: THIS
    //EFFECTS: Adds new entry to end of history
    public void addEntry(FinancialEntry entry) {

    }


    //REQUIRES index must be not be a number bigger than history.size or less than 1
    //MODIFIES: THIS
    //EFFECTS: delete the entry at the index of history
    public void deleteEntry(int index) {

    }



}
