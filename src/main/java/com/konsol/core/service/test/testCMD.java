package com.konsol.core.service.test;

import com.konsol.core.service.BankService;
import com.konsol.core.service.ItemService;
import java.time.Instant;
import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testCMD implements CommandLineRunner {

    private final BankService bankService;

    private final ItemService itemService;

    public testCMD(BankService bankService, ItemService itemService) {
        this.bankService = bankService;
        this.itemService = itemService;
    }

    /**
     * @param args incoming main method arguments
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // bankService.processBankTransactions("674ffcaab0c83957e9623138");

        System.out.println(bankService.calculateBankBalance("674ffcaab0c83957e9623138"));

        itemService.analyzeItem("674ffceb8c68a169aa4c8adf", null, null, null);

        itemService.getSalesChartData(
            "674ffceb8c68a169aa4c8adf",
            Date.from(Instant.parse("2024-12-03T06:56:17.411+00:00")),
            Date.from(Instant.parse("2024-12-08T06:56:17.411+00:00"))
        );
        //getSalesChartData

    }
}
