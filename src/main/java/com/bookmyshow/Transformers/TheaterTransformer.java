package com.bookmyshow.Transformers;


import com.bookmyshow.Dtos.RequestDtos.TheaterEntryDto;
import com.bookmyshow.Models.Theater;

public class TheaterTransformer {

    public static Theater theaterDtoToTheater(TheaterEntryDto entryDto) {
        Theater theater = Theater.builder()
                .name(entryDto.getName())
                .address(entryDto.getAddress())
                .build();
        return theater;
    }
}