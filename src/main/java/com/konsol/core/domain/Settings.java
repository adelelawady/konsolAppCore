package com.konsol.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "settings")
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * MainOptions
     */

    @NotNull
    @Field("MAIN_SELECTED_STORE_ID")
    private String MAIN_SELECTED_STORE_ID = "default";

    @NotNull
    @Field("MAIN_SELECTED_BANK_ID")
    private String MAIN_SELECTED_BANK_ID = "default";

    /**
     * salesInvoiceOptions
     */

    @Field("SALES_CHECK_ITEM_QTY")
    private boolean SALES_CHECK_ITEM_QTY = true; // BE

    @Field("SALES_UPDATE_ITEM_QTY_AFTER_SAVE")
    private boolean SALES_UPDATE_ITEM_QTY_AFTER_SAVE = true; // BE

    /**
     * purchaseInvoiceOptions
     */

    @Field("PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE")
    private boolean PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE = true; // BE

    @Field("PLAYSTATION_SELECTED_STORE_ID")
    private String PLAYSTATION_SELECTED_STORE_ID = "default";

    // BE
    @Field("PLAYSTATION_SELECTED_BANK_ID")
    private String PLAYSTATION_SELECTED_BANK_ID = "default";

    @Field("ALLOW_NEGATIVE_INVENTORY")
    private boolean ALLOW_NEGATIVE_INVENTORY = false; // BE

    @Field("SAVE_INVOICE_DELETETED_INVOICEITEMS")
    private boolean SAVE_INVOICE_DELETETED_INVOICEITEMS = true; // BE

    /**
     * Backup Settings
     */
    @Field("BACKUP_ENABLED")
    private boolean BACKUP_ENABLED = false;

    @Field("BACKUP_SCHEDULE_TYPE")
    private String BACKUP_SCHEDULE_TYPE = "DAILY";

    @Field("BACKUP_TIME")
    private String BACKUP_TIME = "23:00";

    @Field("BACKUP_DAYS")
    private List<String> BACKUP_DAYS = new ArrayList<>();

    @Field("BACKUP_RETENTION_DAYS")
    private int BACKUP_RETENTION_DAYS = 30;

    @Field("BACKUP_LOCATION")
    private String BACKUP_LOCATION = "C:/KonsolBackups";

    @Field("BACKUP_INCLUDE_FILES")
    private boolean BACKUP_INCLUDE_FILES = true;

    @Field("BACKUP_COMPRESS")
    private boolean BACKUP_COMPRESS = true;

    @Field("MONGODB_DUMP_PATH")
    private String MONGODB_DUMP_PATH = "C:/Program Files/MongoDB/Tools/100/bin/mongodump.exe";

    @Field("MONGODB_RESTORE_PATH")
    private String MONGODB_RESTORE_PATH = "C:/Program Files/MongoDB/Tools/100/bin/mongorestore.exe";

    @Field("PLAYSTATION_DEFAULT_PRINTER")
    private String PLAYSTATION_DEFAULT_PRINTER = "default";

    @Field("PLAYSTATION_PRINT_ON_CHECKOUT")
    private boolean PLAYSTATION_PRINT_ON_CHECKOUT = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * getters and setter
     */
    public String getMAIN_SELECTED_STORE_ID() {
        return MAIN_SELECTED_STORE_ID;
    }

    public void setMAIN_SELECTED_STORE_ID(String MAIN_SELECTED_STORE_ID) {
        this.MAIN_SELECTED_STORE_ID = MAIN_SELECTED_STORE_ID;
    }

    public String getMAIN_SELECTED_BANK_ID() {
        return MAIN_SELECTED_BANK_ID;
    }

    public void setMAIN_SELECTED_BANK_ID(String MAIN_SELECTED_BANK_ID) {
        this.MAIN_SELECTED_BANK_ID = MAIN_SELECTED_BANK_ID;
    }

    public boolean isSALES_CHECK_ITEM_QTY() {
        return SALES_CHECK_ITEM_QTY;
    }

    public void setSALES_CHECK_ITEM_QTY(boolean SALES_CHECK_ITEM_QTY) {
        this.SALES_CHECK_ITEM_QTY = SALES_CHECK_ITEM_QTY;
    }

    public boolean isSALES_UPDATE_ITEM_QTY_AFTER_SAVE() {
        return SALES_UPDATE_ITEM_QTY_AFTER_SAVE;
    }

    public void setSALES_UPDATE_ITEM_QTY_AFTER_SAVE(boolean SALES_UPDATE_ITEM_QTY_AFTER_SAVE) {
        this.SALES_UPDATE_ITEM_QTY_AFTER_SAVE = SALES_UPDATE_ITEM_QTY_AFTER_SAVE;
    }

    public boolean isPURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE() {
        return PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE;
    }

    public void setPURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE(boolean PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE) {
        this.PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE = PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE;
    }

    public String getPLAYSTATION_SELECTED_STORE_ID() {
        return PLAYSTATION_SELECTED_STORE_ID;
    }

    public void setPLAYSTATION_SELECTED_STORE_ID(String PLAYSTATION_SELECTED_STORE_ID) {
        this.PLAYSTATION_SELECTED_STORE_ID = PLAYSTATION_SELECTED_STORE_ID;
    }

    public String getPLAYSTATION_SELECTED_BANK_ID() {
        return PLAYSTATION_SELECTED_BANK_ID;
    }

    public void setPLAYSTATION_SELECTED_BANK_ID(String PLAYSTATION_SELECTED_BANK_ID) {
        this.PLAYSTATION_SELECTED_BANK_ID = PLAYSTATION_SELECTED_BANK_ID;
    }

    public boolean isALLOW_NEGATIVE_INVENTORY() {
        return ALLOW_NEGATIVE_INVENTORY;
    }

    public void setALLOW_NEGATIVE_INVENTORY(boolean ALLOW_NEGATIVE_INVENTORY) {
        this.ALLOW_NEGATIVE_INVENTORY = ALLOW_NEGATIVE_INVENTORY;
    }

    public boolean isSAVE_INVOICE_DELETETED_INVOICEITEMS() {
        return SAVE_INVOICE_DELETETED_INVOICEITEMS;
    }

    public void setSAVE_INVOICE_DELETETED_INVOICEITEMS(boolean SAVE_INVOICE_DELETETED_INVOICEITEMS) {
        this.SAVE_INVOICE_DELETETED_INVOICEITEMS = SAVE_INVOICE_DELETETED_INVOICEITEMS;
    }

    public boolean isBACKUP_ENABLED() {
        return BACKUP_ENABLED;
    }

    public void setBACKUP_ENABLED(boolean BACKUP_ENABLED) {
        this.BACKUP_ENABLED = BACKUP_ENABLED;
    }

    public String getBACKUP_SCHEDULE_TYPE() {
        return BACKUP_SCHEDULE_TYPE;
    }

    public void setBACKUP_SCHEDULE_TYPE(String BACKUP_SCHEDULE_TYPE) {
        this.BACKUP_SCHEDULE_TYPE = BACKUP_SCHEDULE_TYPE;
    }

    public String getBACKUP_TIME() {
        return BACKUP_TIME;
    }

    public void setBACKUP_TIME(String BACKUP_TIME) {
        this.BACKUP_TIME = BACKUP_TIME;
    }

    public List<String> getBACKUP_DAYS() {
        return BACKUP_DAYS;
    }

    public void setBACKUP_DAYS(List<String> BACKUP_DAYS) {
        this.BACKUP_DAYS = BACKUP_DAYS;
    }

    public int getBACKUP_RETENTION_DAYS() {
        return BACKUP_RETENTION_DAYS;
    }

    public void setBACKUP_RETENTION_DAYS(int BACKUP_RETENTION_DAYS) {
        this.BACKUP_RETENTION_DAYS = BACKUP_RETENTION_DAYS;
    }

    public String getBACKUP_LOCATION() {
        return BACKUP_LOCATION;
    }

    public void setBACKUP_LOCATION(String BACKUP_LOCATION) {
        this.BACKUP_LOCATION = BACKUP_LOCATION;
    }

    public boolean isBACKUP_INCLUDE_FILES() {
        return BACKUP_INCLUDE_FILES;
    }

    public void setBACKUP_INCLUDE_FILES(boolean BACKUP_INCLUDE_FILES) {
        this.BACKUP_INCLUDE_FILES = BACKUP_INCLUDE_FILES;
    }

    public boolean isBACKUP_COMPRESS() {
        return BACKUP_COMPRESS;
    }

    public void setBACKUP_COMPRESS(boolean BACKUP_COMPRESS) {
        this.BACKUP_COMPRESS = BACKUP_COMPRESS;
    }

    public String getMONGODB_DUMP_PATH() {
        return MONGODB_DUMP_PATH;
    }

    public void setMONGODB_DUMP_PATH(String MONGODB_DUMP_PATH) {
        this.MONGODB_DUMP_PATH = MONGODB_DUMP_PATH;
    }

    public String getMONGODB_RESTORE_PATH() {
        return MONGODB_RESTORE_PATH;
    }

    public void setMONGODB_RESTORE_PATH(String MONGODB_RESTORE_PATH) {
        this.MONGODB_RESTORE_PATH = MONGODB_RESTORE_PATH;
    }

    public String getPLAYSTATION_DEFAULT_PRINTER() {
        return PLAYSTATION_DEFAULT_PRINTER;
    }

    public void setPLAYSTATION_DEFAULT_PRINTER(String PLAYSTATION_DEFAULT_PRINTER) {
        this.PLAYSTATION_DEFAULT_PRINTER = PLAYSTATION_DEFAULT_PRINTER;
    }

    public boolean isPLAYSTATION_PRINT_ON_CHECKOUT() {
        return PLAYSTATION_PRINT_ON_CHECKOUT;
    }

    public void setPLAYSTATION_PRINT_ON_CHECKOUT(boolean PLAYSTATION_PRINT_ON_CHECKOUT) {
        this.PLAYSTATION_PRINT_ON_CHECKOUT = PLAYSTATION_PRINT_ON_CHECKOUT;
    }
}
