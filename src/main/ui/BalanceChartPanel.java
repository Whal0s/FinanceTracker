package ui;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.FinancialEntry;
import model.FinancialEntry.TransactionType;
import model.User;

import javax.swing.*;
import java.awt.*;

// Represents a simple bar chart panel that visualizes the sum of deposits
// and withdrawals for a given user.
@ExcludeFromJacocoGeneratedReport
public class BalanceChartPanel extends JPanel {

    private int depositTotal;
    private int withdrawalTotal;

    // MODIFIES: this
    // EFFECTS: creates a BalanceChartPanel with zeroed totals
    public BalanceChartPanel() {
        depositTotal = 0;
        withdrawalTotal = 0;
        setPreferredSize(new Dimension(260, 200));
    }

    // MODIFIES: this
    // EFFECTS: recomputes deposit and withdrawal totals from the given user's
    // entries and repaints the chart; if user is null, totals are set to 0
    public void updateData(User user) {
        depositTotal = 0;
        withdrawalTotal = 0;

        if (user != null) {
            for (FinancialEntry fe : user.getHistory()) {
                if (fe.getType() == TransactionType.DEPOSIT) {
                    depositTotal += fe.getAmount();
                } else {
                    withdrawalTotal += fe.getAmount();
                }
            }
        }

        repaint();
    }

    // EFFECTS: paints a simple bar chart showing deposit vs withdrawal totals
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int margin = 20;
        int chartWidth = w - 2 * margin;
        int chartHeight = h - 2 * margin;

        // Draw axes
        g2.drawLine(margin, h - margin, margin, margin);
        g2.drawLine(margin, h - margin, w - margin, h - margin);

        int max = Math.max(depositTotal, withdrawalTotal);
        if (max == 0) {
            g2.drawString("No data to display", margin + 10, h / 2);
            return;
        }

        int barWidth = chartWidth / 4;
        int depositX = margin + barWidth;
        int withdrawalX = margin + 2 * barWidth;

        int depositBarHeight = (int) ((double) depositTotal / max * (chartHeight - 20));
        int withdrawalBarHeight = (int) ((double) withdrawalTotal / max * (chartHeight - 20));

        int depositY = h - margin - depositBarHeight;
        int withdrawalY = h - margin - withdrawalBarHeight;

        // Deposit bar
        g2.setColor(new Color(76, 175, 80)); // green-ish
        g2.fillRect(depositX, depositY, barWidth, depositBarHeight);
        g2.setColor(Color.BLACK);
        g2.drawRect(depositX, depositY, barWidth, depositBarHeight);
        g2.drawString("Deposits: " + depositTotal, depositX - 10, depositY - 5);

        // Withdrawal bar
        g2.setColor(new Color(244, 67, 54)); // red-ish
        g2.fillRect(withdrawalX, withdrawalY, barWidth, withdrawalBarHeight);
        g2.setColor(Color.BLACK);
        g2.drawRect(withdrawalX, withdrawalY, barWidth, withdrawalBarHeight);
        g2.drawString("Withdrawals: " + withdrawalTotal, withdrawalX - 20, withdrawalY - 5);
    }
}