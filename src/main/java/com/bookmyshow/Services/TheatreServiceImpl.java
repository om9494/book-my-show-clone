package com.bookmyshow.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookmyshow.Dtos.RequestDtos.TheatreDto;
import com.bookmyshow.Models.Theatre;
import com.bookmyshow.Repositories.TheatreRepository;

import java.util.List;

@Service
public class TheatreServiceImpl implements TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    @Override
    public String addTheatre(TheatreDto dto) {
        Theatre theatre = new Theatre();
        theatre.setName(dto.getName());
        theatre.setCity(dto.getCity());
        theatre.setNumberOfScreens(dto.getNumberOfScreens());
        theatreRepository.save(theatre);
        //theatre is now saved in the db
        return "Theatre added successfully!";
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    @Override
    public List<Theatre> getTheatresByCity(String city) {
        return theatreRepository.findByCityIgnoreCase(city);
    }

    @Override
    public String updateTheatre(int id, TheatreDto dto) {
        Theatre theatre = theatreRepository.findById(id).orElseThrow(() ->
                new RuntimeException("There is no such theatre with id: " + id)
        );
        theatre.setName(dto.getName());
        theatre.setCity(dto.getCity());
        theatre.setNumberOfScreens(dto.getNumberOfScreens());
        theatreRepository.save(theatre);
        return "Theatre updated successfully!";
    }

    @Override
    public String deleteTheatre(int id) {
        theatreRepository.deleteById(id);
        return "Theatre deleted successfully.";
    }
}

