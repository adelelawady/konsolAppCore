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
        createAllAuthorities(userAuthority, adminAuthority);
        addUsers(userAuthority, adminAuthority);
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

    private void addUsers(Authority userAuthority, Authority adminAuthority) {
        User user = createUser(userAuthority);
        template.save(user);
        User admin = createAdmin(adminAuthority, userAuthority);
        template.save(admin);
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

    private User createAdmin(Authority adminAuthority, Authority userAuthority) {
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

    private void createAllAuthorities(Authority userAuthority, Authority adminAuthority) {
        template.save(userAuthority);
        template.save(adminAuthority);

        template.save(createAuthority(AuthoritiesConstants.CREATE_INVOICE));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_INVOICE));
        template.save(createAuthority(AuthoritiesConstants.DELETE_INVOICE));
        template.save(createAuthority(AuthoritiesConstants.VIEW_INVOICE));
        template.save(createAuthority(AuthoritiesConstants.SAVE_INVOICE));
        template.save(createAuthority(AuthoritiesConstants.PRINT_INVOICE));
        template.save(createAuthority(AuthoritiesConstants.CANCEL_INVOICE));

        template.save(createAuthority(AuthoritiesConstants.CREATE_STORE));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_STORE));
        template.save(createAuthority(AuthoritiesConstants.DELETE_STORE));
        template.save(createAuthority(AuthoritiesConstants.VIEW_STORE));

        template.save(createAuthority(AuthoritiesConstants.CREATE_ACCOUNT));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_ACCOUNT));
        template.save(createAuthority(AuthoritiesConstants.DELETE_ACCOUNT));
        template.save(createAuthority(AuthoritiesConstants.VIEW_ACCOUNT));

        template.save(createAuthority(AuthoritiesConstants.CREATE_ITEM));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_ITEM));
        template.save(createAuthority(AuthoritiesConstants.DELETE_ITEM));
        template.save(createAuthority(AuthoritiesConstants.VIEW_ITEM));

        template.save(createAuthority(AuthoritiesConstants.CREATE_BANK));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_BANK));
        template.save(createAuthority(AuthoritiesConstants.DELETE_BANK));
        template.save(createAuthority(AuthoritiesConstants.VIEW_BANK));

        template.save(createAuthority(AuthoritiesConstants.CREATE_PAYMENT));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_PAYMENT));
        template.save(createAuthority(AuthoritiesConstants.DELETE_PAYMENT));
        template.save(createAuthority(AuthoritiesConstants.VIEW_PAYMENT));

        template.save(createAuthority(AuthoritiesConstants.CREATE_SALE));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_SALE));
        template.save(createAuthority(AuthoritiesConstants.DELETE_SALE));
        template.save(createAuthority(AuthoritiesConstants.VIEW_SALE));
        template.save(createAuthority(AuthoritiesConstants.CANCEL_SALE));
        template.save(createAuthority(AuthoritiesConstants.PRINT_SALE));

        template.save(createAuthority(AuthoritiesConstants.CREATE_PURCHASE));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_PURCHASE));
        template.save(createAuthority(AuthoritiesConstants.DELETE_PURCHASE));
        template.save(createAuthority(AuthoritiesConstants.VIEW_PURCHASE));
        template.save(createAuthority(AuthoritiesConstants.CANCEL_PURCHASE));
        template.save(createAuthority(AuthoritiesConstants.PRINT_PURCHASE));

        template.save(createAuthority(AuthoritiesConstants.VIEW_REPORTS));
        template.save(createAuthority(AuthoritiesConstants.GENERATE_REPORT));
        template.save(createAuthority(AuthoritiesConstants.EXPORT_REPORT));

        template.save(createAuthority(AuthoritiesConstants.CREATE_USER));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_USER));
        template.save(createAuthority(AuthoritiesConstants.DELETE_USER));
        template.save(createAuthority(AuthoritiesConstants.VIEW_USER));
        template.save(createAuthority(AuthoritiesConstants.MANAGE_ROLES));

        template.save(createAuthority(AuthoritiesConstants.VIEW_SETTINGS));
        template.save(createAuthority(AuthoritiesConstants.UPDATE_SETTINGS));
        template.save(createAuthority(AuthoritiesConstants.MANAGE_SYSTEM_CONFIG));
    }
}
