package com.konsol.core.service.test;

import static org.reflections.Reflections.log;

import com.konsol.core.service.BankService;
import com.konsol.core.service.FinancialDashboardService;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.api.dto.FinancialDashboardDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testCMD implements CommandLineRunner {

    private final BankService bankService;

    private final ItemService itemService;

    private final FinancialDashboardService financialDashboardService;

    public testCMD(BankService bankService, ItemService itemService, FinancialDashboardService financialDashboardService) {
        this.bankService = bankService;
        this.itemService = itemService;
        this.financialDashboardService = financialDashboardService;
    }

    /**
     * @param args incoming main method arguments
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        /*
        FinancialDashboardDTO financialDashboardDTO = financialDashboardService.getDashboardData(
            startDate,
            endDate,
            "674ffcaab0c83957e9623139",
            "675068e4e31429211961ed67",
            "674ffcaab0c83957e9623138"
        );

         */

        // log.debug("financialDashboardDTO : {}", financialDashboardDTO);
    }
}
