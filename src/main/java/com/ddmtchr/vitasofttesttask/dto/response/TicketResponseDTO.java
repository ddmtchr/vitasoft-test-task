package com.ddmtchr.vitasofttesttask.dto.response;

import com.ddmtchr.vitasofttesttask.domain.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketResponseDTO {
    private Long id;
    private TicketStatus status;
    private LocalDateTime creationDate;
    private String text;
}
