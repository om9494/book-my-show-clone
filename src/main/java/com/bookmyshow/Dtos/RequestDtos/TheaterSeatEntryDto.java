package com.bookmyshow.Dtos.RequestDtos;

import com.bookmyshow.Enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheaterSeatEntryDto {
    private String seatNo;
    private SeatType seatType;
    private Integer theaterId;
}
