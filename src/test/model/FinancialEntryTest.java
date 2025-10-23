package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.FinancialEntry.TransactionType;

@ExcludeFromJacocoGeneratedReport
public class FinancialEntryTest {

    private FinancialEntry f1;
    private FinancialEntry f2;
    private FinancialEntry f3;

    @BeforeEach
    void runBefore() {
        f1 = new FinancialEntry("Hospital fee", TransactionType.WITHDRAWAL, 3000, false);
        f2 = new FinancialEntry("June Income", TransactionType.DEPOSIT, 5000, true);
        f3 = new FinancialEntry("June Groceries", TransactionType.WITHDRAWAL, 400, true);
    }

    @Test
    void testSetTitle() {
        assertEquals("Hospital fee", f1.getTitle());
        f1.setTitle("Coldplay Concert");
        assertEquals("Coldplay Concert", f1.getTitle());
    }

    @Test
    void testSetType() {
        assertEquals(TransactionType.WITHDRAWAL, f3.getType());
        f3.setType(TransactionType.DEPOSIT);
        assertEquals(TransactionType.DEPOSIT, f3.getType());
    }

    @Test
    void testSetAmount() {
        assertEquals(5000, f2.getAmount());
        f2.setAmount(2000);
        assertEquals(2000, f2.getAmount());
    }

    @Test
    void testSetWasPlanned() {
        assertFalse(f1.getWasPlanned());
        ;
        f1.setWasPlanned(true);
        assertTrue(f1.getWasPlanned());
        ;
    }

}
