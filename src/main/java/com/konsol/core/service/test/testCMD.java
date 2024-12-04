package com.konsol.core.service.test;

import com.konsol.core.service.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testCMD implements CommandLineRunner {

    private final BankService bankService;

    public testCMD(BankService bankService) {
        this.bankService = bankService;
    }

    /**
     * @param args incoming main method arguments
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        bankService.loadBankMovements("674ffcaab0c83957e9623138");

        System.out.println(bankService.calculateBankBalance("674ffcaab0c83957e9623138"));
    }
}
