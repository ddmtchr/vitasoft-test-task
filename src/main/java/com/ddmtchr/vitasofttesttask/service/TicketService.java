package com.ddmtchr.vitasofttesttask.service;

import com.ddmtchr.vitasofttesttask.domain.Ticket;
import com.ddmtchr.vitasofttesttask.domain.TicketStatus;
import com.ddmtchr.vitasofttesttask.dto.request.TicketRequestDTO;
import com.ddmtchr.vitasofttesttask.dto.response.TicketResponseDTO;
import com.ddmtchr.vitasofttesttask.exception.NotFoundException;
import com.ddmtchr.vitasofttesttask.exception.UnauthorizedActionException;
import com.ddmtchr.vitasofttesttask.mapper.TicketMapper;
import com.ddmtchr.vitasofttesttask.repository.TicketRepository;
import com.ddmtchr.vitasofttesttask.security.entity.User;
import com.ddmtchr.vitasofttesttask.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;

    public TicketResponseDTO createTicket(TicketRequestDTO requestDTO, String username) {
        User user = (User) userService.loadUserByUsername(username);

        Ticket entity = TicketMapper.INSTANCE.toEntity(requestDTO);
        entity.setUser(user);
        Ticket savedEntity = ticketRepository.save(entity);
        return TicketMapper.INSTANCE.toResponseDTO(savedEntity);
    }

    public TicketResponseDTO updateTicketDraft(Long id, TicketRequestDTO requestDTO, String username) {
        User user = (User) userService.loadUserByUsername(username);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (!ticket.getUser().getUsername().equals(username) || ticket.getStatus() != TicketStatus.DRAFT) {
            throw new UnauthorizedActionException("Unauthorized action or ticket is not a draft");
        }

        ticket.setText(requestDTO.getText());

        Ticket updatedEntity = ticketRepository.save(ticket);
        return TicketMapper.INSTANCE.toResponseDTO(updatedEntity);
    }

    public void sendTicket(Long ticketId, String username) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (!ticket.getUser().getUsername().equals(username) || ticket.getStatus() != TicketStatus.DRAFT) {
            throw new UnauthorizedActionException("Unauthorized action or ticket is not a draft");
        }

        ticket.setStatus(TicketStatus.SENT);
        ticketRepository.save(ticket);
    }

    public Page<TicketResponseDTO> getAllUserTickets(int page, int size, String sort, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        if (sort.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by("creationDate").ascending());
        }

        Page<Ticket> tickets = ticketRepository.findByUserUsername(username, pageable);
        return tickets.map(TicketMapper.INSTANCE::toResponseDTO);
    }

    public Page<TicketResponseDTO> getAllTicketsByUsername(int page, int size, String sort, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        if (sort.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by("creationDate").ascending());
        }

        Page<Ticket> tickets = ticketRepository.findByStatusAndUserUsernameStartingWith(TicketStatus.SENT, username, pageable);
        Page<TicketResponseDTO> p = tickets.map(TicketMapper.INSTANCE::toResponseDTO);
        p.forEach(dto -> dto.setText(insertDashes(dto.getText())));
        return p;
    }

    public Page<TicketResponseDTO> getAllTickets(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        if (sort.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by("creationDate").ascending());
        }

        Page<Ticket> tickets = ticketRepository.findByStatus(TicketStatus.SENT, pageable);
        Page<TicketResponseDTO> p = tickets.map(TicketMapper.INSTANCE::toResponseDTO);
        p.forEach(dto -> dto.setText(insertDashes(dto.getText())));
        return p;
    }

    public TicketResponseDTO acceptTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdAndStatus(ticketId, TicketStatus.SENT)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        ticket.setStatus(TicketStatus.ACCEPTED);
        Ticket acceptedTicket = ticketRepository.save(ticket);

        return TicketMapper.INSTANCE.toResponseDTO(acceptedTicket);
    }

    public TicketResponseDTO rejectTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdAndStatus(ticketId, TicketStatus.SENT)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        ticket.setStatus(TicketStatus.REJECTED);
        Ticket rejectedTicket = ticketRepository.save(ticket);

        return TicketMapper.INSTANCE.toResponseDTO(rejectedTicket);
    }

    private String insertDashes(String text) {
        return text.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining("-"));
    }
}
