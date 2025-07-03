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

        List<ShowSeat> showSeats = show.getShowSeatList();

        List<ShowSeat> bookedSeats = showSeats.stream()
                .filter(seat -> dto.getSeatNumbers().contains(seat.getSeatNo()) && seat.getIsAvailable())
                .collect(Collectors.toList());

        if (bookedSeats.size() != dto.getSeatNumbers().size()) {
            throw new RuntimeException("Some seats are already booked or do not exist.");
        }

        int totalPrice = 0;
        for (ShowSeat seat : bookedSeats) {
            seat.setIsAvailable(false);
            totalPrice += seat.getPrice();
        }

        Ticket ticket = Ticket.builder()
                .show(show)
                .user(user)
                .totalTicketsPrice(totalPrice)
                .bookedSeats(String.join(",", dto.getSeatNumbers()))
                .build();

        show.getTicketList().add(ticket);
        user.getTicketList().add(ticket);

        ticketRepository.save(ticket);
        return "Booking successful! Ticket ID: " + ticket.getTicketId();
    }

    @Override
    public Ticket getBookingById(Integer bookingId) {
        return ticketRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Ticket> getBookingsByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getTicketList();
    }

    @Override
    public void cancelBooking(Integer bookingId) {
        Ticket ticket = ticketRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Set seats as available again
        Show show = ticket.getShow();
        List<String> seatsToFree = Arrays.asList(ticket.getBookedSeats().split(","));

        for (ShowSeat seat : show.getShowSeatList()) {
            if (seatsToFree.contains(seat.getSeatNo())) {
                seat.setIsAvailable(true);
            }
        }

        ticketRepository.delete(ticket);
    }
}
