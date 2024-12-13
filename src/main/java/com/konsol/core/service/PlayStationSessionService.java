package com.konsol.core.service;

import com.konsol.core.service.dto.PlayStationSessionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.PlayStationSession}.
 */
public interface PlayStationSessionService {
    /**
     * Save a playStationSession.
     *
     * @param playStationSessionDTO the entity to save.
     * @return the persisted entity.
     */
    PlayStationSessionDTO save(PlayStationSessionDTO playStationSessionDTO);

    /**
     * Updates a playStationSession.
     *
     * @param playStationSessionDTO the entity to update.
     * @return the persisted entity.
     */
    PlayStationSessionDTO update(PlayStationSessionDTO playStationSessionDTO);

    /**
     * Partially updates a playStationSession.
     *
     * @param playStationSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlayStationSessionDTO> partialUpdate(PlayStationSessionDTO playStationSessionDTO);

    /**
     * Get all the playStationSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlayStationSessionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" playStationSession.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayStationSessionDTO> findOne(Long id);

    /**
     * Delete the "id" playStationSession.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
