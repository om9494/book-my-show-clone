package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.TheatreDto;
import com.bookmyshow.Models.Theater;
import com.bookmyshow.Services.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theaters")
public class TheaterController {

    @Autowired
    private TheatreService theatreService;

    @PostMapping
    public ResponseEntity<String> addTheatre(@RequestBody TheatreDto theatreDto) {
        String result = theatreService.addTheatre(theatreDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheatres() {
        List<Theater> theaters = theatreService.getAllTheatres();
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Theater>> getTheatresByCity(@PathVariable String city) {
        List<Theater> theaters = theatreService.getTheatresByCity(city);
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Theater> getTheatreById(@PathVariable int id){
        try{Theater theater = theatreService.getTheatreById(id);
        return ResponseEntity.ok(theater);
        }
        catch(Exception e){
            return null;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTheatre(@PathVariable int id, @RequestBody TheatreDto theatreDto) {
        String result = theatreService.updateTheatre(id, theatreDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheatre(@PathVariable int id) {
        String result = theatreService.deleteTheatre(id);
        return ResponseEntity.ok(result);
    }
}
