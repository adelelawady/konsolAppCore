package com.konsol.core.domain;

import java.io.Serializable;
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
}
