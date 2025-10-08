package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.User;

public class FinanceTrackerApp {
    
    private Scanner input;
    private List<User> users;
    private User selectedUser;

    public FinanceTrackerApp(){
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
            System.out.println("There are currently no users so you will have to make one to begin.");
            createUser();

        } else {
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
            if (user.getName() == command){
                isInside = true;
            }
        }

        if (isInside){
            System.out.println("Name already exists, please select new name.");
            createUser();
        } else {
            users.add(new User(command));

        mainMenu();
        }
    }
    
    private void deleteUser() {
        //delete user
    }

    private void enterUserChoice() {
        System.out.println("Please type the name of the user whose account you want to check:");

        String command = input.next();
        command = command.toLowerCase();

        for (User user : users) {
            if (user.getName() == command) {
                

            }
        }

    }
}
