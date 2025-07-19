package com.bookmyshow.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookmyshow.Dtos.RequestDtos.TheatreDto;
<<<<<<< Updated upstream:src/main/java/com/bookmyshow/Services/TheatreServiceImpl.java
=======
import com.bookmyshow.Exceptions.TheaterDoesNotExists;
>>>>>>> Stashed changes:book-my-show-clone-main_3/src/main/java/com/bookmyshow/Services/TheatreServiceImpl.java
import com.bookmyshow.Models.Theater;
import com.bookmyshow.Repositories.TheatreRepository;

import java.util.List;

@Service
public class TheatreServiceImpl implements TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    @Override
    public String addTheatre(TheatreDto dto) {
        Theater theatre = new Theater();
        theatre.setName(dto.getName());
        theatre.setCity(dto.getCity());
        theatre.setNumberOfScreens(dto.getNumberOfScreens());
        theatreRepository.save(theatre);
        //theatre is now saved in the db
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
    public Theater getTheatreById(int id) throws TheaterDoesNotExists {
        Theater theatre = theatreRepository.findById(id).orElseThrow(TheaterDoesNotExists::new);
        return theatre;
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

