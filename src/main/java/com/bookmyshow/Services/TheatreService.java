package com.bookmyshow.Services;


import java.util.List;


import com.bookmyshow.Dtos.RequestDtos.TheatreDto;
import com.bookmyshow.Exceptions.TheaterDoesNotExists;
import com.bookmyshow.Models.Theater;

public interface TheatreService {
    String addTheatre(TheatreDto theatreDto);
    List<Theater> getAllTheatres();
    List<Theater> getTheatresByCity(String city);
    Theater getTheatreById(int id) throws TheaterDoesNotExists;
    String updateTheatre(int id , TheatreDto theatreDto);
    String deleteTheatre(int id);
}

