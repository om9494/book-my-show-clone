package com.bookmyshow.Controllers;


import com.bookmyshow.Dtos.RequestDtos.TheatreDto;
import com.bookmyshow.Services.TheatreService;
import com.bookmyshow.Models.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theatre")
public class TheatreController {
//changes

    private final TheatreService theatreService;

    @Autowired
    // constructor injection is a safer choice. we can also make var final
    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addTheatre")
    public ResponseEntity<String> addTheatre(@RequestBody TheatreDto theatreDto) {
        try {
            String result = theatreService.addTheatre(theatreDto);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Theater>> getAllTheatres() {
        return ResponseEntity.ok(theatreService.getAllTheatres());
        /*
        ResponseEntity.ok(data) is a shortcut for: new ResponseEntity<>(data, HttpStatus.OK)
        In simple terms:
            .ok(...) means:
            Return this data
            Set the HTTP status to 200 OK
         */
    }

    @GetMapping("/city/{name}")
    public ResponseEntity<List<Theater>> getTheatresByCity(@PathVariable String name) {
        return ResponseEntity.ok(theatreService.getTheatresByCity(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateTheatre/{id}")
    public ResponseEntity<String> updateTheatre(@PathVariable int id ,
                                                @RequestBody TheatreDto theatreDto){
        try{
            String result = theatreService.updateTheatre(id , theatreDto);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheatre(@PathVariable int id) {
        return ResponseEntity.ok(theatreService.deleteTheatre(id));
    }


}
