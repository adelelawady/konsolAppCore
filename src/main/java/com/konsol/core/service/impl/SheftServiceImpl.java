package com.konsol.core.service.impl;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.Sheft;
import com.konsol.core.domain.User;
import com.konsol.core.domain.enumeration.SheftStatus;
import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.repository.SheftRepository;
import com.konsol.core.security.SecurityUtils;
import com.konsol.core.service.SheftService;
import com.konsol.core.service.UserService;
import com.konsol.core.service.api.dto.SheftDTO;
import com.konsol.core.service.mapper.SheftMapper;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.Sheft}.
 */
@Service
public class SheftServiceImpl implements SheftService {

    private static final Logger LOG = LoggerFactory.getLogger(SheftServiceImpl.class);

    private final SheftRepository sheftRepository;

    private final SheftMapper sheftMapper;
    private final UserService userService;

    private final PlayStationSessionRepository playStationSessionRepository;

    public SheftServiceImpl(
        SheftRepository sheftRepository,
        SheftMapper sheftMapper,
        UserService userService,
        PlayStationSessionRepository playStationSessionRepository
    ) {
        this.sheftRepository = sheftRepository;
        this.sheftMapper = sheftMapper;
        this.userService = userService;
        this.playStationSessionRepository = playStationSessionRepository;
    }

    @Override
    public SheftDTO save(SheftDTO sheftDTO) {
        LOG.debug("Request to save Sheft : {}", sheftDTO);
        Sheft sheft = sheftMapper.toEntity(sheftDTO);
        sheft = sheftRepository.save(sheft);
        return sheftMapper.toDto(sheft);
    }

    @Override
    public SheftDTO update(SheftDTO sheftDTO) {
        LOG.debug("Request to update Sheft : {}", sheftDTO);
        Sheft sheft = sheftMapper.toEntity(sheftDTO);
        sheft = sheftRepository.save(sheft);
        return sheftMapper.toDto(sheft);
    }

    @Override
    public Optional<SheftDTO> partialUpdate(SheftDTO sheftDTO) {
        LOG.debug("Request to partially update Sheft : {}", sheftDTO);

        return sheftRepository
            .findById(sheftDTO.getId())
            .map(existingSheft -> {
                sheftMapper.partialUpdate(existingSheft, sheftDTO);

                return existingSheft;
            })
            .map(sheftRepository::save)
            .map(sheftMapper::toDto);
    }

    @Override
    public Page<SheftDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Shefts");
        return sheftRepository.findAll(pageable).map(sheftMapper::toDto);
    }

    @Override
    public Optional<SheftDTO> findOne(String id) {
        LOG.debug("Request to get Sheft : {}", id);
        return sheftRepository.findById(id).map(sheftMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Sheft : {}", id);
        sheftRepository.deleteById(id);
    }

    @Override
    public Sheft startSheft() {
        LOG.debug("Request to start new Sheft");

        // Check if there's already an active shift
        Optional<Sheft> activeSheft = sheftRepository.findByActiveTrue();
        if (activeSheft.isPresent()) {
            return activeSheft.get();
            //throw new IllegalStateException("There is already an active shift. Please end the current shift before starting a new one.");
        }

        // Create new Sheft instance
        Sheft sheft = new Sheft();

        // Set start time to current timestamp
        sheft.setStartTime(LocalDate.now());
        sheft.setEndTime(null);
        sheft.setDuration(Duration.ZERO);
        // Set active status
        sheft.setActive(true);

        // Get current authenticated user
        Optional<User> currentUser = Optional.ofNullable(
            userService.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("Current user not found"))
        );
        if (currentUser.isEmpty()) {
            throw new IllegalStateException("Current user not found");
        }
        sheft.setAssignedEmployee(currentUser.get().getFirstName() + " " + currentUser.get().getLastName());
        sheft.setAssignedEmployeeUser(currentUser.get());

        // Initialize required fields with default values
        sheft.setTotalprice(BigDecimal.ZERO);
        sheft.setTotalCost(BigDecimal.ZERO);
        sheft.setNetPrice(BigDecimal.ZERO);
        sheft.setNetCost(BigDecimal.ZERO);
        sheft.setNetUserPrice(BigDecimal.ZERO);
        sheft.setTotalItemsOut(BigDecimal.ZERO);
        sheft.setDiscount(BigDecimal.ZERO);
        sheft.setInvoicesAdditions(BigDecimal.ZERO);
        sheft.setAdditions(BigDecimal.ZERO);
        sheft.setAdditionsNotes(BigDecimal.ZERO);
        sheft.setInvoicesExpenses(BigDecimal.ZERO);
        sheft.setSheftExpenses(BigDecimal.ZERO);
        sheft.setTotalinvoices(BigDecimal.ZERO);
        sheft.setTotaldeletedItems(BigDecimal.ZERO);
        sheft.setTotaldeletedItemsPrice(BigDecimal.ZERO);

        // Save the new sheft
        return sheftRepository.save(sheft);
    }

    @Override
    public SheftDTO getCurrentSheft() {
        return this.sheftMapper.toDto(startSheft());
    }

    @Override
    public void endSheft() {}

    @Override
    public void calculateSheft(String id) {
        LOG.debug("Request to calculate Sheft : {}", id);

        Sheft sheft = sheftRepository.findById(id).orElseThrow(() -> new IllegalStateException("Sheft not found with id: " + id));

        // Get all sessions from sheft start time till now
        List<PlayStationSession> sessions = playStationSessionRepository.findByEndTimeBetween(
            Instant.from(sheft.getStartTime()),
            Instant.now()
        );

        // Set sessions to sheft
        sheft.setSessions(sessions);

        // Initialize calculation variables
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal netPrice = BigDecimal.ZERO;
        BigDecimal netCost = BigDecimal.ZERO;
        BigDecimal netUserPrice = BigDecimal.ZERO;
        BigDecimal totalItemsOut = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal invoicesAdditions = BigDecimal.ZERO;
        BigDecimal additions = BigDecimal.ZERO;
        BigDecimal additionsNotes = BigDecimal.ZERO;
        BigDecimal invoicesExpenses = BigDecimal.ZERO;
        BigDecimal sheftExpenses = BigDecimal.ZERO;
        BigDecimal totalInvoices = BigDecimal.ZERO;
        BigDecimal totalDeletedItems = BigDecimal.ZERO;
        BigDecimal totalDeletedItemsPrice = BigDecimal.ZERO;

        totalInvoices = BigDecimal.valueOf(sessions.size());
        // Calculate totals from sessions
        for (PlayStationSession session : sessions) {
            Invoice invoice = session.getInvoice();
            if (invoice != null) {
                // Add invoice totals
                totalPrice = totalPrice.add(invoice.getTotalPrice());
                totalCost = totalCost.add(invoice.getTotalCost());
                netPrice = netPrice.add(invoice.getNetPrice());
                netCost = netCost.add(invoice.getNetCost());
                netUserPrice = netUserPrice.add(invoice.getUserNetPrice());
                discount = discount.add(invoice.getDiscount());
                invoicesAdditions = invoicesAdditions.add(invoice.getAdditions() != null ? invoice.getAdditions() : BigDecimal.ZERO);
                invoicesExpenses = invoicesExpenses.add(invoice.getExpenses());
            }
        }

        // Set calculated values to sheft
        sheft.setTotalprice(totalPrice);
        sheft.setTotalCost(totalCost);
        sheft.setNetPrice(netPrice);
        sheft.setNetCost(netCost);
        sheft.setNetUserPrice(netUserPrice);
        sheft.setTotalItemsOut(totalItemsOut);
        sheft.setDiscount(discount);
        sheft.setInvoicesAdditions(invoicesAdditions);
        sheft.setAdditions(additions);
        sheft.setAdditionsNotes(additionsNotes);
        sheft.setInvoicesExpenses(invoicesExpenses);
        sheft.setSheftExpenses(sheftExpenses);
        sheft.setTotalinvoices(totalInvoices);
        sheft.setTotaldeletedItems(totalDeletedItems);
        sheft.setTotaldeletedItemsPrice(totalDeletedItemsPrice);

        // Calculate duration if end time exists
        if (sheft.getEndTime() != null) {
            Duration duration = Duration.between(sheft.getStartTime(), Instant.now());
            sheft.setDuration(duration);
        }

        // Save updated sheft
        sheftRepository.save(sheft);
    }
}
