package com.bookmyshow.Dtos.ResponseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

import com.bookmyshow.Models.Movie;
import com.bookmyshow.Models.Theater;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDto {
    private Integer ticketId;
    private Date bookedAt;
    private Time time;
    private Date date;
    private Movie movie;
    private Theater theater;
    private String address;
    private String seatNo;
    private Integer showId;
    private Integer seatId;
    private Integer fare;
}