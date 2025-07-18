package com.bookmyshow.Transformers;

import com.bookmyshow.Dtos.RequestDtos.TheatreDto;
import com.bookmyshow.Models.Theater;

public class TheaterTransformer {

    public static Theater theaterDtoToTheater(TheatreDto entryDto) {
        Theater theater = Theater.builder()
                .name(entryDto.getName())
                .address(entryDto.getAddress()) // <-- Corrected
                .city(entryDto.getCity())     // <-- Added
                .numberOfScreens(entryDto.getNumberOfScreens()) // <-- Added
                .build();
        return theater;
    }
}