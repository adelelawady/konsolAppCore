package com.konsol.core.config.dbmigrations;

import com.konsol.core.config.Constants;
import com.konsol.core.domain.Authority;
import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Store;
import com.konsol.core.domain.User;
import com.konsol.core.security.AuthoritiesConstants;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.time.Instant;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Creates the initial database setup.
 */
@ChangeUnit(id = "users-initialization", order = "001")
public class InitialSetupMigration {

    private final MongoTemplate template;

    public InitialSetupMigration(MongoTemplate template) {
        this.template = template;
    }

    @Execution
    public void changeSet() {
        Authority userAuthority = createUserAuthority();
        userAuthority = template.save(userAuthority);
        Authority adminAuthority = createAdminAuthority();
        adminAuthority = template.save(adminAuthority);

        Authority superAdminAuthority = createSuperAdminAuthority();
        superAdminAuthority = template.save(superAdminAuthority);

        createAllAuthorities(userAuthority, adminAuthority, superAdminAuthority);
        addUsers(userAuthority, adminAuthority, superAdminAuthority);
        createFirstBank();
        createFirstStore();
    }

    @RollbackExecution
    public void rollback() {}

    private Authority createAuthority(String authority) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(authority);
        return adminAuthority;
    }

    private Authority createAdminAuthority() {
        Authority adminAuthority = createAuthority(AuthoritiesConstants.ADMIN);
        return adminAuthority;
    }

    private Authority createUserAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.USER);
        return userAuthority;
    }

    private Authority createSuperAdminAuthority() {
        Authority superAdminAuthority = createAuthority(AuthoritiesConstants.SUPER_ADMIN);
        return superAdminAuthority;
    }

    private void addUsers(Authority userAuthority, Authority adminAuthority, Authority superAdminAuthority) {
        /*  CREATE USER */
        User user = createUser(userAuthority);
        template.save(user);

        /*  CREATE SUPER ADMIN */
        User admin = createAdmin(adminAuthority, userAuthority, null);
        template.save(admin);

        /*  CREATE SUPER ADMIN */
        User superAdmin = createAdmin(adminAuthority, userAuthority, superAdminAuthority);
        template.save(superAdmin);
    }

    private User createUser(Authority userAuthority) {
        User userUser = new User();
        userUser.setId("user-2");
        userUser.setLogin("user");
        userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        userUser.setFirstName("");
        userUser.setLastName("User");
        userUser.setEmail("user@localhost");
        userUser.setActivated(true);
        userUser.setLangKey("en");
        userUser.setCreatedBy(Constants.SYSTEM);
        userUser.setCreatedDate(Instant.now());
        userUser.getAuthorities().add(userAuthority);
        return userUser;
    }

    private User createAdmin(Authority adminAuthority, Authority userAuthority, Authority superAdminAuthority) {
        User adminUser = new User();
        adminUser.setId("user-1");
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(Constants.SYSTEM);
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        if (superAdminAuthority != null) {
            adminUser.getAuthorities().add(superAdminAuthority);
        }
        return adminUser;
    }

    private Bank createFirstBank() {
        Bank bank = new Bank();
        bank.name("Bank_1");
        return template.save(bank);
    }

    private Store createFirstStore() {
        Store store = new Store();
        store.name("Store_1");
        return template.save(store);
    }

    private void createAllAuthorities(Authority userAuthority, Authority adminAuthority, Authority superAdminAuthority) {
        // Basic Roles
        userAuthority.setCategory("Basic Roles");
        userAuthority.setDescription("Regular user with basic access");
        template.save(userAuthority);

        adminAuthority.setCategory("Basic Roles");
        adminAuthority.setDescription("Administrator with elevated privileges");
        template.save(adminAuthority);

        superAdminAuthority.setCategory("Basic Roles");
        superAdminAuthority.setDescription("Super Administrator with all privileges");
        template.save(superAdminAuthority);

        // Invoice Management
        Authority createInvoice = createAuthority(AuthoritiesConstants.CREATE_INVOICE);
        createInvoice.setCategory("Invoice Management");
        createInvoice.setDescription("Create new invoices");
        template.save(createInvoice);

        Authority updateInvoice = createAuthority(AuthoritiesConstants.UPDATE_INVOICE);
        updateInvoice.setCategory("Invoice Management");
        updateInvoice.setDescription("Modify existing invoices");
        template.save(updateInvoice);

        Authority deleteInvoice = createAuthority(AuthoritiesConstants.DELETE_INVOICE);
        deleteInvoice.setCategory("Invoice Management");
        deleteInvoice.setDescription("Delete invoices from the system");
        template.save(deleteInvoice);

        Authority viewInvoice = createAuthority(AuthoritiesConstants.VIEW_INVOICE);
        viewInvoice.setCategory("Invoice Management");
        viewInvoice.setDescription("View invoice details");
        template.save(viewInvoice);

        Authority saveInvoice = createAuthority(AuthoritiesConstants.SAVE_INVOICE);
        saveInvoice.setCategory("Invoice Management");
        saveInvoice.setDescription("Save invoice changes");
        template.save(saveInvoice);

        Authority printInvoice = createAuthority(AuthoritiesConstants.PRINT_INVOICE);
        printInvoice.setCategory("Invoice Management");
        printInvoice.setDescription("Print invoice documents");
        template.save(printInvoice);

        Authority cancelInvoice = createAuthority(AuthoritiesConstants.CANCEL_INVOICE);
        cancelInvoice.setCategory("Invoice Management");
        cancelInvoice.setDescription("Cancel existing invoices");
        template.save(cancelInvoice);

        // Store Management
        Authority createStore = createAuthority(AuthoritiesConstants.CREATE_STORE);
        createStore.setCategory("Store Management");
        createStore.setDescription("Create new stores");
        template.save(createStore);

        Authority updateStore = createAuthority(AuthoritiesConstants.UPDATE_STORE);
        updateStore.setCategory("Store Management");
        updateStore.setDescription("Modify store information");
        template.save(updateStore);

        Authority deleteStore = createAuthority(AuthoritiesConstants.DELETE_STORE);
        deleteStore.setCategory("Store Management");
        deleteStore.setDescription("Delete stores from the system");
        template.save(deleteStore);

        Authority viewStore = createAuthority(AuthoritiesConstants.VIEW_STORE);
        viewStore.setCategory("Store Management");
        viewStore.setDescription("View store details");
        template.save(viewStore);

        // Account Management
        Authority createAccount = createAuthority(AuthoritiesConstants.CREATE_ACCOUNT);
        createAccount.setCategory("Account Management");
        createAccount.setDescription("Create new accounts");
        template.save(createAccount);

        Authority updateAccount = createAuthority(AuthoritiesConstants.UPDATE_ACCOUNT);
        updateAccount.setCategory("Account Management");
        updateAccount.setDescription("Modify account information");
        template.save(updateAccount);

        Authority deleteAccount = createAuthority(AuthoritiesConstants.DELETE_ACCOUNT);
        deleteAccount.setCategory("Account Management");
        deleteAccount.setDescription("Delete accounts from the system");
        template.save(deleteAccount);

        Authority viewAccount = createAuthority(AuthoritiesConstants.VIEW_ACCOUNT);
        viewAccount.setCategory("Account Management");
        viewAccount.setDescription("View account details");
        template.save(viewAccount);

        // Item Management
        Authority createItem = createAuthority(AuthoritiesConstants.CREATE_ITEM);
        createItem.setCategory("Item Management");
        createItem.setDescription("Create new items");
        template.save(createItem);

        Authority updateItem = createAuthority(AuthoritiesConstants.UPDATE_ITEM);
        updateItem.setCategory("Item Management");
        updateItem.setDescription("Modify item information");
        template.save(updateItem);

        Authority deleteItem = createAuthority(AuthoritiesConstants.DELETE_ITEM);
        deleteItem.setCategory("Item Management");
        deleteItem.setDescription("Delete items from the system");
        template.save(deleteItem);

        Authority viewItem = createAuthority(AuthoritiesConstants.VIEW_ITEM);
        viewItem.setCategory("Item Management");
        viewItem.setDescription("View item details");
        template.save(viewItem);

        // Bank Management
        Authority createBank = createAuthority(AuthoritiesConstants.CREATE_BANK);
        createBank.setCategory("Bank Management");
        createBank.setDescription("Create new bank entries");
        template.save(createBank);

        Authority updateBank = createAuthority(AuthoritiesConstants.UPDATE_BANK);
        updateBank.setCategory("Bank Management");
        updateBank.setDescription("Modify bank information");
        template.save(updateBank);

        Authority deleteBank = createAuthority(AuthoritiesConstants.DELETE_BANK);
        deleteBank.setCategory("Bank Management");
        deleteBank.setDescription("Delete bank entries");
        template.save(deleteBank);

        Authority viewBank = createAuthority(AuthoritiesConstants.VIEW_BANK);
        viewBank.setCategory("Bank Management");
        viewBank.setDescription("View bank details");
        template.save(viewBank);

        // Payment Management
        Authority createPayment = createAuthority(AuthoritiesConstants.CREATE_PAYMENT);
        createPayment.setCategory("Payment Management");
        createPayment.setDescription("Create new payments");
        template.save(createPayment);

        Authority updatePayment = createAuthority(AuthoritiesConstants.UPDATE_PAYMENT);
        updatePayment.setCategory("Payment Management");
        updatePayment.setDescription("Modify payment information");
        template.save(updatePayment);

        Authority deletePayment = createAuthority(AuthoritiesConstants.DELETE_PAYMENT);
        deletePayment.setCategory("Payment Management");
        deletePayment.setDescription("Delete payments from the system");
        template.save(deletePayment);

        Authority viewPayment = createAuthority(AuthoritiesConstants.VIEW_PAYMENT);
        viewPayment.setCategory("Payment Management");
        viewPayment.setDescription("View payment details");
        template.save(viewPayment);

        // Sales Management
        Authority createSale = createAuthority(AuthoritiesConstants.CREATE_SALE);
        createSale.setCategory("Sales Management");
        createSale.setDescription("Create new sales records");
        template.save(createSale);

        Authority updateSale = createAuthority(AuthoritiesConstants.UPDATE_SALE);
        updateSale.setCategory("Sales Management");
        updateSale.setDescription("Modify sales information");
        template.save(updateSale);

        Authority deleteSale = createAuthority(AuthoritiesConstants.DELETE_SALE);
        deleteSale.setCategory("Sales Management");
        deleteSale.setDescription("Delete sales records");
        template.save(deleteSale);

        Authority viewSale = createAuthority(AuthoritiesConstants.VIEW_SALE);
        viewSale.setCategory("Sales Management");
        viewSale.setDescription("View sales details");
        template.save(viewSale);

        Authority cancelSale = createAuthority(AuthoritiesConstants.CANCEL_SALE);
        cancelSale.setCategory("Sales Management");
        cancelSale.setDescription("Cancel sales transactions");
        template.save(cancelSale);

        Authority printSale = createAuthority(AuthoritiesConstants.PRINT_SALE);
        printSale.setCategory("Sales Management");
        printSale.setDescription("Print sales documents");
        template.save(printSale);

        // Purchase Management
        Authority createPurchase = createAuthority(AuthoritiesConstants.CREATE_PURCHASE);
        createPurchase.setCategory("Purchase Management");
        createPurchase.setDescription("Create new purchase records");
        template.save(createPurchase);

        Authority updatePurchase = createAuthority(AuthoritiesConstants.UPDATE_PURCHASE);
        updatePurchase.setCategory("Purchase Management");
        updatePurchase.setDescription("Modify purchase information");
        template.save(updatePurchase);

        Authority deletePurchase = createAuthority(AuthoritiesConstants.DELETE_PURCHASE);
        deletePurchase.setCategory("Purchase Management");
        deletePurchase.setDescription("Delete purchase records");
        template.save(deletePurchase);

        Authority viewPurchase = createAuthority(AuthoritiesConstants.VIEW_PURCHASE);
        viewPurchase.setCategory("Purchase Management");
        viewPurchase.setDescription("View purchase details");
        template.save(viewPurchase);

        Authority cancelPurchase = createAuthority(AuthoritiesConstants.CANCEL_PURCHASE);
        cancelPurchase.setCategory("Purchase Management");
        cancelPurchase.setDescription("Cancel purchase transactions");
        template.save(cancelPurchase);

        Authority printPurchase = createAuthority(AuthoritiesConstants.PRINT_PURCHASE);
        printPurchase.setCategory("Purchase Management");
        printPurchase.setDescription("Print purchase documents");
        template.save(printPurchase);

        // Report Management
        Authority viewReports = createAuthority(AuthoritiesConstants.VIEW_REPORTS);
        viewReports.setCategory("Report Management");
        viewReports.setDescription("View system reports");
        template.save(viewReports);

        Authority generateReport = createAuthority(AuthoritiesConstants.GENERATE_REPORT);
        generateReport.setCategory("Report Management");
        generateReport.setDescription("Generate new reports");
        template.save(generateReport);

        Authority exportReport = createAuthority(AuthoritiesConstants.EXPORT_REPORT);
        exportReport.setCategory("Report Management");
        exportReport.setDescription("Export reports to different formats");
        template.save(exportReport);

        // User Management
        Authority createUser = createAuthority(AuthoritiesConstants.CREATE_USER);
        createUser.setCategory("User Management");
        createUser.setDescription("Create new system users");
        template.save(createUser);

        Authority updateUser = createAuthority(AuthoritiesConstants.UPDATE_USER);
        updateUser.setCategory("User Management");
        updateUser.setDescription("Modify user information");
        template.save(updateUser);

        Authority deleteUser = createAuthority(AuthoritiesConstants.DELETE_USER);
        deleteUser.setCategory("User Management");
        deleteUser.setDescription("Delete users from the system");
        template.save(deleteUser);

        Authority viewUser = createAuthority(AuthoritiesConstants.VIEW_USER);
        viewUser.setCategory("User Management");
        viewUser.setDescription("View user details");
        template.save(viewUser);

        Authority manageRoles = createAuthority(AuthoritiesConstants.MANAGE_ROLES);
        manageRoles.setCategory("User Management");
        manageRoles.setDescription("Manage user roles and permissions");
        template.save(manageRoles);

        // System Settings
        Authority viewSettings = createAuthority(AuthoritiesConstants.VIEW_SETTINGS);
        viewSettings.setCategory("System Settings");
        viewSettings.setDescription("View system settings");
        template.save(viewSettings);

        Authority updateSettings = createAuthority(AuthoritiesConstants.UPDATE_SETTINGS);
        updateSettings.setCategory("System Settings");
        updateSettings.setDescription("Modify system settings");
        template.save(updateSettings);

        Authority manageSystemConfig = createAuthority(AuthoritiesConstants.MANAGE_SYSTEM_CONFIG);
        manageSystemConfig.setCategory("System Settings");
        manageSystemConfig.setDescription("Manage system configuration");
        template.save(manageSystemConfig);
    }
}
