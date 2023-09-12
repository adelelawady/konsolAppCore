package com.konsol.core.service.mapper;

import com.konsol.core.domain.User;
import com.konsol.core.service.api.dto.AdminUserDTO;
import com.konsol.core.service.api.dto.ManagedUserVM;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:03+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class UtilitsMapperImpl implements UtilitsMapper {

    @Override
    public AdminUserDTO fromManagedUserVM(ManagedUserVM adminUserDTO) {
        if ( adminUserDTO == null ) {
            return null;
        }

        AdminUserDTO adminUserDTO1 = new AdminUserDTO();

        adminUserDTO1.setId( adminUserDTO.getId() );
        adminUserDTO1.setLogin( adminUserDTO.getLogin() );
        adminUserDTO1.setFirstName( adminUserDTO.getFirstName() );
        adminUserDTO1.setLastName( adminUserDTO.getLastName() );
        adminUserDTO1.setEmail( adminUserDTO.getEmail() );
        adminUserDTO1.setImageUrl( adminUserDTO.getImageUrl() );
        adminUserDTO1.setActivated( adminUserDTO.getActivated() );
        adminUserDTO1.setLangKey( adminUserDTO.getLangKey() );
        adminUserDTO1.setCreatedBy( adminUserDTO.getCreatedBy() );
        adminUserDTO1.setCreatedDate( adminUserDTO.getCreatedDate() );
        adminUserDTO1.setLastModifiedBy( adminUserDTO.getLastModifiedBy() );
        adminUserDTO1.setLastModifiedDate( adminUserDTO.getLastModifiedDate() );
        Set<String> set = adminUserDTO.getAuthorities();
        if ( set != null ) {
            adminUserDTO1.setAuthorities( new LinkedHashSet<String>( set ) );
        }

        return adminUserDTO1;
    }

    @Override
    public com.konsol.core.service.api.dto.User FromUserToAPIUser(User user) {
        if ( user == null ) {
            return null;
        }

        com.konsol.core.service.api.dto.User user1 = new com.konsol.core.service.api.dto.User();

        user1.setId( user.getId() );
        user1.setLogin( user.getLogin() );
        user1.setFirstName( user.getFirstName() );
        user1.setLastName( user.getLastName() );
        user1.setEmail( user.getEmail() );
        user1.setActivated( user.isActivated() );
        user1.setLangKey( user.getLangKey() );
        user1.setImageUrl( user.getImageUrl() );
        user1.setResetDate( map( user.getResetDate() ) );

        return user1;
    }
}
