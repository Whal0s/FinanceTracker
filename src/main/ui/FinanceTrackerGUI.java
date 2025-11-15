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

    
}