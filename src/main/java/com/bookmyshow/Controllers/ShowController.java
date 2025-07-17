package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Import ResponseStatusException

import java.sql.Time;
import java.util.List;

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
            // Log the exception for debugging purposes on the server side
            System.err.println("Error fetching show by ID: " + e.getMessage());
            e.printStackTrace();
            // Re-throw as a ResponseStatusException for proper HTTP error response
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/addShow")
    public String addShow(@RequestBody ShowEntryDto showEntryDto) {
        try {
            String res = showService.addShow(showEntryDto);
            return res;
        } catch (Exception e) {
            // Log the exception for debugging purposes on the server side
            System.err.println("Error adding show: " + e.getMessage());
            e.printStackTrace(); // This will print the full stack trace to your backend console
            // Re-throw as a ResponseStatusException to ensure a proper 500 error with details
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
        // This method does not throw checked exceptions, so no try-catch needed here
        return showService.showTimingsOnDate(showTimingsDto);
    }

    @GetMapping("/movieHavingMostShows")
    public String movieHavingMostShows() {
        // This method does not throw checked exceptions, so no try-catch needed here
        return showService.movieHavingMostShows();
    }
}
