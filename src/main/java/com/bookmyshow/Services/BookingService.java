package com.bookmyshow.Services;

import com.bookmyshow.Dtos.RequestDtos.BookingRequestDto;
import com.bookmyshow.Models.Ticket;

import java.util.List;

public interface BookingService {
    String bookTicket(BookingRequestDto bookingRequestDto);
    Ticket getBookingById(Integer bookingId);
    List<Ticket> getBookingsByUserId(Integer userId);
    void cancelBooking(Integer bookingId);
}
