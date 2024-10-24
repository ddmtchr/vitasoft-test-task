package com.ddmtchr.vitasofttesttask.repository;

import com.ddmtchr.vitasofttesttask.domain.Ticket;
import com.ddmtchr.vitasofttesttask.domain.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {
    Page<Ticket> findByUserUsername(String username, Pageable pageable);

    Optional<Ticket> findByIdAndStatus(Long id, TicketStatus status);

    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByStatusAndUserUsernameStartingWith(TicketStatus status, String username, Pageable pageable);
}
