package com.bookmyshow.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmyshow.Models.ShowSeat;
import com.bookmyshow.Services.ShowSeatService;

@RestController
@RequestMapping("/seats")
public class ShowSeatController {
    @Autowired
    private ShowSeatService showSeatService;

    @GetMapping("/all")
    public List<ShowSeat> getAllShowSeats() {
        return showSeatService.getAllShowSeats();
    }

    @GetMapping("/id/{id}")
    public ShowSeat getShowSeatById(@PathVariable int id) {
        try {
            return showSeatService.getShowSeatById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/show/{showId}/available")
    public List<ShowSeat> getAvailableSeatsByShowId(@PathVariable int showId) {
        try {
            return showSeatService.getAvailableSeatsByShowId(showId);
        } catch (Exception e) {
            return null;
        }
    }
    
    @GetMapping("/show/{showId}/booked")
    public List<ShowSeat> getBookedSeatsByShowId(@PathVariable int showId) {
        try {
            return showSeatService.getBookedSeatsByShowId(showId);
        } catch (Exception e) {
            return null;
        }
    }
    

    @GetMapping("/show/{showId}")
    public List<ShowSeat> getShowSeatsByShowId(@PathVariable int showId) {
        try {
            return showSeatService.getShowSeatsByShowId(showId);
        } catch (Exception e) {
            return null;
        }
    }



}
