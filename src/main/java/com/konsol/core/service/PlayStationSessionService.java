package com.konsol.core.service;

import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.service.api.dto.PsSessionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PlayStationSession}.
 */
public interface PlayStationSessionService {
    /**
     * Save a playStationSession.
     *
     * @param PsSessionDTO the entity to save.
     * @return the persisted entity.
     */
    PsSessionDTO save(PsSessionDTO PsSessionDTO);

    /**
     * Updates a playStationSession.
     *
     * @param PsSessionDTO the entity to update.
     * @return the persisted entity.
     */
    PsSessionDTO update(PsSessionDTO PsSessionDTO);

    /**
     * Partially updates a playStationSession.
     *
     * @param PsSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PsSessionDTO> partialUpdate(PsSessionDTO PsSessionDTO);

    /**
     * Get all the playStationSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PsSessionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" playStationSession.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PsSessionDTO> findOne(Long id);

    /**
     * Delete the "id" playStationSession.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
