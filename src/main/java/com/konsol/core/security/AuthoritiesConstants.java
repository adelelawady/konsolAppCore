package com.konsol.core.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    // Basic Roles
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    // Invoice Authorities
    public static final String CREATE_INVOICE = "ROLE_CREATE_INVOICE";
    public static final String UPDATE_INVOICE = "ROLE_UPDATE_INVOICE";
    public static final String DELETE_INVOICE = "ROLE_DELETE_INVOICE";
    public static final String VIEW_INVOICE = "ROLE_VIEW_INVOICE";
    public static final String SAVE_INVOICE = "ROLE_SAVE_INVOICE";

    // Store Authorities
    public static final String CREATE_STORE = "ROLE_CREATE_STORE";
    public static final String UPDATE_STORE = "ROLE_UPDATE_STORE";
    public static final String DELETE_STORE = "ROLE_DELETE_STORE";
    public static final String VIEW_STORE = "ROLE_VIEW_STORE";

    // Account Authorities
    public static final String CREATE_ACCOUNT = "ROLE_CREATE_ACCOUNT";
    public static final String UPDATE_ACCOUNT = "ROLE_UPDATE_ACCOUNT";
    public static final String DELETE_ACCOUNT = "ROLE_DELETE_ACCOUNT";
    public static final String VIEW_ACCOUNT = "ROLE_VIEW_ACCOUNT";

    // Item Authorities
    public static final String CREATE_ITEM = "ROLE_CREATE_ITEM";
    public static final String UPDATE_ITEM = "ROLE_UPDATE_ITEM";
    public static final String DELETE_ITEM = "ROLE_DELETE_ITEM";
    public static final String VIEW_ITEM = "ROLE_VIEW_ITEM";

    // Bank Authorities
    public static final String CREATE_BANK = "ROLE_CREATE_BANK";
    public static final String UPDATE_BANK = "ROLE_UPDATE_BANK";
    public static final String DELETE_BANK = "ROLE_DELETE_BANK";
    public static final String VIEW_BANK = "ROLE_VIEW_BANK";

    // Payment Authorities
    public static final String CREATE_PAYMENT = "ROLE_CREATE_PAYMENT";
    public static final String UPDATE_PAYMENT = "ROLE_UPDATE_PAYMENT";
    public static final String DELETE_PAYMENT = "ROLE_DELETE_PAYMENT";
    public static final String VIEW_PAYMENT = "ROLE_VIEW_PAYMENT";

    // Sales Authorities
    public static final String CREATE_SALE = "ROLE_CREATE_SALE";
    public static final String UPDATE_SALE = "ROLE_UPDATE_SALE";
    public static final String DELETE_SALE = "ROLE_DELETE_SALE";
    public static final String VIEW_SALE = "ROLE_VIEW_SALE";
    public static final String CANCEL_SALE = "ROLE_CANCEL_SALE";
    public static final String PRINT_SALE = "ROLE_PRINT_SALE";

    // Purchase Authorities
    public static final String CREATE_PURCHASE = "ROLE_CREATE_PURCHASE";
    public static final String UPDATE_PURCHASE = "ROLE_UPDATE_PURCHASE";
    public static final String DELETE_PURCHASE = "ROLE_DELETE_PURCHASE";
    public static final String VIEW_PURCHASE = "ROLE_VIEW_PURCHASE";
    public static final String CANCEL_PURCHASE = "ROLE_CANCEL_PURCHASE";
    public static final String PRINT_PURCHASE = "ROLE_PRINT_PURCHASE";


    // User Management Authorities
    public static final String CREATE_USER = "ROLE_CREATE_USER";
    public static final String UPDATE_USER = "ROLE_UPDATE_USER";
    public static final String DELETE_USER = "ROLE_DELETE_USER";
    public static final String VIEW_USER = "ROLE_VIEW_USER";
    public static final String MANAGE_ROLES = "ROLE_MANAGE_ROLES";


    // Playstation Device Authorities
    public static final String CREATE_PLAYSTATION_DEVICE = "ROLE_CREATE_PLAYSTATION_DEVICE";
    public static final String UPDATE_PLAYSTATION_DEVICE = "ROLE_UPDATE_PLAYSTATION_DEVICE";
    public static final String DELETE_PLAYSTATION_DEVICE = "ROLE_DELETE_PLAYSTATION_DEVICE";
    public static final String VIEW_PLAYSTATION_DEVICE = "ROLE_VIEW_PLAYSTATION_DEVICE";

    // Playstation Session Management
    public static final String START_PLAYSTATION_SESSION = "ROLE_START_PLAYSTATION_SESSION";
    public static final String STOP_PLAYSTATION_SESSION = "ROLE_STOP_PLAYSTATION_SESSION";
    public static final String VIEW_PLAYSTATION_SESSION = "ROLE_VIEW_PLAYSTATION_SESSION";
    public static final String UPDATE_PLAYSTATION_SESSION = "ROLE_UPDATE_PLAYSTATION_SESSION";


    // Playstation Device Operations
    public static final String MOVE_PLAYSTATION_DEVICE = "ROLE_MOVE_PLAYSTATION_DEVICE";
    public static final String CHANGE_PLAYSTATION_TYPE = "ROLE_CHANGE_PLAYSTATION_TYPE";
    public static final String MANAGE_PLAYSTATION_ORDERS = "ROLE_MANAGE_PLAYSTATION_ORDERS";

    // Playstation Device Type Management
    public static final String CREATE_PLAYSTATION_TYPE = "ROLE_CREATE_PLAYSTATION_TYPE";
    public static final String UPDATE_PLAYSTATION_TYPE = "ROLE_UPDATE_PLAYSTATION_TYPE";
    public static final String DELETE_PLAYSTATION_TYPE = "ROLE_DELETE_PLAYSTATION_TYPE";
    public static final String VIEW_PLAYSTATION_TYPE = "ROLE_VIEW_PLAYSTATION_TYPE";

    // Sheft Management
    public static final String START_SHEFT = "ROLE_START_SHEFT";
    public static final String END_SHEFT = "ROLE_END_SHEFT";
    public static final String VIEW_SHEFT = "ROLE_VIEW_SHEFT";
    public static final String UPDATE_SHEFT = "ROLE_UPDATE_SHEFT";
    public static final String DELETE_SHEFT = "ROLE_DELETE_SHEFT";
    public static final String VIEW_ACTIVE_SHEFT = "ROLE_VIEW_ACTIVE_SHEFT";

    // Settings Management
    public static final String UPDATE_SYSTEM_SETTINGS = "ROLE_UPDATE_SYSTEM_SETTINGS";

    // Finance Management
    public static final String MANAGE_FINANCE = "ROLE_MANAGE_FINANCE";




    private AuthoritiesConstants() {}
}
