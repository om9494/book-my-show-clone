package com.bookmyshow.Dtos.ResponseDtos;

import com.bookmyshow.Models.Movie; // Import Movie model
import com.bookmyshow.Models.Theater; // Import Theater model
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;
import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDto {
    private Integer ticketId;
    private Date bookedAt;
    private Integer showId; // Added as per your transformer
    private Movie movie; // Changed from movieName to Movie object
    private Theater theater; // Changed from theaterName to Theater object
    private Integer seatId; // Added as per your transformer
    private String seatNo; // Added as per your transformer
    private String address; // Added as per your transformer, assuming Theater has an address
    private Date date; // Changed from showDate to date
    private Time time; // Changed from showTime to time
    private Integer fare; // Changed from ticketPrice to fare
}