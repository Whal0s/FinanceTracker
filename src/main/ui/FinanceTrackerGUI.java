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

    
    
}