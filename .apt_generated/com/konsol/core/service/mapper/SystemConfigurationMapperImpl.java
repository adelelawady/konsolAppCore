package com.konsol.core.service.mapper;

import com.konsol.core.service.api.dto.SysOptions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:03+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class SystemConfigurationMapperImpl implements SystemConfigurationMapper {

    @Override
    public SysOptions toEntity(SystemConfiguration dto) {
        if (dto == null) {
            return null;
        }

        SysOptions sysOptions = new SysOptions();

        return sysOptions;
    }

    @Override
    public SystemConfiguration toDto(SysOptions entity) {
        if (entity == null) {
            return null;
        }

        SystemConfiguration systemConfiguration = new SystemConfiguration();

        return systemConfiguration;
    }

    @Override
    public List<SysOptions> toEntity(List<SystemConfiguration> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<SysOptions> list = new ArrayList<SysOptions>(dtoList.size());
        for (SystemConfiguration systemConfiguration : dtoList) {
            list.add(toEntity(systemConfiguration));
        }

        return list;
    }

    @Override
    public List<SystemConfiguration> toDto(List<SysOptions> entityList) {
        if (entityList == null) {
            return null;
        }

        List<SystemConfiguration> list = new ArrayList<SystemConfiguration>(entityList.size());
        for (SysOptions sysOptions : entityList) {
            list.add(toDto(sysOptions));
        }

        return list;
    }

    @Override
    public void partialUpdate(SysOptions entity, SystemConfiguration dto) {
        if (dto == null) {
            return;
        }
    }
}
