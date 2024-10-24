package com.ddmtchr.vitasofttesttask.mapper;

import com.ddmtchr.vitasofttesttask.domain.Ticket;
import com.ddmtchr.vitasofttesttask.domain.TicketStatus;
import com.ddmtchr.vitasofttesttask.dto.request.TicketRequestDTO;
import com.ddmtchr.vitasofttesttask.dto.response.TicketResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", expression = "java(setDraft())")
    Ticket toEntity(TicketRequestDTO dto);

    TicketResponseDTO toResponseDTO(Ticket e);

    default TicketStatus setDraft() {
        return TicketStatus.DRAFT;
    }
}
