package com.bookmyshow.Transformers;

import com.bookmyshow.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.Ticket;

public class TicketTransformer {

    public static TicketResponseDto returnTicket(Show show, Ticket ticket) {
        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .ticketId(ticket.getTicketId())
                .bookedAt(ticket.getBookedAt())
                .showId(show.getShowId())
                .movie(show.getMovie())
                .theater(show.getTheatre())
                .seatId(ticket.getShowSeat().getId())
                .seatNo(ticket.getShowSeat().getSeatNo())
                .address(show.getTheatre().getAddress())
                .date(show.getDate())
                .time(show.getTime())
                .fare(ticket.getTicketPrice())
                .build();

        return ticketResponseDto;
    }
}