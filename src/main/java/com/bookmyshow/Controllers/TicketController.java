
package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.TicketEntryDto;
import com.bookmyshow.Dtos.RequestDtos.TicketUpdateDto;
import com.bookmyshow.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.Services.TicketService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<List<TicketResponseDto>> getTicketsByUserId(@PathVariable Integer userId) {
        List<TicketResponseDto> tickets = ticketService.getAllTicketsByUserId(userId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<TicketResponseDto>> getActiveTicketsByUserId(@PathVariable Integer userId) {
        List<TicketResponseDto> tickets = ticketService.getActiveTicketsByUserId(userId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PostMapping("/book")
    public ResponseEntity<List<TicketResponseDto>> ticketBooking(@RequestBody TicketEntryDto ticketEntryDto) throws Exception {
        // Let exceptions bubble up to GlobalExceptionHandler
        List<TicketResponseDto> result = ticketService.ticketBooking(ticketEntryDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    
 // --- START OF NEW ENDPOINT ---

    /**
     * API endpoint to update an existing ticket.
     *
     * @param ticketUpdateDto DTO containing all necessary information for the update.
     * @return A response entity with the updated ticket details.
     */
    @PutMapping("/update")
    public ResponseEntity<TicketResponseDto> updateTicket(@RequestBody TicketUpdateDto ticketUpdateDto) {
        // Exceptions will be handled by a global exception handler.
        TicketResponseDto updatedTicket = ticketService.updateTicket(ticketUpdateDto);
        return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
    }

    // --- END OF NEW ENDPOINT ---

    
    
}
