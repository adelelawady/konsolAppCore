package com.konsol.core.service.test;

import static org.reflections.Reflections.log;

import com.konsol.core.service.BankService;
import com.konsol.core.service.FinancialDashboardService;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.SheftService;
import com.konsol.core.service.api.dto.FinancialDashboardDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testCMD implements CommandLineRunner {

    private final SheftService sheftService;

    public testCMD(SheftService sheftService) {
        this.sheftService = sheftService;
    }

    /**
     * @param args incoming main method arguments
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        //sheftService.startSheft();
    }
}
