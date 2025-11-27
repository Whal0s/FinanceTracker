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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        setupRendering(g2);

        int w = getWidth();
        int h = getHeight();
        int margin = 20;
        int max = Math.max(depositTotal, withdrawalTotal);

        if (max == 0) {
            drawNoDataMessage(g2, margin, h);
            return;
        }

        int chartWidth = w - 2 * margin;
        int chartHeight = h - 2 * margin;

        drawAxes(g2, w, h, margin);
        drawBars(g2, h, margin, chartWidth, chartHeight, max);
    }

    private void setupRendering(Graphics2D g2) {
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void drawNoDataMessage(Graphics2D g2, int margin, int h) {
        g2.drawString("No data to display", margin + 10, h / 2);
    }

    private void drawAxes(Graphics2D g2, int w, int h, int margin) {
        g2.drawLine(margin, h - margin, margin, margin);
        g2.drawLine(margin, h - margin, w - margin, h - margin);
    }

    private void drawBars(Graphics2D g2,
            int h,
            int margin,
            int chartWidth,
            int chartHeight,
            int max) {

        int barWidth = chartWidth / 4;
        int depositX = margin + barWidth;
        int withdrawalX = margin + 2 * barWidth;

        int depositBarHeight = computeBarHeight(depositTotal, max, chartHeight);
        int withdrawalBarHeight = computeBarHeight(withdrawalTotal, max, chartHeight);

        int depositY = computeBarY(h, margin, depositBarHeight);
        int withdrawalY = computeBarY(h, margin, withdrawalBarHeight);

        drawDepositBar(g2, barWidth, depositX, depositY, depositBarHeight);
        drawWithdrawalBar(g2, barWidth, withdrawalX, withdrawalY, withdrawalBarHeight);
    }

    private int computeBarHeight(int total, int max, int chartHeight) {
        return (int) ((double) total / max * (chartHeight - 20));
    }

    private int computeBarY(int h, int margin, int barHeight) {
        return h - margin - barHeight;
    }

    private void drawDepositBar(Graphics2D g2,
            int barWidth,
            int x,
            int y,
            int height) {

        g2.setColor(new Color(76, 175, 80)); // green-ish
        g2.fillRect(x, y, barWidth, height);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, barWidth, height);
        g2.drawString("Deposits: " + depositTotal, x - 10, y - 5);
    }

    private void drawWithdrawalBar(Graphics2D g2,
            int barWidth,
            int x,
            int y,
            int height) {

        g2.setColor(new Color(244, 67, 54)); // red-ish
        g2.fillRect(x, y, barWidth, height);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, barWidth, height);
        g2.drawString("Withdrawals: " + withdrawalTotal, x - 20, y - 5);
    }
}