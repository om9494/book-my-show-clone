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
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

    // @Autowired
    // private JavaMailSender mailSender;

    public TicketResponseDto ticketBooking(TicketEntryDto ticketEntryDto) throws RequestedSeatAreNotAvailable, UserDoesNotExist, ShowDoesNotExists{
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
        if (requestedSeats == null || requestedSeats.size() != 1) {
            throw new RequestedSeatAreNotAvailable(); // Only one seat per ticket as per model
        }
        String requestedSeatNo = requestedSeats.get(0);

        List<ShowSeat> showSeatList = showSeatRepository.findByShowId(show.getShowId());
        ShowSeat bookedSeat = null;
        for (ShowSeat seat : showSeatList) {
            if (seat.getSeatNo().equals(requestedSeatNo)) {
                if (!seat.getIsAvailable()) {
                    throw new RequestedSeatAreNotAvailable();
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

        return TicketTransformer.returnTicket(show, ticket);
    }

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