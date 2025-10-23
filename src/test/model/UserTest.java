package model;

import static org.junit.Assert.assertEquals;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.FinancialEntry.TransactionType;

@ExcludeFromJacocoGeneratedReport
public class UserTest {

    private User u1;
    private User u2;
    private FinancialEntry f1;
    private FinancialEntry f2;
    private FinancialEntry f3;

    @BeforeEach
    void runBefore() {
        u1 = new User("James");
        u2 = new User("David");
        f1 = new FinancialEntry("Hospital fee", TransactionType.WITHDRAWAL, 3000, false);
        f2 = new FinancialEntry("June Income", TransactionType.DEPOSIT, 5000, true);
        f3 = new FinancialEntry("June Groceries", TransactionType.WITHDRAWAL, 400, true);
    }

    @Test
    void testConstructor() {
        assertEquals("James", u1.getName());
        assertEquals("David", u2.getName());
    }

    @Test
    void testAddEntry() {
        u1.addEntry(f1);
        u1.addEntry(f2);
        assertEquals(f1, u1.getHistory().get(0));
        assertEquals(f2, u1.getHistory().get(1));

    }

    @Test
    void testGetEntryTitles() {
        u1.addEntry(f1);
        u1.addEntry(f2);
        u1.addEntry(f3);

        List<String> list = new ArrayList<>();
        list.add("Hospital fee");
        list.add("June Income");
        list.add("June Groceries");
        assertEquals(list, u1.getEntryTitles());
    }

    @Test
    void testDeleteEntry() {
        u1.addEntry(f1);
        u1.addEntry(f2);
        u1.addEntry(f3);
        assertEquals(1600, u1.getBalance());

        u1.deleteEntry(1);
        assertEquals(4600 , u1.getBalance());
        assertEquals(f2, u1.getHistory().get(0));
        assertEquals(f3, u1.getHistory().get(1));

        u1.deleteEntry(1);
        assertEquals(-400 , u1.getBalance());
        assertEquals(f3, u1.getHistory().get(0));

    }

    @Test
    void testGetBalance() {
        u1.addEntry(f1);
        u1.addEntry(f2);
        assertEquals(2000, u1.getBalance());
    }

    @Test
    void testsetBalance() {
        u1.setBalancePlus(1000);
        assertEquals(1000, u1.getBalance());
    }

    @Test
    void testGetEntry() {
        u1.addEntry(f1);
        u1.addEntry(f2);
        assertEquals(f1, u1.getEntry(1));
    }
}
