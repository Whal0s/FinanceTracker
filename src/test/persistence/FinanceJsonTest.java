package persistence;

import static org.junit.jupiter.api.Assertions.*;

import model.FinancialEntry;
import model.User;

import java.util.List;

public abstract class FinanceJsonTest {

    protected void checkUser(String expectedName, int expectedBalance, int expectedHistorySize, User actual) {
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedBalance, actual.getBalance());
        assertEquals(expectedHistorySize, actual.getHistory().size());
    }

    protected void checkEntry(String title, FinancialEntry.TransactionType type, int amount, boolean wasPlanned,
                              FinancialEntry actual) {
        assertEquals(title, actual.getTitle());
        assertEquals(type, actual.getType());
        assertEquals(amount, actual.getAmount());
        assertEquals(wasPlanned, actual.getWasPlanned());
    }

    protected User findUserByName(List<User> users, String name) {
        return users.stream().filter(u -> u.getName().equals(name)).findFirst().orElse(null);
    }
}