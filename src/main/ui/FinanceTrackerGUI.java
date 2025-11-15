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

        addEntryButton = new JButton("Add Entry (X to Y)");
        showUnplannedButton = new JButton("Show Unplanned Entries");
        showAllEntriesButton = new JButton("Show All Entries");

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
        entryButtonsPanel.setLayout(new GridLayout(1, 3, 5, 5));
        entryButtonsPanel.add(addEntryButton);
        entryButtonsPanel.add(showUnplannedButton);
        entryButtonsPanel.add(showAllEntriesButton);

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

    

    
    
}