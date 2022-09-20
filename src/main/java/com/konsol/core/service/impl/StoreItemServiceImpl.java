package com.konsol.core.service.impl;

import com.konsol.core.domain.StoreItem;
import com.konsol.core.repository.StoreItemRepository;
import com.konsol.core.service.StoreItemService;
import com.konsol.core.service.api.dto.StoreItemDTO;
import com.konsol.core.service.mapper.StoreItemMapper;
import com.konsol.core.web.api.StoresApi;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link StoreItem}.
 */
@Service
public class StoreItemServiceImpl implements StoreItemService {

    private final Logger log = LoggerFactory.getLogger(StoreItemServiceImpl.class);

    private final StoreItemRepository storeItemRepository;

    private final StoreItemMapper storeItemMapper;

    public StoreItemServiceImpl(StoreItemRepository storeItemRepository, StoreItemMapper storeItemMapper) {
        this.storeItemRepository = storeItemRepository;
        this.storeItemMapper = storeItemMapper;
    }

    @Override
    public StoreItemDTO save(StoreItemDTO storeItemDTO) {
        log.debug("Request to save StoreItem : {}", storeItemDTO);
        StoreItem storeItem = storeItemMapper.toEntity(storeItemDTO);
        storeItem = storeItemRepository.save(storeItem);
        return storeItemMapper.toDto(storeItem);
    }

    @Override
    public StoreItemDTO update(StoreItemDTO storeItemDTO) {
        log.debug("Request to update StoreItem : {}", storeItemDTO);
        StoreItem storeItem = storeItemMapper.toEntity(storeItemDTO);
        storeItem = storeItemRepository.save(storeItem);
        return storeItemMapper.toDto(storeItem);
    }

    @Override
    public Optional<StoreItemDTO> partialUpdate(StoreItemDTO storeItemDTO) {
        log.debug("Request to partially update StoreItem : {}", storeItemDTO);

        return storeItemRepository
            .findById(storeItemDTO.getId())
            .map(existingStoreItem -> {
                storeItemMapper.partialUpdate(existingStoreItem, storeItemDTO);

                return existingStoreItem;
            })
            .map(storeItemRepository::save)
            .map(storeItemMapper::toDto);
    }

    @Override
    public Page<StoreItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StoreItems");
        return storeItemRepository.findAll(pageable).map(storeItemMapper::toDto);
    }

    @Override
    public Optional<StoreItemDTO> findOne(String id) {
        log.debug("Request to get StoreItem : {}", id);
        return storeItemRepository.findById(id).map(storeItemMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete StoreItem : {}", id);
        storeItemRepository.deleteById(id);
    }
}
