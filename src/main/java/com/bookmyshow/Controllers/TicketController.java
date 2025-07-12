
package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.TicketEntryDto;
import com.bookmyshow.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/book")
    public ResponseEntity<TicketResponseDto> ticketBooking(@RequestBody TicketEntryDto ticketEntryDto) {
        // Let exceptions bubble up to GlobalExceptionHandler
        TicketResponseDto result = ticketService.ticketBooking(ticketEntryDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
