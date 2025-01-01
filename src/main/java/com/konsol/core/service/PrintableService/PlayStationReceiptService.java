package com.konsol.core.service.PrintableService;

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
public class PlayStationReceiptService implements Printable {

    private static final int RECEIPT_WIDTH = 42;
    private static final String SEPARATOR = "==========================================";
    private static final String THIN_SEPARATOR = "------------------------------------------";
    private List<String> receiptLines;
    private PlayStationSession currentSession;
    private Font receiptFont;
    private int fontSize = 9;

    public PlayStationReceiptService() {
        this.receiptFont = new Font("Monospaced", Font.PLAIN, fontSize);
    }

    public void prepareReceipt(PlayStationSession session) {
        this.currentSession = session;
        this.receiptLines = new ArrayList<>();

        addHeader();
        addSessionDetails();
        addTimeDetails();
        addOrdersTable();
        addTotals();
        addFooter();
    }

    private void addHeader() {
        receiptLines.add("");
        receiptLines.add(centerText("KONSOL GAMING"));
        receiptLines.add(centerText("PlayStation Receipt"));
        receiptLines.add(SEPARATOR);
    }

    private void addSessionDetails() {
        receiptLines.add(formatLine("Session ID", currentSession.getId()));
        receiptLines.add(formatLine("Device", currentSession.getDevice().getName()));
        receiptLines.add(formatLine("Type", currentSession.getType().getName()));
    }

    private void addTimeDetails() {
        // receiptLines.add(THIN_SEPARATOR);
        //receiptLines.add(formatLine("Start Time", formatDateTime(currentSession.getStartTime())));
        if (currentSession.getEndTime() != null) {
            //   receiptLines.add(formatLine("End Time", formatDateTime(currentSession.getEndTime())));
            receiptLines.add(formatLine("Duration", calculateDuration(currentSession.getStartTime(), currentSession.getEndTime())));
        }
    }

    private void addOrdersTable() {
        if (currentSession.getInvoice() == null || currentSession.getInvoice().getInvoiceItems().isEmpty()) return;

        receiptLines.add(SEPARATOR);
        receiptLines.add(centerText("Orders"));
        receiptLines.add(THIN_SEPARATOR);
        receiptLines.add(String.format("%-24s %5s %10s", "Item", "Qty", "Price"));
        receiptLines.add(THIN_SEPARATOR);

        currentSession
            .getInvoice()
            .getInvoiceItems()
            .forEach(item -> {
                String itemName = truncateString(item.getItem().getName(), 24);
                boolean isArabic = isArabicText(itemName);

                // Format price and quantity
                String formattedPrice = formatPrice(item.getNetPrice());
                String quantity = String.valueOf(item.getQtyOut().intValue());

                if (isArabic) {
                    receiptLines.add(formatArabicLine(itemName, quantity, formattedPrice));
                } else {
                    receiptLines.add(String.format("%-24s %5s %8s", itemName, quantity, formattedPrice));
                }
            });
    }

    private void addTotals() {
        receiptLines.add(SEPARATOR);
        receiptLines.add(formatRightAligned("Session Price:", currentSession.getDeviceSessionsNetPrice()));

        BigDecimal grandTotal = currentSession.getDeviceSessionsNetPrice();
        if (currentSession.getInvoice() != null) {
            grandTotal = grandTotal.add(currentSession.getInvoice().getNetPrice());
        }
        receiptLines.add(formatBoldLine("Grand Total:", grandTotal));
    }

    private void addFooter() {
        receiptLines.add(SEPARATOR);
        receiptLines.add(centerText("Thank you for visiting!"));
        receiptLines.add(centerText("Please come again"));
        receiptLines.add("");
    }

    private String truncateString(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private boolean isArabicText(String text) {
        if (text == null) return false;
        return text.chars().anyMatch(c -> Character.UnicodeBlock.of(c) == Character.UnicodeBlock.ARABIC);
    }

    private String formatArabicLine(String item, String quantity, String price) {
        return String.format("%-10s %5s %-24s", price, quantity, item);
    }

    private String formatRightAligned(String label, BigDecimal amount) {
        String formattedAmount = String.format("%.2f", amount);
        int spaces = RECEIPT_WIDTH - label.length() - formattedAmount.length();
        return String.format("%s%s%s", label, " ".repeat(Math.max(0, spaces)), formattedAmount);
    }

    private String formatBoldLine(String label, BigDecimal amount) {
        return formatRightAligned(label, amount);
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

    private String calculateDuration(Instant start, Instant end) {
        if (start == null || end == null) return "";
        Duration duration = Duration.between(start, end);
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
        return String.format("%-12s %s", label + ":", value);
    }

    public void setFontSize(int size) {
        this.fontSize = size;
        this.receiptFont = new Font("Monospaced", Font.PLAIN, fontSize);
    }
}
