package com.bookmyshow.Transformers;

import com.bookmyshow.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.Ticket;

public class TicketTransformer {

    public static TicketResponseDto returnTicket(Show show, Ticket ticket) {
        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .bookedSeats(ticket.getShowSeat() != null ? ticket.getShowSeat().getSeatNo() : null)
                .address(show.getTheatre().getAddress())
                .theaterName(show.getTheatre().getName())
                .movieName(show.getMovie().getMovieName())
                .date(show.getDate())
                .time(show.getTime())
                .totalPrice(ticket.getTicketPrice())
                .build();

        return ticketResponseDto;
    }
}