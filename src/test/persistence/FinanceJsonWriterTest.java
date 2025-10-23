package persistence;

import static org.junit.jupiter.api.Assertions.*;

import model.FinancialEntry;
import model.User;
import org.junit.jupiter.api.Test;
import ui.FinanceTrackerApp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class FinanceJsonWriterTest extends FinanceJsonTest {

    @Test
    public void testWriterInvalidFile() {
        FinanceJsonWriter writer = new FinanceJsonWriter("./data/\0illegal:name.json");
        assertThrows(FileNotFoundException.class, writer::open);
    }

    @Test
    public void testWriterAndReaderRoundTrip() {
        String path = "./data/test-finances-roundtrip.json";
        List<User> seed = new ArrayList<>();

        User alice = new User("Alice");
        alice.addEntry(new FinancialEntry("Paycheck", FinancialEntry.TransactionType.DEPOSIT, 1000, true));
        alice.addEntry(new FinancialEntry("Groceries", FinancialEntry.TransactionType.WITHDRAWAL, 200, true));

        User bob = new User("Bob");
        bob.addEntry(new FinancialEntry("Ticket", FinancialEntry.TransactionType.WITHDRAWAL, 50, true));

        seed.add(alice);
        seed.add(bob);

        FinanceTrackerApp app = new FinanceTrackerApp(seed);

        // write
        FinanceJsonWriter writer = new FinanceJsonWriter(path);
        try {
            writer.open();
            writer.write(app);
            writer.close();
        } catch (FileNotFoundException e) {
            fail("Should have been able to write");
        }

        // read back
        FinanceJsonReader reader = new FinanceJsonReader(path);
        try {
            List<User> loaded = reader.read();

            assertEquals(2, loaded.size());

            User a = findUserByName(loaded, "Alice");
            assertNotNull(a);
            // 1000 - 200 = 800
            checkUser("Alice", 800, 2, a);

            User b = findUserByName(loaded, "Bob");
            assertNotNull(b);
            checkUser("Bob", -50, 1, b);

        } catch (IOException e) {
            fail("Couldn't read back the file");
        }
    }
}