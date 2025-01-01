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

        // Header
        receiptLines.add("");
        receiptLines.add(centerText("KONSOL GAMING"));
        receiptLines.add(centerText("PlayStation Receipt"));
        receiptLines.add(SEPARATOR);

        // Session Details
        receiptLines.add(formatLine("Session ID", session.getId()));
        receiptLines.add(formatLine("Device", session.getDevice().getName().toString()));
        receiptLines.add(formatLine("Type", session.getType().getName().toString()));

        // Time Details
        receiptLines.add(THIN_SEPARATOR);
        receiptLines.add(formatLine("Start Time", formatDateTime(session.getStartTime())));
        if (session.getEndTime() != null) {
            receiptLines.add(formatLine("End Time", formatDateTime(session.getEndTime())));
            receiptLines.add(formatLine("Duration", calculateDuration(session.getStartTime(), session.getEndTime())));
        }

        // Add Orders Table if invoice items exist
        if (session.getInvoice() != null && !session.getInvoice().getInvoiceItems().isEmpty()) {
            receiptLines.add(SEPARATOR);
            receiptLines.add(centerText("Orders"));
            receiptLines.add(THIN_SEPARATOR);

            // Table header with adjusted column widths
            receiptLines.add(String.format("%-24s %5s %10s", "Item", "Qty", "Price"));
            receiptLines.add(THIN_SEPARATOR);

            // Table rows
            session
                .getInvoice()
                .getInvoiceItems()
                .forEach(item -> {
                    String itemName = truncateString(item.getItem().getName(), 24);
                    String formattedPrice = String.format("%8.2f", item.getNetPrice().doubleValue());

                    receiptLines.add(String.format("%-24s %5d %8s", itemName, item.getQtyOut().intValue(), formattedPrice));
                });

            // Totals section
            receiptLines.add(THIN_SEPARATOR);
            receiptLines.add(formatRightAligned("Subtotal:", session.getInvoice().getTotalPrice()));
            if (session.getInvoice().getDiscount().compareTo(BigDecimal.ZERO) > 0) {
                receiptLines.add(formatRightAligned("Discount:", session.getInvoice().getDiscount()));
            }
            receiptLines.add(formatRightAligned("Net Total:", session.getInvoice().getNetPrice()));
        }

        // Session price and grand total
        receiptLines.add(SEPARATOR);
        receiptLines.add(formatRightAligned("Session Price:", session.getDeviceSessionsNetPrice()));

        BigDecimal grandTotal = session.getDeviceSessionsNetPrice();
        if (session.getInvoice() != null) {
            grandTotal = grandTotal.add(session.getInvoice().getNetPrice());
        }
        receiptLines.add(formatBoldLine("Grand Total:", grandTotal));

        // Footer
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

    private String formatRightAligned(String label, BigDecimal amount) {
        String formattedAmount = String.format("%.2f LYD", amount);
        int spaces = RECEIPT_WIDTH - label.length() - formattedAmount.length();
        return String.format("%s%s%s", label, " ".repeat(Math.max(0, spaces)), formattedAmount);
    }

    private String formatBoldLine(String label, BigDecimal amount) {
        String formattedAmount = String.format("%.2f LYD", amount);
        int spaces = RECEIPT_WIDTH - label.length() - formattedAmount.length();
        return String.format("%s%s%s", label, " ".repeat(Math.max(0, spaces)), formattedAmount);
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
        return String.format("%.2f LYD", price);
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
