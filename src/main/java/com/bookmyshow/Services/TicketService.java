package com.bookmyshow.Services;

import com.bookmyshow.Dtos.RequestDtos.TicketEntryDto;
import com.bookmyshow.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.Exceptions.RequestedSeatAreNotAvailable;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.UserDoesNotExist;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.ShowSeat;
import com.bookmyshow.Models.Ticket;
import com.bookmyshow.Models.User;
import com.bookmyshow.Repositories.*;
import com.bookmyshow.Transformers.TicketTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    // --- CHANGE 1: Inject the EmailService ---
    @Autowired
    private EmailService emailService;

    public List<TicketResponseDto> getAllTicketsByUserId(Integer userId) throws UserDoesNotExist {
        userRepository.findById(userId).orElseThrow(UserDoesNotExist::new);
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        List<TicketResponseDto> ticketResponseDtos = new ArrayList<>();
        for(Ticket ticket : tickets) {
            Show show = ticket.getShow();
            ticketResponseDtos.add(TicketTransformer.returnTicket(show, ticket));
        }
        return ticketResponseDtos;
    }

    public List<TicketResponseDto> getActiveTicketsByUserId(Integer userId) throws UserDoesNotExist {
        userRepository.findById(userId).orElseThrow(UserDoesNotExist::new);
        Date today = new Date(System.currentTimeMillis());
        System.out.println(today);
        List<Ticket> tickets = ticketRepository.findActiveTicketsByUserId(userId, today);
        List<TicketResponseDto> ticketResponseDtos = new ArrayList<>();
        for(Ticket ticket : tickets){
            Show show = ticket.getShow();
            ticketResponseDtos.add(TicketTransformer.returnTicket(show, ticket));
        }
        return ticketResponseDtos;
    }

    public List<TicketResponseDto> ticketBooking(TicketEntryDto ticketEntryDto) throws RequestedSeatAreNotAvailable, UserDoesNotExist, ShowDoesNotExists{
        // check show present
        Optional<Show> showOpt = showRepository.findById(ticketEntryDto.getShowId());
        if(showOpt.isEmpty()) {
            throw new ShowDoesNotExists();
        }

        // check user present
        Optional<User> userOpt = userRepository.findById(ticketEntryDto.getUserId());
        if(userOpt.isEmpty()) {
            throw new UserDoesNotExist();
        }

        User user = userOpt.get();
        Show show = showOpt.get();

        List<String> requestedSeats = ticketEntryDto.getRequestSeats();
        if (requestedSeats == null || requestedSeats.size() < 1) {
            throw new RequestedSeatAreNotAvailable("No Seat Requested!");
        }
        List<TicketResponseDto> tickets = new ArrayList<>();
        for(String seatno : requestedSeats){
            String requestedSeatNo = seatno;

            List<ShowSeat> showSeatList = showSeatRepository.findByShowId(show.getShowId());
            ShowSeat bookedSeat = null;
            for (ShowSeat seat : showSeatList) {
                if (seat.getSeatNo().equals(requestedSeatNo)) {
                    if (!seat.getIsAvailable()) {
                        throw new RequestedSeatAreNotAvailable("Seat " + seatno + " is already booked.");
                    }
                    bookedSeat = seat;
                    break;
                }
            }
            if (bookedSeat == null) {
                throw new RequestedSeatAreNotAvailable();
            }

            // Mark seat as unavailable and calculate price
            bookedSeat.setIsAvailable(Boolean.FALSE);
            Integer ticketPrice = bookedSeat.getPrice();

            // create ticket entity and set all attribute
            Ticket ticket = new Ticket();
            ticket.setTicketPrice(ticketPrice);
            ticket.setShowSeat(bookedSeat);
            ticket.setUser(user);
            ticket.setShow(show);

            // Save entities
            showSeatRepository.save(bookedSeat);
            ticket = ticketRepository.save(ticket);
            userRepository.save(user);
            showRepository.save(show);
            tickets.add(TicketTransformer.returnTicket(show, ticket));
        }

        // --- CHANGE 2: Send the confirmation email ---
        // This code will execute only after the tickets have been successfully saved to the database.
        String userEmail = user.getEmail(); // Get the user's email from the User object.
        emailService.sendTicketConfirmationEmail(userEmail, tickets);
        // --- END OF CHANGES ---

        return tickets;
    }

    // The helper methods below remain unchanged.
    private Boolean isSeatAvailable(List<ShowSeat> showSeatList, List<String> requestSeats) {
        for(ShowSeat showSeat : showSeatList) {
            String seatNo = showSeat.getSeatNo();
            if(requestSeats.contains(seatNo)) {
                if(!showSeat.getIsAvailable()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Integer getPriceAndAssignSeats(List<ShowSeat> showSeatList, List<String> requestSeats) {
        Integer totalAmount = 0;
        for(ShowSeat showSeat : showSeatList) {
            if(requestSeats.contains(showSeat.getSeatNo())) {
                totalAmount += showSeat.getPrice();
                showSeat.setIsAvailable(Boolean.FALSE);
            }
        }
        return totalAmount;
    }

    private String listToString(List<String> requestSeats) {
        StringBuilder sb = new StringBuilder();
        for(String s : requestSeats) {
            sb.append(s).append(",");
        }
        return sb.toString();
    }
}
