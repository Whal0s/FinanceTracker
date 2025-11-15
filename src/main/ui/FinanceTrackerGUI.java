package ui;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.FinancialEntry;
import model.FinancialEntry.TransactionType;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.FinanceJsonReader;
import persistence.FinanceJsonWriter;
import persistence.Writable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Represents a Swing-based GUI for the Finance Tracker application.
// Allows the user to:
// - view all entries (Xs) belonging to a selected user (Y),
// - add an entry to the selected user,
// - edit an existing entry for the selected user,
// - display a subset of entries that satisfy a criterion (unplanned entries),
// - save and load the state of the application to/from file, and
// - view a simple visual component (bar chart) summarizing the selected user's data.
@ExcludeFromJacocoGeneratedReport
public class FinanceTrackerGUI extends JFrame implements Writable {

    private static final String JSON_STORE = "./data/finances.json";

    private List<User> users;
    private User selectedUser;

    private FinanceJsonWriter jsonWriter;
    private FinanceJsonReader jsonReader;

    // Swing components
    private DefaultListModel<String> userListModel;
    private JList<String> userList;

    private DefaultListModel<String> entryListModel;
    private JList<String> entryList;
    private List<FinancialEntry> displayedEntries; // keeps track of what is actually shown

    private JLabel selectedUserLabel;
    private JLabel balanceLabel;
    private JTextArea entryDetailsArea;

    private JButton addUserButton;
    private JButton deleteUserButton;
    private JButton addEntryButton;
    private JButton showUnplannedButton;
    private JButton showAllEntriesButton;
    private JButton editEntryButton;
    private JButton saveButton;
    private JButton loadButton;

    private BalanceChartPanel chartPanel;

    // EFFECTS: launches the GUI
    public static void main(String[] args) {
        // Ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(FinanceTrackerGUI::new);
    }

    // MODIFIES: this
    // EFFECTS: constructs a new FinanceTrackerGUI with an empty user list,
    // initializes persistence and all Swing components, and makes
    // the main window visible
    public FinanceTrackerGUI() {
        super("Finance Tracker GUI");

        users = new ArrayList<>();
        selectedUser = null;
        displayedEntries = new ArrayList<>();

        initPersistence();
        initComponents();
        initLayout();
        initListeners();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JSON reader and writer for persistence
    private void initPersistence() {
        jsonWriter = new FinanceJsonWriter(JSON_STORE);
        jsonReader = new FinanceJsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: initializes Swing components used by the GUI
    private void initComponents() {
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        entryListModel = new DefaultListModel<>();
        entryList = new JList<>(entryListModel);
        entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectedUserLabel = new JLabel("No user selected");
        balanceLabel = new JLabel("Balance: 0");

        entryDetailsArea = new JTextArea(6, 25);
        entryDetailsArea.setEditable(false);
        entryDetailsArea.setLineWrap(true);
        entryDetailsArea.setWrapStyleWord(true);
        entryDetailsArea.setBorder(new EmptyBorder(5, 5, 5, 5));

        addUserButton = new JButton("Add User");
        deleteUserButton = new JButton("Delete User");

        addEntryButton = new JButton("Add Entry");
        showUnplannedButton = new JButton("Show Unplanned Entries");
        showAllEntriesButton = new JButton("Show All Entries");
        editEntryButton = new JButton("Edit Selected Entry");

        saveButton = new JButton("Save");
        loadButton = new JButton("Load");

        chartPanel = new BalanceChartPanel();
    }

    // MODIFIES: this
    // EFFECTS: constructs and arranges all panels and components in the main window
    @SuppressWarnings("methodlength")
    private void initLayout() {
        setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // ----- Left: Users panel -----
        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new BorderLayout(5, 5));
        usersPanel.setBorder(BorderFactory.createTitledBorder("Users (Y)"));

        JScrollPane userScroll = new JScrollPane(userList);
        usersPanel.add(userScroll, BorderLayout.CENTER);

        JPanel userButtonPanel = new JPanel();
        userButtonPanel.setLayout(new GridLayout(1, 2, 5, 5));
        userButtonPanel.add(addUserButton);
        userButtonPanel.add(deleteUserButton);

        usersPanel.add(userButtonPanel, BorderLayout.SOUTH);

        // ----- Center: Entries panel -----
        JPanel entriesPanel = new JPanel();
        entriesPanel.setLayout(new BorderLayout(5, 5));
        entriesPanel.setBorder(BorderFactory.createTitledBorder("Entries (Xs) for Selected User"));

        JScrollPane entryScroll = new JScrollPane(entryList);
        entriesPanel.add(entryScroll, BorderLayout.CENTER);

        JPanel entryTopPanel = new JPanel(new BorderLayout());
        entryTopPanel.add(selectedUserLabel, BorderLayout.WEST);
        entryTopPanel.add(balanceLabel, BorderLayout.EAST);

        entriesPanel.add(entryTopPanel, BorderLayout.NORTH);

        JPanel entryButtonsPanel = new JPanel();
        entryButtonsPanel.setLayout(new GridLayout(1, 4, 5, 5));
        entryButtonsPanel.add(addEntryButton);
        entryButtonsPanel.add(showUnplannedButton);
        entryButtonsPanel.add(showAllEntriesButton);
        entryButtonsPanel.add(editEntryButton);

        entriesPanel.add(entryButtonsPanel, BorderLayout.SOUTH);

        // ----- Right: Details + chart (visual component) -----
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(5, 5));

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Entry Details"));
        JScrollPane detailsScroll = new JScrollPane(entryDetailsArea);
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);

        chartPanel.setBorder(BorderFactory.createTitledBorder("Deposit vs Withdrawal (Visual)"));

        rightPanel.add(detailsPanel, BorderLayout.NORTH);
        rightPanel.add(chartPanel, BorderLayout.CENTER);

        // ----- Bottom: Save / Load -----
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(loadButton);
        bottomPanel.add(saveButton);

        // ----- Add all major sections -----
        add(usersPanel, BorderLayout.WEST);
        add(entriesPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: installs listeners for UI events (button presses, selections, window
    // closing)
    @SuppressWarnings("methodlength")
    private void initListeners() {
        // When a user is selected from the list
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = userList.getSelectedIndex();
                if (index >= 0 && index < users.size()) {
                    selectedUser = users.get(index);
                    updateSelectedUserInfo();
                    showAllEntriesForSelectedUser();
                }
            }
        });

        // When an entry is selected from the list
        entryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = entryList.getSelectedIndex();
                if (index >= 0 && index < displayedEntries.size()) {
                    FinancialEntry fe = displayedEntries.get(index);
                    showEntryDetails(fe);
                }
            }
        });

        // Add user
        addUserButton.addActionListener(e -> addUserViaDialog());

        // Delete user
        deleteUserButton.addActionListener(e -> deleteSelectedUser());

        // Add entry (X to Y)
        addEntryButton.addActionListener(e -> addEntryViaDialog());

        // Show unplanned entries
        showUnplannedButton.addActionListener(e -> showUnplannedEntries());

        // Show all entries
        showAllEntriesButton.addActionListener(e -> showAllEntriesForSelectedUser());

        // Edit selected entry
        editEntryButton.addActionListener(e -> editSelectedEntryViaDialog());

        // Save and Load
        saveButton.addActionListener(e -> saveFinanceData());
        loadButton.addActionListener(e -> loadFinanceData());

        // On window closing, ask user if they want to save before exit
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: updates labels and chart to reflect the currently selected user
    private void updateSelectedUserInfo() {
        if (selectedUser == null) {
            selectedUserLabel.setText("No user selected");
            balanceLabel.setText("Balance: 0");
            chartPanel.updateData(null);
        } else {
            selectedUserLabel.setText("User: " + selectedUser.getName());
            balanceLabel.setText("Balance: " + selectedUser.getBalance());
            chartPanel.updateData(selectedUser);
        }
    }

    // MODIFIES: this
    // EFFECTS: repopulates the user list model from the users field
    private void refreshUserList() {
        userListModel.clear();
        for (User u : users) {
            userListModel.addElement(u.getName());
        }
    }

    // MODIFIES: this
    // EFFECTS: shows all entries of the selected user in the entries list;
    // if no user is selected, does nothing
    private void showAllEntriesForSelectedUser() {
        if (selectedUser == null) {
            return;
        }
        displayedEntries.clear();
        displayedEntries.addAll(selectedUser.getHistory());

        entryListModel.clear();
        int i = 1;
        for (FinancialEntry fe : displayedEntries) {
            String typeLabel = fe.getType() == TransactionType.DEPOSIT ? "Deposit" : "Withdrawal";
            String plannedLabel = fe.getWasPlanned() ? "planned" : "unplanned";
            String line = i + ". " + typeLabel + " | " + fe.getTitle()
                    + " | amount: " + fe.getAmount() + " (" + plannedLabel + ")";
            entryListModel.addElement(line);
            i++;
        }

        chartPanel.updateData(selectedUser);
        entryDetailsArea.setText("");
    }

    // MODIFIES: this
    // EFFECTS: shows only unplanned entries of the selected user; if no user is
    // selected, shows a message dialog
    @SuppressWarnings("methodlength")
    private void showUnplannedEntries() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user first.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        displayedEntries.clear();
        entryListModel.clear();

        int i = 1;
        for (FinancialEntry fe : selectedUser.getHistory()) {
            if (!fe.getWasPlanned()) {
                displayedEntries.add(fe);
                String typeLabel = fe.getType() == TransactionType.DEPOSIT ? "Deposit" : "Withdrawal";
                String plannedLabel = "unplanned";
                String line = i + ". " + typeLabel + " | " + fe.getTitle()
                        + " | amount: " + fe.getAmount() + " (" + plannedLabel + ")";
                entryListModel.addElement(line);
                i++;
            }
        }

        if (displayedEntries.isEmpty()) {
            entryDetailsArea.setText("No unplanned entries for this user.");
        } else {
            entryDetailsArea.setText("Showing unplanned entries for " + selectedUser.getName() + ".");
        }

        chartPanel.updateData(selectedUser);
    }

    // MODIFIES: this
    // EFFECTS: shows a multi-step dialog to create a new user and adds it to the
    // list if the name is non-empty and not already present
    @SuppressWarnings("methodlength")
    private void addUserViaDialog() {
        String name = JOptionPane.showInputDialog(
                this,
                "Enter new user's name:",
                "Add User",
                JOptionPane.PLAIN_MESSAGE);

        if (name == null) {
            return; // user cancelled
        }

        name = name.trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Name cannot be empty.",
                    "Invalid Name",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (User u : users) {
            if (u.getName().equals(name)) {
                JOptionPane.showMessageDialog(
                        this,
                        "A user with that name already exists.",
                        "Duplicate User",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        users.add(new User(name));
        refreshUserList();
    }

    // MODIFIES: this
    // EFFECTS: deletes the currently selected user (if any) after confirmation
    @SuppressWarnings("methodlength")
    private void deleteSelectedUser() {
        int index = userList.getSelectedIndex();
        if (index < 0 || index >= users.size()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user to delete.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        User toDelete = users.get(index);
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete user \"" + toDelete.getName() + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            users.remove(index);
            selectedUser = null;
            refreshUserList();
            entryListModel.clear();
            displayedEntries.clear();
            entryDetailsArea.setText("");
            updateSelectedUserInfo();
        }
    }

    // MODIFIES: this, selectedUser
    // EFFECTS: shows a multi-step dialog to add a new entry (X) to the selected
    // user (Y); if no user is selected, shows a warning
    @SuppressWarnings("methodlength")
    private void addEntryViaDialog() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user first.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = JOptionPane.showInputDialog(
                this,
                "Enter entry title:",
                "New Entry",
                JOptionPane.PLAIN_MESSAGE);

        if (title == null) {
            return; // user cancelled
        }

        title = title.trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Title cannot be empty.",
                    "Invalid Title",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] typeOptions = { "Deposit", "Withdrawal" };
        int typeChoice = JOptionPane.showOptionDialog(
                this,
                "Is this a deposit or withdrawal?",
                "Entry Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                typeOptions,
                typeOptions[0]);

        if (typeChoice != 0 && typeChoice != 1) {
            return; // cancelled
        }

        TransactionType type = (typeChoice == 0)
                ? TransactionType.DEPOSIT
                : TransactionType.WITHDRAWAL;

        Integer amount = askForAmount();
        if (amount == null) {
            return; // cancelled
        }

        String[] plannedOptions = { "Planned", "Unplanned" };
        int plannedChoice = JOptionPane.showOptionDialog(
                this,
                "Was this planned?",
                "Planned?",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                plannedOptions,
                plannedOptions[0]);

        if (plannedChoice != 0 && plannedChoice != 1) {
            return; // cancelled
        }

        boolean wasPlanned = (plannedChoice == 0);

        FinancialEntry entry = new FinancialEntry(title, type, amount, wasPlanned);
        selectedUser.addEntry(entry);

        updateSelectedUserInfo();
        showAllEntriesForSelectedUser();
        JOptionPane.showMessageDialog(
                this,
                "Entry added.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // MODIFIES: this, selectedUser
    // EFFECTS: edits the currently selected entry for the selected user by
    // prompting the user for new values. If there is no selected user
    // or no selected entry, shows a warning dialog.
    @SuppressWarnings("methodlength")
    private void editSelectedEntryViaDialog() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user first.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int index = entryList.getSelectedIndex();
        if (index < 0 || index >= displayedEntries.size()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an entry to edit.",
                    "No Entry Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        FinancialEntry entry = displayedEntries.get(index);

        // Remember old values so we can correct the balance
        TransactionType oldType = entry.getType();
        int oldAmount = entry.getAmount();
        boolean oldPlanned = entry.getWasPlanned();
        String oldTitle = entry.getTitle();

        // --- Ask for new title ---
        String newTitle = JOptionPane.showInputDialog(
                this,
                "Enter new title (leave blank to keep current):",
                "Edit Entry",
                JOptionPane.PLAIN_MESSAGE);

        if (newTitle == null) {
            return; // cancelled
        }

        newTitle = newTitle.trim();
        if (newTitle.isEmpty()) {
            newTitle = oldTitle;
        }

        // --- Ask for new type ---
        String[] typeOptions = { "Deposit", "Withdrawal" };
        int defaultTypeIndex = (oldType == TransactionType.DEPOSIT) ? 0 : 1;

        int typeChoice = JOptionPane.showOptionDialog(
                this,
                "Choose new type:",
                "Edit Entry Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                typeOptions,
                typeOptions[defaultTypeIndex]);

        if (typeChoice != 0 && typeChoice != 1) {
            return; // cancelled
        }

        TransactionType newType = (typeChoice == 0)
                ? TransactionType.DEPOSIT
                : TransactionType.WITHDRAWAL;

        // --- Ask for new amount ---
        Integer newAmount = askForAmount();
        if (newAmount == null) {
            return; // cancelled
        }

        // --- Ask for new planned/unplanned ---
        String[] plannedOptions = { "Planned", "Unplanned" };
        int defaultPlannedIndex = oldPlanned ? 0 : 1;

        int plannedChoice = JOptionPane.showOptionDialog(
                this,
                "Was this planned?",
                "Edit Planned Status",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                plannedOptions,
                plannedOptions[defaultPlannedIndex]);

        if (plannedChoice != 0 && plannedChoice != 1) {
            return; // cancelled
        }

        boolean newPlanned = (plannedChoice == 0);

        // --- Apply balance correction (mirror console updateEntry logic) ---
        if (oldType == TransactionType.DEPOSIT) {
            selectedUser.setBalancePlus(-oldAmount);
        } else {
            selectedUser.setBalancePlus(oldAmount);
        }

        // --- Write new values into the entry ---
        entry.setTitle(newTitle);
        entry.setType(newType);
        entry.setAmount(newAmount);
        entry.setWasPlanned(newPlanned);

        if (newType == TransactionType.DEPOSIT) {
            selectedUser.setBalancePlus(newAmount);
        } else {
            selectedUser.setBalancePlus(-newAmount);
        }

        updateSelectedUserInfo();
        showAllEntriesForSelectedUser();
        JOptionPane.showMessageDialog(
                this,
                "Entry updated.",
                "Edit Entry",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // MODIFIES: this
    // EFFECTS: prompts user repeatedly for a valid non-negative integer amount,
    // or returns null if the user cancels the dialog
    @SuppressWarnings("methodlength")
    private Integer askForAmount() {
        while (true) {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter amount (whole number, >= 0):",
                    "Entry Amount",
                    JOptionPane.PLAIN_MESSAGE);

            if (input == null) {
                return null;
            }

            input = input.trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Amount cannot be empty.",
                        "Invalid Amount",
                        JOptionPane.ERROR_MESSAGE);
                continue;
            }

            try {
                int amt = Integer.parseInt(input);
                if (amt < 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Amount cannot be negative.",
                            "Invalid Amount",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                return amt;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid whole number.",
                        "Invalid Amount",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the details of the given entry in the details area
    private void showEntryDetails(FinancialEntry entry) {
        if (entry == null) {
            entryDetailsArea.setText("");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(entry.getTitle()).append("\n");

        String typeLabel = entry.getType() == TransactionType.DEPOSIT ? "Deposit" : "Withdrawal";
        sb.append("Type: ").append(typeLabel).append("\n");

        sb.append("Amount: ").append(entry.getAmount()).append("\n");

        String plannedLabel = entry.getWasPlanned() ? "Planned" : "Unplanned";
        sb.append("This entry was ").append(plannedLabel.toLowerCase()).append(".");

        entryDetailsArea.setText(sb.toString());
    }

    // MODIFIES: this
    // EFFECTS: saves the current state of the application to JSON_STORE and
    // shows a dialog indicating success or failure
    private void saveFinanceData() {
        try {
            jsonWriter.open();
            jsonWriter.write(this); // uses toJson()
            jsonWriter.close();
            JOptionPane.showMessageDialog(
                    this,
                    "Saved finance data to " + JSON_STORE,
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to write to file: " + JSON_STORE,
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the state of the application from JSON_STORE and updates
    // the UI to reflect the loaded data; if unable to read, shows an error
    private void loadFinanceData() {
        try {
            List<User> loaded = jsonReader.read();
            users = new ArrayList<>(loaded);
            selectedUser = null;
            refreshUserList();
            entryListModel.clear();
            displayedEntries.clear();
            entryDetailsArea.setText("");
            updateSelectedUserInfo();

            JOptionPane.showMessageDialog(
                    this,
                    "Loaded finance data from " + JSON_STORE,
                    "Load Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to read from file: " + JSON_STORE,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts the user to optionally save before closing the window,
    // then disposes the frame and exits
    private void handleWindowClosing() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Would you like to save before exiting?",
                "Save Before Exit",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (result == JOptionPane.CANCEL_OPTION) {
            return;
        }

        if (result == JOptionPane.YES_OPTION) {
            saveFinanceData();
        }

        dispose();
        System.exit(0);
    }

    // EFFECTS: returns the entire visible GUI state as a JSON object, containing
    // an array of the users and their data
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

    // MODIFIES: this
    // EFFECTS: sets the list of users used by the GUI; primarily intended for
    // testing
    public void setUsers(List<User> users) {
        this.users = new ArrayList<>(users);
        selectedUser = null;
        refreshUserList();
        entryListModel.clear();
        displayedEntries.clear();
        entryDetailsArea.setText("");
        updateSelectedUserInfo();
    }

    // EFFECTS: returns the current list of users; primarily intended for testing
    public List<User> getUsers() {
        return users;
    }
}