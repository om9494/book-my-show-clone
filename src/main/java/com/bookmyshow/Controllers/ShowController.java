package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsAllTheaterDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Models.SeatPrice;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.HashMap;

@RestController
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping("/getAllShows")
    public List<Show> getAllShows() {
        return showService.getAllShows();
    }

    @GetMapping("/getShowById/{id}")
    public Show getShowById(@PathVariable int id) {
        try {
            return showService.getShowById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/addShow")
    public String addShow(@RequestBody ShowEntryDto showEntryDto) {
        try {
            String res = showService.addShow(showEntryDto);
            return res;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/updateShow/{id}")
    public String updateShow(@PathVariable int id, @RequestBody ShowEntryDto showEntryDto) {
        try {
            String res = showService.updateShow(id, showEntryDto);
            return res;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/deleteShow/{id}")
    public String deleteShow(@PathVariable int id) {
        try {
            String res = showService.deleteShow(id);
            return res;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/associateShowSeats")
    public String associateShowSeats(@RequestBody ShowSeatEntryDto showSeatEntryDto) {
        try {
            String res = showService.associateShowSeats(showSeatEntryDto);
            return res;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/showTimingsOnDate")
    public List<Time> showTimingsOnDate(@RequestBody ShowTimingsDto showTimingsDto) {
        System.out.println("Fetching show timings for date: " + showTimingsDto.getDate());
        return showService.showTimingsOnDate(showTimingsDto);
    }

    @GetMapping("/theaterAndShowTimingsByMovie")
    public HashMap<Integer, HashMap<Integer, Time>> theaterAndShowTimingsByMovie(
            @RequestParam(name = "movieId") Integer movieId,
            @RequestParam(name = "city") String city,
            @RequestParam(name = "date") Date date
    ) {
        try 
        {
            System.out.println("Fetching theater and show timings for movie ID: " + movieId + " on date: " + date + " in city: " + city);
            return showService.getTheaterAndShowTimingsByMovie(movieId, date, city); 
        } catch (MovieDoesNotExists e) {
            return null;
        }
    }

    @GetMapping("/seat/prices/{showId}")
    public HashMap<String, Integer> getSeatsPrices(@PathVariable Integer showId) {
        try {
            return showService.getSeatsPrices(showId);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/movieHavingMostShows")
    public String movieHavingMostShows() {
        return showService.movieHavingMostShows();
    }

}
