// MovieController.java
package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.MovieEntryDto;
import com.bookmyshow.Models.Movie;
import com.bookmyshow.Services.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/add")
    public ResponseEntity<String> addMovie(@RequestBody @Valid MovieEntryDto movieEntryDto) {
        String response = movieService.addMovie(movieEntryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    
    @GetMapping("/{name}")
    public ResponseEntity<Movie> getMovieByName(@PathVariable String name) {
        Movie movie = movieService.getMovieByName(name);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

}
