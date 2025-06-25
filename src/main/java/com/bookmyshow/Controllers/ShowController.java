package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/movie/{movieId}/theater/{theaterId}")
    public List<Show> getShowByMovieAndTheater(@PathVariable Integer movieId, @PathVariable Integer theaterId) {
        try {
            return showService.getShowByMovieAndTheater(movieId, theaterId);
        } catch (Exception e) {
            return null;
        }
    }
    

    @GetMapping("/movie/{movieId}")
    public List<Show> getAllShowByMovie(@PathVariable Integer movieId) {
        try {
            return showService.getAllShowByMovie(movieId);
        } catch (Exception e) {
            return null;
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
        return showService.showTimingsOnDate(showTimingsDto);
    }

    @GetMapping("/movieHavingMostShows")
    public String movieHavingMostShows() {
        return showService.movieHavingMostShows();
    }

}
