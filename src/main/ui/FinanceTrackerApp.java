package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.User;
import model.FinancialEntry;
import model.FinancialEntry.TransactionType;

public class FinanceTrackerApp {

    private Scanner input;
    private List<User> users;
    private User selectedUser;

    public FinanceTrackerApp() {
        users = new ArrayList<>();
        selectedUser = null;
        input = new Scanner(System.in);

        welcome();
    }

    private void welcome() {
        System.out.println("------------------------------------------");
        System.out.println("Welcome to my finance tracker console app!");
        System.out.println("------------------------------------------");
        mainMenu();

    }

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

    private void deleteUser() {
        System.out.println("Please type the name of the user whose account you want to delete:");

        String command = input.next();
        User userToDelete = null;

        // Find the matching user
        for (User user : users) {
            if (command.equals(user.getName())) {
                userToDelete = user;
                break; // stop once we find the user
            }
        }

        // If found, remove them
        if (userToDelete != null) {
            users.remove(userToDelete);
            System.out.println("User \"" + userToDelete.getName() + "\" deleted successfully.");
        } else {
            System.out.println("Not a valid user.");
        }

        mainMenu(); // go back to main menu
    }

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

    public void enterUser() {
        System.out.println("You are now in the financial account of: " + selectedUser.getName());
        System.out.println(selectedUser.getName() + " has a balance of " + selectedUser.getBalance());

        userChoices();

    }

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

    public void userEntries() {

        List<String> entries = selectedUser.getEntryTitles();

        if (entries.size() == 0) {
            System.out.println("This user has no financial entries which is why their balance is 0");

        } else {

            for (String title : entries) {
                System.out.println("- " + title);

            }
        }

        userEntriesChoices();

    }

    public void userEntriesChoices() {
        System.out.println("------------------------------------------");
        System.out.println(
                "Do you want to (a) add an entry, (b) delete an entry, (c) edit an entry, (d) return to user page?");

        String command = input.next();
        command = command.toLowerCase();

        if (command.equals("a")) {
            addUserEntry();
        } else if (command.equals("b")) {
            deleteUserEntry();
        } else if (command.equals("c")) {
            editUserEntry();
        } else if (command.equals("d")) {
            enterUser();
        } else {
            System.out.println("You have not inputed a valid command, please try again");
            userEntriesChoices();
        }
    }

    public void addUserEntry() {
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

    public void editUserEntry() {
        System.out.println("------------------------------------------");
        System.out.println("Here are this user's entries:");

        System.out.println("Type the number of the entry you want to edit (or 0 to go back):");

        if (input.hasNextInt()) {
            int index = input.nextInt();

            if (index == 0) {
                userEntries();
                return;
            }

            if (index >= 1 && index <= selectedUser.getHistory().size()) {
                FinancialEntry entry = selectedUser.getEntry(index);

                System.out.println("Enter the new title:");
                String newTitle = input.next();
                entry.setTitle(newTitle);

                TransactionType newType = getEntryType();
                entry.setType(newType);

                int newAmount = getUserAmount();
                entry.setAmount(newAmount);

                boolean newPlanned = wasPlanned();
                entry.setWasPlanned(newPlanned);

                System.out.println("Entry updated.");
            } else {
                System.out.println("Invalid number. Please try again.");
                editUserEntry();
                return;
            }
        } else {
            input.next(); // consume bad token
            System.out.println("Not a number. Please try again.");
            editUserEntry();
            return;
        }

        userEntries();
    }

}
