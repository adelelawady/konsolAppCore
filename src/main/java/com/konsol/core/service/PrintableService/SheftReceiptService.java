package com.konsol.core.service.PrintableService;

import com.konsol.core.domain.Sheft;
import com.konsol.core.domain.playstation.PlayStationSession;
import java.awt.*;
import java.awt.print.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SheftReceiptService implements Printable {

    private static final int RECEIPT_WIDTH = 42;
    private static final String SEPARATOR = "==========================================";
    private static final String THIN_SEPARATOR = "------------------------------------------";
    private List<String> receiptLines;
    private Sheft currentSheft;
    private Font receiptFont;
    private int fontSize = 9;

    public SheftReceiptService() {
        this.receiptFont = new Font("Monospaced", Font.PLAIN, fontSize);
    }

    public void prepareReceipt(Sheft sheft) {
        this.currentSheft = sheft;
        this.receiptLines = new ArrayList<>();

        addHeader();
        addSheftDetails();
        addFinancialDetails();
        addSessionsSummary();
        addExpensesDetails();
        addTotals();
        addFooter();
    }

    private void addHeader() {
        receiptLines.add("");
        receiptLines.add(centerText("KONSOL GAMING"));
        receiptLines.add(centerText("Shift Report"));
        receiptLines.add(SEPARATOR);
    }

    private void addSheftDetails() {
        receiptLines.add(formatLine("Shift ID", currentSheft.getId()));
        receiptLines.add(formatLine("Employee", currentSheft.getAssignedEmployeeUser().getLogin()));
        receiptLines.add(formatLine("Start Time", formatDateTime(currentSheft.getStartTime())));
        receiptLines.add(formatLine("End Time", formatDateTime(currentSheft.getEndTime())));
        receiptLines.add(formatLine("Duration", formatDuration(currentSheft.getDuration())));
        receiptLines.add(THIN_SEPARATOR);
    }

    private void addFinancialDetails() {
        receiptLines.add(centerText("Financial Summary"));
        receiptLines.add(formatLine("Total Price", formatPrice(currentSheft.getTotalprice())));
        receiptLines.add(formatLine("Net Price", formatPrice(currentSheft.getNetPrice())));
        receiptLines.add(formatLine("Net Cost", formatPrice(currentSheft.getNetCost())));
        receiptLines.add(formatLine("Net User Price", formatPrice(currentSheft.getNetUserPrice())));
        receiptLines.add(formatLine("Discount", formatPrice(currentSheft.getDiscount())));
        receiptLines.add(THIN_SEPARATOR);
    }

    private void addSessionsSummary() {
        if (currentSheft.getSessions() != null && !currentSheft.getSessions().isEmpty()) {
            receiptLines.add(centerText("Sessions Summary"));
            receiptLines.add(String.format("Total Sessions: %d", currentSheft.getSessions().size()));
            receiptLines.add(THIN_SEPARATOR);
        }
    }

    private void addExpensesDetails() {
        receiptLines.add(centerText("Expenses & Additions"));
        receiptLines.add(formatLine("Invoice Expenses", formatPrice(currentSheft.getInvoicesExpenses())));
        receiptLines.add(formatLine("Shift Expenses", formatPrice(currentSheft.getSheftExpenses())));
        receiptLines.add(formatLine("Additions", formatPrice(currentSheft.getAdditions())));
        if (currentSheft.getAdditionsNotes() != null && !currentSheft.getAdditionsNotes().isEmpty()) {
            receiptLines.add("Additions Notes:");
            receiptLines.add(currentSheft.getAdditionsNotes());
        }
        receiptLines.add(THIN_SEPARATOR);
    }

    private void addTotals() {
        receiptLines.add(SEPARATOR);
        receiptLines.add(formatBoldLine("Total Invoices:", currentSheft.getTotalinvoices()));
        receiptLines.add(formatBoldLine("Total Net:", currentSheft.getNetPrice()));
    }

    private void addFooter() {
        receiptLines.add(SEPARATOR);
        if (currentSheft.getNotes() != null && !currentSheft.getNotes().isEmpty()) {
            receiptLines.add("Notes:");
            receiptLines.add(currentSheft.getNotes());
            receiptLines.add(SEPARATOR);
        }
        receiptLines.add(centerText("End of Shift Report"));
        receiptLines.add("");
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0 || receiptLines == null) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.setFont(receiptFont);

        float lineHeight = g2d.getFontMetrics().getHeight();
        float y = lineHeight;

        for (String line : receiptLines) {
            g2d.drawString(line, 0, y);
            y += lineHeight;
        }

        return PAGE_EXISTS;
    }

    private String formatDateTime(Instant instant) {
        if (instant == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    private String formatDuration(Duration duration) {
        if (duration == null) return "";
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%02d:%02d", hours, minutes);
    }

    private String formatPrice(BigDecimal price) {
        if (price == null) return "0.00";
        return String.format("%.2f", price);
    }

    private String centerText(String text) {
        if (text == null) return "";
        int padding = (RECEIPT_WIDTH - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    private String formatLine(String label, String value) {
        return String.format("%-16s %s", label + ":", value);
    }

    private String formatBoldLine(String label, BigDecimal amount) {
        String formattedAmount = formatPrice(amount);
        int spaces = RECEIPT_WIDTH - label.length() - formattedAmount.length();
        return String.format("%s%s%s", label, " ".repeat(Math.max(0, spaces)), formattedAmount);
    }

    public void setFontSize(int size) {
        this.fontSize = size;
        this.receiptFont = new Font("Monospaced", Font.PLAIN, fontSize);
    }
}
