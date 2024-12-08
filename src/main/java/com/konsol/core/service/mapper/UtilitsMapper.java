package com.konsol.core.service.mapper;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.User;
import com.konsol.core.service.api.dto.AdminUserDTO;
import com.konsol.core.service.api.dto.ManagedUserVM;
import com.konsol.core.service.dto.AccountUserDTO;
import java.time.*;
import javax.validation.Valid;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link AccountUser} and its DTO {@link AccountUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilitsMapper {
    AdminUserDTO fromManagedUserVM(@Valid ManagedUserVM adminUserDTO);

    com.konsol.core.service.api.dto.User FromUserToAPIUser(User user);

    default OffsetDateTime map(Instant value) {
        return OffsetDateTime.ofInstant(value, ZoneId.systemDefault());
    }

    default Instant map(OffsetDateTime value) {
        return value.toInstant();
    }

    default String intToString(Integer value) {
        return String.valueOf(value);
    }

    default LocalDateTime mapLocalDateTime(OffsetDateTime value) {
        return value.toLocalDateTime();
    }

    default OffsetDateTime mapOffsetDateTime(LocalDateTime value) {
        return value.atOffset(ZoneOffset.UTC);
    }
}
