package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.User;
import model.FinancialEntry;
import model.FinancialEntry.TransactionType;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;


// Represents the overall app system. Stores a list of users which you can perform operations on 
// including viewing, delete or editing their financial entries
@ExcludeFromJacocoGeneratedReport
public class FinanceTrackerApp implements Writable{

    private Scanner input;
    private List<User> users;
    private User selectedUser;


    public List<User> getUsers() { 
        return users; 
    }

    @Override
    public JSONObject toJson() {
        JSONObject root = new JSONObject();
        JSONArray arr = new JSONArray();
        for (User u : users) {
            arr.put(u.toJson());
        }
        root.put("users", arr);
        return root;
    }

    // MODIFIES: THIS
    // EFFECTS: Initializes finance app with empty list of users and no selected user
    public FinanceTrackerApp() {
        users = new ArrayList<>();
        selectedUser = null;
        input = new Scanner(System.in);

        welcome();
    }

    // EFFECTS: Main welcome function
    private void welcome() {
        System.out.println("------------------------------------------");
        System.out.println("Welcome to my finance tracker console app!");
        System.out.println("------------------------------------------");
        mainMenu();

    }

    // EFFECTS: Enter the main menu and show users if there are any
    private void mainMenu() {
        if (users.size() == 0) {
            System.out.println("------------------------------------------");
            System.out.println("There are currently no users so you will have to make one to begin.");
            createUser();

        } else {
            System.out.println("------------------------------------------");
            System.out.println("Welcome to the main menu");
            System.out.println("Here are your following users:");
            for (User user : users) {
                System.out.println("- " + user.getName());
            }

            mainMenuChoices();

        }
    }

    // EFFECTS: Make a decision from the main menu to enter more functionality
    private void mainMenuChoices() {
        System.out.println("------------------------------------------");
        System.out.println("Do you want to (a) create a new user, (b) delete a user, (c) enter a user account?");

        String command = input.next();
        command = command.toLowerCase();

        if (command.equals("a")) {
            createUser();
        } else if (command.equals("b")) {
            deleteUser();
        } else if (command.equals("c")) {
            enterUserChoice();
        } else {
            System.out.println("You have not inputed a valid command, please try again");
            mainMenuChoices();
        }
    }

    // MODIFIES: THIS
    // EFFECTS: Create a new user to be added to users
    private void createUser() {
        System.out.println("------------------------------------------");
        System.out.println("What will your user's name be?");

        String command = input.next();

        Boolean isInside = false;
        for (User user : users) {
            if (user.getName() == command) {
                isInside = true;
            }
        }

        if (isInside) {
            System.out.println("Name already exists, please select new name.");
            createUser();
        } else {
            users.add(new User(command));

            mainMenu();
        }
    }

    // MODIFIES: THIS
    // EFFECTS: Delete a specific user from users
    private void deleteUser() {
        System.out.println("Please type the name of the user whose account you want to delete:");

        String command = input.next();
        User userToDelete = null;

        for (User user : users) {
            if (command.equals(user.getName())) {
                userToDelete = user;
                break;
            }
        }

        if (userToDelete != null) {
            users.remove(userToDelete);
            System.out.println("User \"" + userToDelete.getName() + "\" deleted successfully.");
        } else {
            System.out.println("Not a valid user.");
        }

        mainMenu();
    }

    // MODIFIES: THIS
    // EFFECTS: takes users input to enter the account of a the selected user
    private void enterUserChoice() {
        System.out.println("Please type the name of the user whose account you want to check:");

        String command = input.next();

        Boolean isValid = false;

        for (User user : users) {
            if (command.equals(user.getName())) {
                selectedUser = user;
                isValid = true;

            }
        }

        if (isValid) {
            enterUser();
        } else {
            System.out.println("You have not typed a valid user. Please try again.");
            enterUserChoice();
        }

    }

    // EFFECTS: Shows selected users balance and name
    public void enterUser() {
        System.out.println("------------------------------------------");
        System.out.println("You are now in the financial account of: " + selectedUser.getName());
        System.out.println(selectedUser.getName() + " has a balance of " + selectedUser.getBalance());

        userChoices();

    }

    // MODIFIES: THIS
    // EFFECTS: Gives the option to select actions related to user including check
    // entries or return to meny
    public void userChoices() {
        System.out.println("------------------------------------------");
        System.out.println("Do you want to (a) check user entries, (b) return to the menu?");

        String command = input.next();
        command = command.toLowerCase();

        if (command.equals("a")) {
            userEntries();
        } else if (command.equals("b")) {
            selectedUser = null;
            mainMenu();
        } else {
            System.out.println("You have not inputed a valid command, please try again");
            userChoices();
        }

    }

    // EFFECTS: Shows all selected user entries
    public void userEntries() {

        List<String> entries = selectedUser.getEntryTitles();

        if (entries.size() == 0) {
            System.out.println("------------------------------------------");
            System.out.println("This user has no financial entries which is why their balance is 0");

        } else {
            System.out.println("------------------------------------------");
            System.out.println("Here are the following entries:");
            for (String title : entries) {
                System.out.println("- " + title);

            }
        }

        userEntriesChoices();

    }

    // EFFECTS: Shows actions user can do after entering the entry section of a
    // user. They can (a) add an entry, (b) delete an entry, (c) edit an entry, (d)
    // return to user page?
    public void userEntriesChoices() {
        System.out.println("------------------------------------------");
        System.out.println(
                "Do you want to (a) add, (b) view, (c) delete, (d) edit, (e) return to user page?");

        String command = input.next();
        command = command.toLowerCase();

        if (command.equals("a")) {
            addUserEntry();
        } else if (command.equals("b")) {
            viewEntry();
        } else if (command.equals("c")) {
            deleteUserEntry();
        } else if (command.equals("d")) {
            editUserEntry();
        } else if (command.equals("e")) {
            enterUser();
        } else {
            System.out.println("You have not inputed a valid command, please try again");
            userEntriesChoices();
        }
    }

    // EFFECTS Choose entry to view
    public void viewEntry() {
        System.out.println("------------------------------------------");
        System.out.println("Type the number of the entry you want to edit (or 0 to go back):");

        if (input.hasNextInt()) {
            int index = input.nextInt();

            if (index == 0) {
                userEntries();
                return;
            }

            if (index >= 1 && index <= selectedUser.getHistory().size()) {
                viewSpecificEntry(selectedUser.getEntry(index));

            } else {
                System.out.println("Invalid number. Please try again.");
                editUserEntry();
                return;
            }
        } else {
            input.next();
            System.out.println("Not a number. Please try again.");
            editUserEntry();
            return;
        }
        userEntries();
    }

    // EFFECTS: Shows Entry contents
    public void viewSpecificEntry(FinancialEntry entry) {
        System.out.println("------------------------------------------");
        System.out.println("Title: " + entry.getTitle());

        String type;
        if (entry.getType().equals(TransactionType.DEPOSIT)) {
            type = "Deposit";

        } else {
            type = "Withdrawal";
        }

        System.out.println("Transaction Type: " + type);

        System.out.println("Amount: " + entry.getAmount());

        String wasPlanned;
        if (entry.getWasPlanned()) {
            wasPlanned = "was planned";

        } else {
            wasPlanned = "was unplanned";
        }

        System.out.println("This entry " + wasPlanned);

        userEntries();
    }

    // MODIFIES: THIS, USER
    // EFFECTS: Adds a financial entry to selected user
    public void addUserEntry() {
        System.out.println("------------------------------------------");
        System.out.println("What is the title of your entry?");
        String title = input.next();

        TransactionType type = getEntryType();

        int amount = getUserAmount();

        boolean wasPlanned = wasPlanned();

        FinancialEntry entry = new FinancialEntry(title, type, amount, wasPlanned);
        selectedUser.addEntry(entry);

        System.out.println("Entry added.");
        userEntries();
    }

    // EFFECTS: helper function to get an TransactionType back to the entry creation
    // function
    public TransactionType getEntryType() {
        System.out.println("------------------------------------------");
        System.out.println("Is this a (a) deposit or (b) withdrawal?");
        String command = input.next().toLowerCase();

        if (command.equals("a")) {
            return TransactionType.DEPOSIT;
        } else if (command.equals("b")) {
            return TransactionType.WITHDRAWAL;
        } else {
            System.out.println("You have not inputed a valid command, please try again");
            return getEntryType();
        }
    }

    // EFFECTS: helper function to get a number back to the entry creation function
    public int getUserAmount() {
        System.out.println("------------------------------------------");
        System.out.println("What is the amount? (enter a whole number)");

        // keep it simple: expect a valid int token
        if (input.hasNextInt()) {
            int amt = input.nextInt();
            if (amt < 0) {
                System.out.println("Amount cannot be negative. Please try again.");
                return getUserAmount();
            }
            return amt;
        } else {
            // consume invalid token and retry
            input.next();
            System.out.println("That is not a valid number. Please try again.");
            return getUserAmount();
        }
    }

    // EFFECTS: helper function to get a was planned boolean to the entry creation
    // function
    public boolean wasPlanned() {
        System.out.println("------------------------------------------");
        System.out.println("Was this planned? (y/n)");

        String command = input.next().toLowerCase();

        if (command.equals("y")) {
            return true;
        } else if (command.equals("n")) {
            return false;
        } else {
            System.out.println("You have not inputed a valid command, please try again");
            return wasPlanned();
        }
    }

    // EFFECTS: delete a specific entry based on the index inputted
    public void deleteUserEntry() {
        System.out.println("------------------------------------------");

        System.out.println("Type the number of the entry you want to delete (or 0 to go back):");

        if (input.hasNextInt()) {
            int index = input.nextInt();

            if (index == 0) {
                userEntries();
                return;
            }

            if (index >= 1 && index <= selectedUser.getHistory().size()) {
                selectedUser.deleteEntry(index);
                System.out.println("Entry deleted.");
            } else {
                System.out.println("Invalid number. Please try again.");
                deleteUserEntry();
                return;
            }
        } else {
            input.next(); // consume bad token
            System.out.println("Not a number. Please try again.");
            deleteUserEntry();
            return;
        }

        userEntries();
    }

    // MODIFIES: THIS, USER, FINANCIAL ENTRY
    // EFFECTS: select an entry from user history and modify it.
    public void editUserEntry() {
        System.out.println("------------------------------------------");
        System.out.println("Type the number of the entry you want to edit (or 0 to go back):");

        if (input.hasNextInt()) {
            int index = input.nextInt();

            if (index == 0) {
                userEntries();
                return;
            }

            if (index >= 1 && index <= selectedUser.getHistory().size()) {
                
                updateEntry(selectedUser.getEntry(index));

            } else {
                System.out.println("Invalid number. Please try again.");
                editUserEntry();
                return;
            }
        } else {
            input.next();
            System.out.println("Not a number. Please try again.");
            editUserEntry();
            return;
        }
        userEntries();
    }

    // MODIFIES: THIS, FinancialEntry
    // EFFECTS: helper function to update the specific fields of the selected
    // financial entry
    public void updateEntry(FinancialEntry entry) {
        if (entry.getType() == TransactionType.DEPOSIT) {
            selectedUser.setBalancePlus(-(entry.getAmount()));
        } else {
            selectedUser.setBalancePlus(entry.getAmount());
        }
        
        System.out.println("Enter the new title:");
        String newTitle = input.next();
        entry.setTitle(newTitle);

        TransactionType newType = getEntryType();
        entry.setType(newType);

        int newAmount = getUserAmount();
        entry.setAmount(newAmount);

        if (newType == TransactionType.DEPOSIT) {
            selectedUser.setBalancePlus(newAmount);
        } else {
            selectedUser.setBalancePlus(-newAmount);
        }

        boolean newPlanned = wasPlanned();
        entry.setWasPlanned(newPlanned);

        System.out.println("Entry updated.");
    }
}
