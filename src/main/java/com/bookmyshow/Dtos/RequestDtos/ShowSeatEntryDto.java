package com.bookmyshow.Dtos.RequestDtos;

import lombok.Data;

@Data
public class ShowSeatEntryDto {
    private Integer showId;
    private Integer priceOfPremiumSeat;
    private Integer priceOfClassicPlusSeat;
    private Integer priceOfClassicSeat;
}
