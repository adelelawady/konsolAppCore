package com.konsol.core.service;

import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.service.dto.PkDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Pk}.
 */
public interface PkService {
    /**
     * Save a pk.
     *
     * @param pkDTO the entity to save.
     * @return the persisted entity.
     */
    PkDTO save(PkDTO pkDTO);

    /**
     * Updates a pk.
     *
     * @param pkDTO the entity to update.
     * @return the persisted entity.
     */
    PkDTO update(PkDTO pkDTO);

    /**
     * Partially updates a pk.
     *
     * @param pkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PkDTO> partialUpdate(PkDTO pkDTO);

    /**
     * Get all the pks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PkDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pk.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PkDTO> findOne(String id);

    /**
     * Delete the "id" pk.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * create new entity pk number or updates current pk for selected entity
     * @param entityKind type for pk required to be created ex items , invoice
     * @return new or found pk for current entity
     */
    Pk generatePkEntity(PkKind entityKind);

    /**
     * gets current pk for selected entity
     * @param entityKind type for pk required to search for ex items , invoice
     * @return found pk for current entity
     */
    Pk getPkEntity(PkKind entityKind);
}
