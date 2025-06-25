// MovieController.java
package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.MovieEntryDto;
import com.bookmyshow.Models.Movie;
import com.bookmyshow.Services.MovieService;
import jakarta.validation.Valid;

import java.util.List;

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
    
    @GetMapping("/totalCollection/{movieId}")
    public ResponseEntity<Long> totalCollection(@PathVariable Integer movieId) {
        try {
            Long result = movieService.totalCollection(movieId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return null;
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movieList = movieService.getAllMovies();
        return new ResponseEntity<>(movieList, HttpStatus.OK);
    }
    
    @GetMapping("/id/{id}") // ✅ GET by ID
    public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
        Movie movie = movieService.getMovieById(id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @DeleteMapping("/{id}") // ✅ DELETE
    public ResponseEntity<String> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>("Movie deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}") // ✅ PUT/Update
    public ResponseEntity<String> updateMovie(@PathVariable Integer id,
                                              @RequestBody @Valid MovieEntryDto movieEntryDto) {
        movieService.updateMovie(id, movieEntryDto);
        return new ResponseEntity<>("Movie updated successfully", HttpStatus.OK);
    }



}
