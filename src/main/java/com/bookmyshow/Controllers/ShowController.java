package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsAllTheaterDto; // Make sure this DTO exists if used
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Models.SeatPrice;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.HashMap; // Keep this import for HashMap

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
            System.err.println("Error fetching show by ID: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/addShow")
    public String addShow(@RequestBody ShowEntryDto showEntryDto) {
        try {
            String res = showService.addShow(showEntryDto);
            return res;
        } catch (Exception e) {
            System.err.println("Error adding show: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add show: " + e.getMessage(), e);
        }
    }

    @PostMapping("/updateShow/{id}")
    public String updateShow(@PathVariable int id, @RequestBody ShowEntryDto showEntryDto) {
        try {
            String res = showService.updateShow(id, showEntryDto);
            return res;
        } catch (Exception e) {
            System.err.println("Error updating show: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update show: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/deleteShow/{id}")
    public String deleteShow(@PathVariable int id) {
        try {
            String res = showService.deleteShow(id);
            return res;
        } catch (Exception e) {
            System.err.println("Error deleting show: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete show: " + e.getMessage(), e);
        }
    }

    @PostMapping("/associateShowSeats")
    public String associateShowSeats(@RequestBody ShowSeatEntryDto showSeatEntryDto) {
        try {
            String res = showService.associateShowSeats(showSeatEntryDto);
            return res;
        } catch (Exception e) {
            System.err.println("Error associating show seats: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to associate show seats: " + e.getMessage(), e);
        }
    }

    @GetMapping("/showTimingsOnDate")
    public List<Time> showTimingsOnDate(@RequestBody ShowTimingsDto showTimingsDto) {
        System.out.println("Fetching show timings for date: " + showTimingsDto.getDate() +
                           ", theaterId: " + showTimingsDto.getTheaterId() +
                           ", movieId: " + showTimingsDto.getMovieId());
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
            // Log the exception for debugging purposes on the server side
            System.err.println("Error in theaterAndShowTimingsByMovie: Movie does not exist. " + e.getMessage());
            e.printStackTrace();
            // Return an empty HashMap or throw a more specific HTTP error if preferred
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found: " + e.getMessage(), e);
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("An unexpected error occurred in theaterAndShowTimingsByMovie: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage(), e);
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
