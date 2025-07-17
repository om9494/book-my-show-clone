package com.bookmyshow.Services;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.bookmyshow.Dtos.RequestDtos.TheatreDto;
import com.bookmyshow.Models.Theater;
import com.bookmyshow.Repositories.TheatreRepository;
import com.bookmyshow.Transformers.TheaterTransformer;


import java.util.List;

@Service
public class TheatreServiceImpl implements TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    @Override
    public String addTheatre(TheatreDto dto) {
        // Use the transformer to create the Theater object
        Theater theatre = TheaterTransformer.theaterDtoToTheater(dto); 
        theatreRepository.save(theatre);
        return "Theatre added successfully!";
    }

    @Override
    public List<Theater> getAllTheatres() {
        return theatreRepository.findAll();
    }

    @Override
    public List<Theater> getTheatresByCity(String city) {
        return theatreRepository.findByCityIgnoreCase(city);
    }

    @Override
    public String updateTheatre(int id, TheatreDto dto) {
        Theater theatre = theatreRepository.findById(id).orElseThrow(() ->
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

