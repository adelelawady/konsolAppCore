package com.konsol.core.domain;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "settings")
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * MainOptions
     */
    @NotNull
    @Field("MAIN_REPORT_FILE_DIR")
    private String MAIN_REPORT_FILE_DIR = "reports"; // UI

    @NotNull
    @Field("MAIN_SELECTED_STORE_ID")
    private String MAIN_SELECTED_STORE_ID = "default";

    @NotNull
    @Field("MAIN_PRINTER_NAME")
    private String MAIN_PRINTER_NAME = "default"; // UI

    @NotNull
    @Field("MAIN_CULTURE_NAME")
    private String MAIN_CULTURE_NAME = "ar-EG"; // UI

    /**
     * salesInvoiceOptions
     */
    @NotNull
    @Field("SALES_INVOICE_REPORT")
    private String SALES_INVOICE_REPORT = "Default"; // UI

    @Field("SALES_AUTO_PRINT_AFTER_SAVE")
    private boolean SALES_AUTO_PRINT_AFTER_SAVE = true; // UI

    @Field("SALES_PRINT_PREVIEW")
    private boolean SALES_PRINT_PREVIEW = true; // UI

    @NotNull
    @Field("SALES_SELECTED_PRINTER_NAME")
    private String SALES_SELECTED_PRINTER_NAME = "Default"; // UI

    @Field("SALES_CHECK_ITEM_QTY")
    private boolean SALES_CHECK_ITEM_QTY = true; // BE

    @Field("SALES_UPDATE_ITEM_QTY_AFTER_SAVE")
    private boolean SALES_UPDATE_ITEM_QTY_AFTER_SAVE = true; // BE

    @NotNull
    @Field("SALES_REPORT_PAGE_NAME")
    private String SALES_REPORT_PAGE_NAME = "sale"; // UI

    /**
     * purchaseInvoiceOptions
     */
    @NotNull
    @Field("PURCHASE_INVOICE_REPORT")
    private String PURCHASE_INVOICE_REPORT = "Default"; // UI

    @Field("PURCHASE_AUTO_PRINT_AFTER_SAVE")
    private boolean PURCHASE_AUTO_PRINT_AFTER_SAVE = true; // UI

    @Field("PURCHASE_PRINT_PREVIEW")
    private boolean PURCHASE_PRINT_PREVIEW = true; // UI

    @NotNull
    @Field("PURCHASE_SELECTED_PRINTER_NAME")
    private String PURCHASE_SELECTED_PRINTER_NAME = "Default"; // UI

    @Field("PURCHASE_CHECK_ITEM_QTY")
    private boolean PURCHASE_CHECK_ITEM_QTY = true; // BE

    @Field("PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE")
    private boolean PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE = true; // BE

    @NotNull
    @Field("PURCHASE_REPORT_PAGE_NAME")
    private String PURCHASE_REPORT_PAGE_NAME = "pur"; // UI
}
