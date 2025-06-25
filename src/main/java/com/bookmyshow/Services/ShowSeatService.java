package com.bookmyshow.Services;

import java.util.List;

import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.ShowSeatDoesNotExists;
import com.bookmyshow.Models.ShowSeat;

public interface ShowSeatService {
    public List<ShowSeat> getAllShowSeats();
    public ShowSeat getShowSeatById(int id) throws ShowSeatDoesNotExists;
    
    public List<ShowSeat> getAvailableSeatsByShowId(int showId) throws ShowDoesNotExists;

    public List<ShowSeat> getBookedSeatsByShowId(int showId) throws ShowDoesNotExists;
    
    public List<ShowSeat> getShowSeatsByShowId(int showId) throws ShowDoesNotExists;
}
