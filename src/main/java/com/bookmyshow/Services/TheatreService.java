package com.bookmyshow.Services;


import java.util.List;


import com.bookmyshow.Dtos.RequestDtos.TheatreDto;
import com.bookmyshow.Models.Theatre;

public interface TheatreService {
    String addTheatre(TheatreDto theatreDto);
    List<Theatre> getAllTheatres();
    List<Theatre> getTheatresByCity(String city);
    String updateTheatre(int id , TheatreDto theatreDto);
    String deleteTheatre(int id);
}

