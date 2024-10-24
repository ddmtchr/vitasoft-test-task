package com.ddmtchr.vitasofttesttask.controller;

import com.ddmtchr.vitasofttesttask.dto.request.TicketRequestDTO;
import com.ddmtchr.vitasofttesttask.dto.response.TicketResponseDTO;
import com.ddmtchr.vitasofttesttask.security.jwt.JwtUtils;
import com.ddmtchr.vitasofttesttask.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        String username = jwtUtils.getCurrentUser().getUsername();

        TicketResponseDTO createdTicket = ticketService.createTicket(ticketRequestDTO, username);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicketDraft(@PathVariable Long id, @RequestBody TicketRequestDTO ticketRequestDTO) {
        String username = jwtUtils.getCurrentUser().getUsername();

        TicketResponseDTO updatedTicket = ticketService.updateTicketDraft(id, ticketRequestDTO, username);
        return ResponseEntity.ok(updatedTicket);
    }

    @PutMapping("/{id}/send")
    public ResponseEntity<?> sendTicket(@PathVariable Long id) {
        String username = jwtUtils.getCurrentUser().getUsername();

        ticketService.sendTicket(id, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<Page<TicketResponseDTO>> getAllUserTickets(@RequestParam(defaultValue = "desc") String sort,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int size) {
        if (!(sort.equals("desc") || sort.equals("asc"))) return ResponseEntity.badRequest().build();

        String username = jwtUtils.getCurrentUser().getUsername();

        return new ResponseEntity<>(ticketService.getAllUserTickets(page, size, sort, username), HttpStatus.OK);
    }

    @GetMapping("/by-name")
    public ResponseEntity<Page<TicketResponseDTO>> getAllTicketsByUsername(@RequestParam(defaultValue = "desc") String sort,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "5") int size,
                                                                           @RequestParam String username) {
        if (!(sort.equals("desc") || sort.equals("asc"))) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(ticketService.getAllTicketsByUsername(page, size, sort, username), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<TicketResponseDTO>> getAllTickets(@RequestParam(defaultValue = "desc") String sort,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int size) {
        if (!(sort.equals("desc") || sort.equals("asc"))) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(ticketService.getAllTickets(page, size, sort), HttpStatus.OK);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<TicketResponseDTO> acceptTicket(@PathVariable Long id) {
        return new ResponseEntity<>(ticketService.acceptTicket(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<TicketResponseDTO> rejectTicket(@PathVariable Long id) {
        return new ResponseEntity<>(ticketService.rejectTicket(id), HttpStatus.OK);
    }
}
