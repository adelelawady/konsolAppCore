package com.konsol.core.web.rest.api;

import com.konsol.core.service.FinancialDashboardService;
import com.konsol.core.service.api.dto.FinancialDashboardDTO;
import com.konsol.core.service.api.dto.FinancialSearchDTO;
import com.konsol.core.service.mapper.sup.BankBalanceMapper;
import com.konsol.core.web.api.FinancialApiDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * REST controller for managing {@link com.konsol.core.domain.Bank}.
 */
@Service
public class FinanicalReportResource implements FinancialApiDelegate {

    private final Logger log = LoggerFactory.getLogger(FinanicalReportResource.class);

    private static final String ENTITY_NAME = "bank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancialDashboardService financialDashboardService;

    public FinanicalReportResource(BankBalanceMapper bankBalanceMapper, FinancialDashboardService financialDashboardService) {
        this.financialDashboardService = financialDashboardService;
    }

    @Override
    public ResponseEntity<FinancialDashboardDTO> getFinancialDashboard(FinancialSearchDTO financialSearchDTO) {
        return ResponseEntity.ok(
            financialDashboardService.getDashboardData(
                financialSearchDTO.getStartDate(),
                financialSearchDTO.getEndDate(),
                financialSearchDTO.getStoreId(),
                financialSearchDTO.getAccountId(),
                financialSearchDTO.getBankId()
            )
        );
    }

    @Override
    public ResponseEntity<FinancialDashboardDTO> getPlaystationFinancialDashboard(FinancialSearchDTO financialSearchDTO) {
        return ResponseEntity.ok(
            financialDashboardService.getDashboardData(
                financialSearchDTO.getStartDate(),
                financialSearchDTO.getEndDate(),
                financialSearchDTO.getStoreId(),
                financialSearchDTO.getAccountId(),
                financialSearchDTO.getBankId()
            )
        );
    }
}
