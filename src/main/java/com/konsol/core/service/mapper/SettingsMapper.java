package com.konsol.core.service.mapper;

import com.konsol.core.domain.Settings;
import com.konsol.core.domain.Store;
import com.konsol.core.service.api.dto.ServerSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Store} and its DTO {@link ServerSettings}.
 */
@Mapper(componentModel = "spring", uses = { UtilitsMapper.class }, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SettingsMapper extends EntityMapper<ServerSettings, Settings> {
    @Override
    @Mapping(target = "id", source = "id")
    @Mapping(target = "MAIN_SELECTED_STORE_ID", source = "MAINSELECTEDSTOREID")
    @Mapping(target = "MAIN_SELECTED_BANK_ID", source = "MAINSELECTEDBANKID")
    @Mapping(target = "SALES_CHECK_ITEM_QTY", source = "SALESCHECKITEMQTY")
    @Mapping(target = "SALES_UPDATE_ITEM_QTY_AFTER_SAVE", source = "SALESUPDATEITEMQTYAFTERSAVE")
    @Mapping(target = "PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE", source = "PURCHASEUPDATEITEMQTYAFTERSAVE")
    @Mapping(target = "PLAYSTATION_SELECTED_STORE_ID", source = "PLAYSTATIONSELECTEDSTOREID")
    @Mapping(target = "PLAYSTATION_SELECTED_BANK_ID", source = "PLAYSTATIONSELECTEDBANKID")
    @Mapping(target = "ALLOW_NEGATIVE_INVENTORY", source = "ALLOWNEGATIVEINVENTORY")
    @Mapping(target = "SAVE_INVOICE_DELETETED_INVOICEITEMS", source = "SAVEINVOICEDELETETEDINVOICEITEMS")
    @Mapping(target = "BACKUP_ENABLED", source = "BACKUPENABLED")
    @Mapping(target = "BACKUP_SCHEDULE_TYPE", source = "BACKUPSCHEDULETYPE")
    @Mapping(target = "BACKUP_TIME", source = "BACKUPTIME")
    @Mapping(target = "BACKUP_DAYS", source = "BACKUPDAYS")
    @Mapping(target = "BACKUP_RETENTION_DAYS", source = "BACKUPRETENTIONDAYS")
    @Mapping(target = "BACKUP_LOCATION", source = "BACKUPLOCATION")
    @Mapping(target = "BACKUP_INCLUDE_FILES", source = "BACKUPINCLUDEFILES")
    @Mapping(target = "BACKUP_COMPRESS", source = "BACKUPCOMPRESS")
    @Mapping(target = "MONGODB_DUMP_PATH", source = "MONGODBDUMPPATH")
    @Mapping(target = "MONGODB_RESTORE_PATH", source = "MONGODBRESTOREPATH")
    @Mapping(target = "PLAYSTATION_DEFAULT_PRINTER", source = "PLAYSTATIONDEFAULTPRINTER")
    @Mapping(target = "PLAYSTATION_PRINT_ON_CHECKOUT", source = "PLAYSTATIONPRINTONCHECKOUT")
    Settings toEntity(ServerSettings dto);
}
