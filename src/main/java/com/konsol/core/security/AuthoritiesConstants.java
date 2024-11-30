package com.konsol.core.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    // Basic Roles
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    // Invoice Authorities
    public static final String CREATE_INVOICE = "ROLE_CREATE_INVOICE";
    public static final String UPDATE_INVOICE = "ROLE_UPDATE_INVOICE";
    public static final String DELETE_INVOICE = "ROLE_DELETE_INVOICE";
    public static final String VIEW_INVOICE = "ROLE_VIEW_INVOICE";
    public static final String SAVE_INVOICE = "ROLE_SAVE_INVOICE";
    public static final String PRINT_INVOICE = "ROLE_PRINT_INVOICE";
    public static final String CANCEL_INVOICE = "ROLE_CANCEL_INVOICE";

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

    // Report Authorities
    public static final String VIEW_REPORTS = "ROLE_VIEW_REPORTS";
    public static final String GENERATE_REPORT = "ROLE_GENERATE_REPORT";
    public static final String EXPORT_REPORT = "ROLE_EXPORT_REPORT";

    // User Management Authorities
    public static final String CREATE_USER = "ROLE_CREATE_USER";
    public static final String UPDATE_USER = "ROLE_UPDATE_USER";
    public static final String DELETE_USER = "ROLE_DELETE_USER";
    public static final String VIEW_USER = "ROLE_VIEW_USER";
    public static final String MANAGE_ROLES = "ROLE_MANAGE_ROLES";

    // Settings Authorities
    public static final String VIEW_SETTINGS = "ROLE_VIEW_SETTINGS";
    public static final String UPDATE_SETTINGS = "ROLE_UPDATE_SETTINGS";
    public static final String MANAGE_SYSTEM_CONFIG = "ROLE_MANAGE_SYSTEM_CONFIG";

    private AuthoritiesConstants() {}
}
