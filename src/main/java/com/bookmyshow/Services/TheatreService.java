package com.example.Super30_Project.Services;

import com.example.Super30_Project.Dtos.TheatreDto;
import com.example.Super30_Project.Entity.Theatre;

import java.util.List;

public interface TheatreService {
    String addTheatre(TheatreDto theatreDto);
    List<Theatre> getAllTheatres();
    List<Theatre> getTheatresByCity(String city);
    String updateTheatre(int id , TheatreDto theatreDto);
    String deleteTheatre(int id);
}

