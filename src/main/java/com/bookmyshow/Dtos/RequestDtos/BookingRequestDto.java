package com.bookmyshow.Dtos.RequestDtos;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDto {
    private Integer userId;
    private Integer showId;
    private List<String> seatNumbers; // Ex: ["A1", "A2", "B3"]
}
