package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.BookingRequestDto;
import com.bookmyshow.Models.Ticket;
import com.bookmyshow.Services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<String> bookTicket(@RequestBody BookingRequestDto bookingRequestDto) {
        try {
            String response = bookingService.bookTicket(bookingRequestDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Booking failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getBookingById(@PathVariable Integer id) {
        try {
            Ticket ticket = bookingService.getBookingById(id);
            return new ResponseEntity<>(ticket, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getBookingsByUserId(@PathVariable Integer userId) {
        List<Ticket> tickets = bookingService.getBookingsByUserId(userId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Integer id) {
        try {
            bookingService.cancelBooking(id);
            return new ResponseEntity<>("Booking cancelled successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cancellation failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
