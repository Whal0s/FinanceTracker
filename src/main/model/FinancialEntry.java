package model;

public class FinancialEntry {
    
    
    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL
    }


    private String title;
    private TransactionType type; 
    private int amount;
    private Boolean wasPlanned;

    public FinancialEntry(String title,TransactionType type, int amount, Boolean wasPlanned) {
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

}
