package com.konsol.core.service.impl;

import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.PkRepository;
import com.konsol.core.service.PkService;
import com.konsol.core.service.dto.PkDTO;
import com.konsol.core.service.mapper.PkMapper;
import java.math.BigDecimal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Pk}.
 */
@Service
public class PkServiceImpl implements PkService {

    private final Logger log = LoggerFactory.getLogger(PkServiceImpl.class);

    private final PkRepository pkRepository;

    private final PkMapper pkMapper;

    public PkServiceImpl(PkRepository pkRepository, PkMapper pkMapper) {
        this.pkRepository = pkRepository;
        this.pkMapper = pkMapper;
    }

    @Override
    public PkDTO save(PkDTO pkDTO) {
        log.debug("Request to save Pk : {}", pkDTO);
        Pk pk = pkMapper.toEntity(pkDTO);
        pk = pkRepository.save(pk);
        return pkMapper.toDto(pk);
    }

    @Override
    public PkDTO update(PkDTO pkDTO) {
        log.debug("Request to update Pk : {}", pkDTO);
        Pk pk = pkMapper.toEntity(pkDTO);
        pk = pkRepository.save(pk);
        return pkMapper.toDto(pk);
    }

    @Override
    public Optional<PkDTO> partialUpdate(PkDTO pkDTO) {
        log.debug("Request to partially update Pk : {}", pkDTO);

        return pkRepository
            .findById(pkDTO.getId())
            .map(existingPk -> {
                pkMapper.partialUpdate(existingPk, pkDTO);

                return existingPk;
            })
            .map(pkRepository::save)
            .map(pkMapper::toDto);
    }

    @Override
    public Page<PkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pks");
        return pkRepository.findAll(pageable).map(pkMapper::toDto);
    }

    @Override
    public Optional<PkDTO> findOne(String id) {
        log.debug("Request to get Pk : {}", id);
        return pkRepository.findById(id).map(pkMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Pk : {}", id);
        pkRepository.deleteById(id);
    }

    @Override
    public Pk generatePkEntity(PkKind entityKind) {
        Pk entityPk = getPkEntity(entityKind);
        entityPk.setValue(entityPk.getValue().add(BigDecimal.valueOf(1)));
        return this.pkRepository.save(entityPk);
    }

    @Override
    public Pk getPkEntity(PkKind entityKind) {
        Optional<Pk> foundPkOp = this.pkRepository.findOneByKind(entityKind);
        if (foundPkOp.isPresent()) {
            return foundPkOp.get();
        } else {
            return createNewPkForEntity(entityKind);
        }
    }

    private Pk createNewPkForEntity(PkKind entityKind) {
        Pk pk = new Pk();
        pk.setKind(entityKind);
        pk.setValue(BigDecimal.valueOf(0));
        return this.pkRepository.save(pk);
    }
}
