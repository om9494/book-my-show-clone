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
    private String rowLabel;
    private Integer seatCount;
    private SeatType seatType;
    private Integer theaterId;
}
