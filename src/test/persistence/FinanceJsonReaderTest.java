package persistence;

import static org.junit.jupiter.api.Assertions.*;

import model.FinancialEntry;
import model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class FinanceJsonReaderTest extends FinanceJsonTest {

    private FinanceJsonReader reader;

    @Test
    public void testReaderNonExistentFile() {
        reader = new FinanceJsonReader("./data/no_such_file_finances.json");
        assertThrows(IOException.class, reader::read);
    }

    @Test
    public void testReaderEmptyUsers() {
        reader = new FinanceJsonReader("./data/test-finances-empty.json");
        try {
            List<User> users = reader.read();
            assertNotNull(users);
            assertEquals(0, users.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralUsers() {
        reader = new FinanceJsonReader("./data/test-finances-general.json");
        try {
            List<User> users = reader.read();
            assertEquals(2, users.size());

            User alice = findUserByName(users, "Alice");
            assertNotNull(alice);
            checkUser("Alice", 1200, 2, alice);
            checkEntry("Paycheck", FinancialEntry.TransactionType.DEPOSIT, 1500, true, alice.getHistory().get(0));
            checkEntry("Groceries", FinancialEntry.TransactionType.WITHDRAWAL, 300, false, alice.getHistory().get(1));

            User bob = findUserByName(users, "Bob");
            assertNotNull(bob);
            checkUser("Bob", -75, 1, bob);
            checkEntry("Late fee", FinancialEntry.TransactionType.WITHDRAWAL, 75, false, bob.getHistory().get(0));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}