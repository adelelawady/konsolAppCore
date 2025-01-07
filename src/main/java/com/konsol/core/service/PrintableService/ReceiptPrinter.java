package com.konsol.core.service.PrintableService;

import java.awt.print.*;
import java.util.Arrays;
import javax.print.*;
import org.springframework.stereotype.Component;

@Component
public class ReceiptPrinter {

    private final PlayStationReceiptService receiptService;

    public ReceiptPrinter(PlayStationReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    public void print(String printerName, boolean isPdfPrinter) throws PrintException {
        PrinterJob job = PrinterJob.getPrinterJob();

        // Set the printer
        if (printerName != null && !printerName.isEmpty()) {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService selectedService = Arrays
                .stream(printServices)
                .filter(service -> service.getName().equalsIgnoreCase(printerName))
                .findFirst()
                .orElseThrow(() -> new PrintException("Printer not found: " + printerName));

            try {
                job.setPrintService(selectedService);
            } catch (PrinterException e) {
                throw new PrintException(e.getMessage());
            }
        } else {
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            if (defaultService != null) {
                try {
                    job.setPrintService(defaultService);
                } catch (PrinterException e) {
                    throw new PrintException(e.getMessage());
                }
            } else {
                throw new PrintException("No default printer found");
            }
        }

        // Set page format
        PageFormat pageFormat = job.defaultPage();
        if (isPdfPrinter) {
            // Use A4 for PDF
            Paper paper = new Paper();
            paper.setSize(595, 842); // A4 in points (72 points per inch)
            paper.setImageableArea(72, 72, 451, 698); // 1 inch margins
            pageFormat.setPaper(paper);
        } else {
            // Use receipt size for thermal printer
            Paper paper = new Paper();
            paper.setSize(226.8, 841.7); // 80mm width (at 72 dpi)
            paper.setImageableArea(0, 0, 226.8, 841.7); // No margins
            pageFormat.setPaper(paper);
        }

        pageFormat.setOrientation(PageFormat.PORTRAIT);
        job.setPrintable(receiptService, pageFormat);

        try {
            job.print();
        } catch (PrinterException e) {
            throw new PrintException("Failed to print: " + e.getMessage());
        }
    }

    public PrintService[] getAvailablePrinters() {
        return PrintServiceLookup.lookupPrintServices(null, null);
    }

    public String getDefaultPrinterName() {
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        return defaultService != null ? defaultService.getName() : null;
    }

    public boolean isPrinterSupported(String printerName) {
        try {

            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            return Arrays.stream(services).anyMatch(service -> service.getName().equalsIgnoreCase(printerName));
        } catch (Exception e) {
            return false;
        }
    }
}
