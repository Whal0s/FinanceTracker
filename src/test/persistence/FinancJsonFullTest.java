package persistence;

import static org.junit.jupiter.api.Assertions.*;

import model.FinancialEntry;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class FinancJsonFullTest {

    @Test
    public void testFinancialEntryToFromJson() {
        FinancialEntry e = new FinancialEntry("Rent", FinancialEntry.TransactionType.WITHDRAWAL, 1200, true);
        e.setWasPlanned(true);

        JSONObject json = e.toJson();
        FinancialEntry parsed = FinancialEntry.fromJson(json);

        assertEquals("Rent", parsed.getTitle());
        assertEquals(FinancialEntry.TransactionType.WITHDRAWAL, parsed.getType());
        assertEquals(1200, parsed.getAmount());
        assertTrue(parsed.getWasPlanned());
    }

    @Test
    public void testUserToFromJson() {
        User u = new User("Cara");
        // Simulate some history and balance changes using addEntry:
        u.addEntry(new FinancialEntry("Pay", FinancialEntry.TransactionType.DEPOSIT, 2000, true));
        u.addEntry(new FinancialEntry("Coffee", FinancialEntry.TransactionType.WITHDRAWAL, 5, true));
        // At this point, Cara's balance should be 1995
        assertEquals(1995, u.getBalance());

        // Serialize
        JSONObject json = u.toJson();

        // Deserialize
        User parsed = User.fromJson(json);

        // Should match exactly; fromJson should set balance directly and only append history
        assertEquals("Cara", parsed.getName());
        assertEquals(1995, parsed.getBalance());
        assertEquals(2, parsed.getHistory().size());
        assertEquals("Pay", parsed.getHistory().get(0).getTitle());
        assertEquals("Coffee", parsed.getHistory().get(1).getTitle());
    }

    @Test
    public void testRootEnvelopeShape() {
        User u1 = new User("X");
        u1.addEntry(new FinancialEntry("Deposit", FinancialEntry.TransactionType.DEPOSIT, 10, true));
        User u2 = new User("Y");

        JSONArray arr = new JSONArray();
        arr.put(u1.toJson());
        arr.put(u2.toJson());

        JSONObject root = new JSONObject();
        root.put("users", arr);

        assertTrue(root.has("users"));
        assertEquals(2, root.getJSONArray("users").length());
    }
}