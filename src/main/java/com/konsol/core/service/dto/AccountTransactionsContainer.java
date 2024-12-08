package com.konsol.core.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Container for Account Transactions with pagination info
 */
public class AccountTransactionsContainer implements Serializable {

    private List<AccountTransactionsDTO> result;
    private long total;

    public List<AccountTransactionsDTO> getResult() {
        return result;
    }

    public void setResult(List<AccountTransactionsDTO> result) {
        this.result = result;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
