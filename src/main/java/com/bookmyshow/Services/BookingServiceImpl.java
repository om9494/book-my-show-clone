package com.bookmyshow.Services;

import com.bookmyshow.Dtos.RequestDtos.BookingRequestDto;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.UserDoesNotExist;
import com.bookmyshow.Models.*;
import com.bookmyshow.Repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Override
    public String bookTicket(BookingRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(UserDoesNotExist::new);

        Show show = showRepository.findById(dto.getShowId())
                .orElseThrow(ShowDoesNotExists::new);

        List<ShowSeat> showSeats = showSeatRepository.findByShowId(show.getShowId());
        List<String> requestedSeatNumbers = dto.getSeatNumbers();
        List<Integer> ticketIds = new ArrayList<>();

        for (String seatNo : requestedSeatNumbers) {
            ShowSeat seatToBook = null;
            for (ShowSeat seat : showSeats) {
                if (seat.getSeatNo().equals(seatNo)) {
                    seatToBook = seat;
                    break;
                }
            }
            if (seatToBook == null || !seatToBook.getIsAvailable()) {
                throw new RuntimeException("Seat " + seatNo + " is already booked or does not exist.");
            }
            seatToBook.setIsAvailable(false);
            showSeatRepository.save(seatToBook);

            Ticket ticket = Ticket.builder()
                    .show(show)
                    .user(user)
                    .showSeat(seatToBook)
                    .ticketPrice(seatToBook.getPrice())
                    .build();
            ticketRepository.save(ticket);
            ticketIds.add(ticket.getTicketId());
        }
        return "Booking successful! Ticket IDs: " + ticketIds;
    }

    @Override
    public Ticket getBookingById(Integer bookingId) {
        return ticketRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Ticket> getBookingsByUserId(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Fetch tickets by userId from the repository
        return ticketRepository.findByUserId(userId);
    }

    @Override
    public void cancelBooking(Integer bookingId) {
        Ticket ticket = ticketRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Set the booked seat as available again
        ShowSeat seat = ticket.getShowSeat();
        if (seat != null) {
            seat.setIsAvailable(true);
            showSeatRepository.save(seat);
        }
        ticketRepository.delete(ticket);
    }
}
