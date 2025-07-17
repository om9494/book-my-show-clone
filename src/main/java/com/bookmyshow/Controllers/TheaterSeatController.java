package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.TheaterSeatEntryDto;
import com.bookmyshow.Models.TheaterSeat;
import com.bookmyshow.Services.TheaterSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theater-seats")
public class TheaterSeatController {

    @Autowired
    private TheaterSeatService theaterSeatService;

    @PostMapping
    public ResponseEntity<String> addTheaterSeat(@RequestBody TheaterSeatEntryDto dto) {
        String result = theaterSeatService.addTheaterSeat(dto);
        return ResponseEntity.ok(result);
    }
    
    // New bulk-seat endpoint
    @PostMapping("/bulk")
    public ResponseEntity<String> addBulkTheaterSeats(@RequestBody List<TheaterSeatEntryDto> dtos) {
        String result = theaterSeatService.addBulkTheaterSeats(dtos);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<TheaterSeat>> getSeatsByTheater(@PathVariable Integer theaterId) {
        List<TheaterSeat> seats = theaterSeatService.getSeatsByTheater(theaterId);
        return ResponseEntity.ok(seats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTheaterSeat(@PathVariable Integer id, @RequestBody TheaterSeatEntryDto dto) {
        String result = theaterSeatService.updateTheaterSeat(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheaterSeat(@PathVariable Integer id) {
        String result = theaterSeatService.deleteTheaterSeat(id);
        return ResponseEntity.ok(result);
    }
}
