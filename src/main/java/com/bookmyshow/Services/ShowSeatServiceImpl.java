package com.bookmyshow.Services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.ShowSeatDoesNotExists;
import com.bookmyshow.Models.ShowSeat;
import com.bookmyshow.Repositories.ShowRepository;
import com.bookmyshow.Repositories.ShowSeatRepositiory;

@Service
public class ShowSeatServiceImpl implements ShowSeatService {

    @Autowired
    private ShowSeatRepositiory showSeatRepositiory;

    @Autowired
    private ShowRepository showRepository;

    @Override
    public List<ShowSeat> getAllShowSeats() {
        return showSeatRepositiory.findAll();
    }

    @Override
    public ShowSeat getShowSeatById(int id) throws ShowSeatDoesNotExists {
        Optional<ShowSeat> showSeatOptional = showSeatRepositiory.findById(id);
        if (showSeatOptional.isEmpty()) {
            throw new ShowSeatDoesNotExists();
        }
        return showSeatOptional.get();
    }

    @Override
    public List<ShowSeat> getAvailableSeatsByShowId(int showId) throws ShowDoesNotExists {
        showRepository.findById(showId).orElseThrow(ShowDoesNotExists::new);
        return showSeatRepositiory.findAvailableSeatsByShowId(showId);
    }

    @Override
    public List<ShowSeat> getBookedSeatsByShowId(int showId) throws ShowDoesNotExists {
        showRepository.findById(showId).orElseThrow(ShowDoesNotExists::new);
        return showSeatRepositiory.findBookedSeatsByShowId(showId);
    }

    @Override
    public List<ShowSeat> getShowSeatsByShowId(int showId) throws ShowDoesNotExists {
        showRepository.findById(showId).orElseThrow(ShowDoesNotExists::new);
        return showSeatRepositiory.findByShowId(showId);
    }

    
}
