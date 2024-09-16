package com.konsol.core.service.mapper;

import com.konsol.core.domain.Pk;
import com.konsol.core.service.dto.PkDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:02+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class PkMapperImpl implements PkMapper {

    @Override
    public Pk toEntity(PkDTO dto) {
        if (dto == null) {
            return null;
        }

        Pk pk = new Pk();

        pk.setId(dto.getId());
        pk.setKind(dto.getKind());
        pk.setValue(dto.getValue());

        return pk;
    }

    @Override
    public PkDTO toDto(Pk entity) {
        if (entity == null) {
            return null;
        }

        PkDTO pkDTO = new PkDTO();

        pkDTO.setId(entity.getId());
        pkDTO.setKind(entity.getKind());
        pkDTO.setValue(entity.getValue());

        return pkDTO;
    }

    @Override
    public List<Pk> toEntity(List<PkDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Pk> list = new ArrayList<Pk>(dtoList.size());
        for (PkDTO pkDTO : dtoList) {
            list.add(toEntity(pkDTO));
        }

        return list;
    }

    @Override
    public List<PkDTO> toDto(List<Pk> entityList) {
        if (entityList == null) {
            return null;
        }

        List<PkDTO> list = new ArrayList<PkDTO>(entityList.size());
        for (Pk pk : entityList) {
            list.add(toDto(pk));
        }

        return list;
    }

    @Override
    public void partialUpdate(Pk entity, PkDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getKind() != null) {
            entity.setKind(dto.getKind());
        }
        if (dto.getValue() != null) {
            entity.setValue(dto.getValue());
        }
    }
}
