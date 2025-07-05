package com.bookmyshow.Services;

import com.bookmyshow.Dtos.RequestDtos.TheaterSeatEntryDto;
import com.bookmyshow.Models.TheaterSeat;
import java.util.List;

public interface TheaterSeatService {
    String addTheaterSeat(TheaterSeatEntryDto theaterSeatEntryDto);
    List<TheaterSeat> getSeatsByTheater(Integer theaterId);
    String updateTheaterSeat(Integer id, TheaterSeatEntryDto theaterSeatEntryDto);
    String deleteTheaterSeat(Integer id);
}
