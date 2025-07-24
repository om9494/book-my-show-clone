package com.bookmyshow.Services;

import com.bookmyshow.Dtos.RequestDtos.TicketEntryDto;
import com.bookmyshow.Dtos.RequestDtos.TicketUpdateDto;
import com.bookmyshow.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.Exceptions.RequestedSeatAreNotAvailable;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.TicketUpdateException;
import com.bookmyshow.Exceptions.UserDoesNotExist;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.ShowSeat;
import com.bookmyshow.Models.Ticket;
import com.bookmyshow.Models.User;
import com.bookmyshow.Repositories.*;
import com.bookmyshow.Transformers.TicketTransformer;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date; // Switched to java.util.Date for the helper method
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private EmailService emailService;

    // ... (Your existing ticketBooking and other methods remain unchanged)
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
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        List<Ticket> tickets = ticketRepository.findActiveTicketsByUserId(userId, today);
        List<TicketResponseDto> ticketResponseDtos = new ArrayList<>();
        for(Ticket ticket : tickets){
            Show show = ticket.getShow();
            ticketResponseDtos.add(TicketTransformer.returnTicket(show, ticket));
        }
        return ticketResponseDtos;
    }

    public List<TicketResponseDto> ticketBooking(TicketEntryDto ticketEntryDto) throws RequestedSeatAreNotAvailable, UserDoesNotExist, ShowDoesNotExists{
        Optional<Show> showOpt = showRepository.findById(ticketEntryDto.getShowId());
        if(showOpt.isEmpty()) {
            throw new ShowDoesNotExists();
        }

        Optional<User> userOpt = userRepository.findById(ticketEntryDto.getUserId());
        if(userOpt.isEmpty()) {
            throw new UserDoesNotExist();
        }

        User user = userOpt.get();
        Show show = showOpt.get();

        List<String> requestedSeats = ticketEntryDto.getRequestSeats();
        if (requestedSeats == null || requestedSeats.isEmpty()) {
            throw new RequestedSeatAreNotAvailable("No Seat Requested!");
        }
        List<TicketResponseDto> tickets = new ArrayList<>();
        for(String seatno : requestedSeats){
            List<ShowSeat> showSeatList = showSeatRepository.findByShowId(show.getShowId());
            ShowSeat bookedSeat = null;
            for (ShowSeat seat : showSeatList) {
                if (seat.getSeatNo().equals(seatno)) {
                    if (!seat.getIsAvailable()) {
                        throw new RequestedSeatAreNotAvailable("Seat " + seatno + " is already booked.");
                    }
                    bookedSeat = seat;
                    break;
                }
            }
            if (bookedSeat == null) {
                throw new RequestedSeatAreNotAvailable("The requested seat " + seatno + " does not exist.");
            }

            bookedSeat.setIsAvailable(Boolean.FALSE);
            Integer ticketPrice = bookedSeat.getPrice();

            Ticket ticket = new Ticket();
            ticket.setTicketPrice(ticketPrice);
            ticket.setShowSeat(bookedSeat);
            ticket.setUser(user);
            ticket.setShow(show);

            showSeatRepository.save(bookedSeat);
            ticket = ticketRepository.save(ticket);
            
            // Assuming User and Show models have lists of tickets that need updating
            // If not, these saves might be redundant but are safe.
            userRepository.save(user); 
            showRepository.save(show);
            tickets.add(TicketTransformer.returnTicket(show, ticket));
        }

        String userEmail = user.getEmail();
        emailService.sendTicketConfirmationEmail(userEmail, tickets);

        return tickets;
    }

    // --- START OF CORRECTED IMPLEMENTATION ---

    public static final int UPDATE_FEE = 20;
    public static final int MIN_HOURS_BEFORE_SHOW_FOR_UPDATE = 3;

    @Transactional
    public TicketResponseDto updateTicket(TicketUpdateDto dto) throws UserDoesNotExist, TicketUpdateException, ShowDoesNotExists, RequestedSeatAreNotAvailable {
        // 1. Fetch original ticket and validate user ownership
        Ticket originalTicket = ticketRepository.findById(dto.getOriginalTicketId())
                .orElseThrow(() -> new TicketUpdateException("Ticket with ID " + dto.getOriginalTicketId() + " not found."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(UserDoesNotExist::new);

        // FIX 1: Assuming your User model's primary key is 'id' and the getter is `getId()`.
        // I changed `getUserId()` to `getId()` to match standard JPA entity conventions.
        if (!originalTicket.getUser().getId().equals(user.getId())) {
            throw new TicketUpdateException("Access Denied: The ticket does not belong to the specified user.");
        }

        // 2. Fetch original show and seat details
        Show oldShow = originalTicket.getShow();
        ShowSeat oldSeat = originalTicket.getShowSeat();

        // 3. Validate the update time window (This part is now corrected)
        validateTimeWindow(oldShow);

        // 4. Fetch the new show and the specific new seat
        Show newShow = showRepository.findById(dto.getNewShowId())
                .orElseThrow(ShowDoesNotExists::new);
        
        // The findAndValidateNewSeat helper is now corrected to use the repository.
        ShowSeat newSeat = findAndValidateNewSeat(newShow, dto.getNewSeatNo());

        // 5. Perform the update
        oldSeat.setIsAvailable(Boolean.TRUE);
        showSeatRepository.save(oldSeat);

        newSeat.setIsAvailable(Boolean.FALSE);
        showSeatRepository.save(newSeat);

        originalTicket.setShow(newShow);
        originalTicket.setShowSeat(newSeat);
        originalTicket.setTicketPrice(newSeat.getPrice() + UPDATE_FEE);

        Ticket updatedTicket = ticketRepository.save(originalTicket);

        // 6. Send a new confirmation email
        TicketResponseDto ticketResponseDto = TicketTransformer.returnTicket(newShow, updatedTicket);
        emailService.sendTicketConfirmationEmail(user.getEmail(), List.of(ticketResponseDto));

        return ticketResponseDto;
    }
    
    /**
     * Helper method to find a seat in a given show and validate its availability.
     */
    private ShowSeat findAndValidateNewSeat(Show show, String seatNo) throws RequestedSeatAreNotAvailable {
        // FIX 2: Correctly fetch seats using the repository instead of a non-existent list in the Show model.
        List<ShowSeat> showSeats = showSeatRepository.findByShowId(show.getShowId());
        
        for (ShowSeat seat : showSeats) {
            if (seat.getSeatNo().equals(seatNo)) {
                if (!seat.getIsAvailable()) {
                    throw new RequestedSeatAreNotAvailable("The requested seat " + seatNo + " is already booked.");
                }
                return seat;
            }
        }
        throw new RequestedSeatAreNotAvailable("The requested seat " + seatNo + " does not exist for this show.");
    }

    /**
     * Helper method to check if the update request is within the allowed time frame.
     */
    private void validateTimeWindow(Show show) throws TicketUpdateException {
        // Note: The `show.getDate()` method should exist because your `Show` model has a `date` field
        // and the `@Data` annotation from Lombok automatically generates getters and setters.
        Date showDateTime = combineDateAndTime(show.getDate(), show.getTime());
        long hoursDifference = TimeUnit.MILLISECONDS.toHours(showDateTime.getTime() - new Date().getTime());

        if (hoursDifference < MIN_HOURS_BEFORE_SHOW_FOR_UPDATE) {
            throw new TicketUpdateException("Ticket update failed. Updates are only allowed up to " +
                    MIN_HOURS_BEFORE_SHOW_FOR_UPDATE + " hours before the show.");
        }
    }

    /**
     * Utility to combine java.sql.Date and java.sql.Time into a single java.util.Date.
     */
    private Date combineDateAndTime(java.sql.Date date, Time time) {
        // FIX 3: Changed method to correctly return `java.util.Date` and removed the invalid cast.
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
        dateCal.set(Calendar.MILLISECOND, 0);

        return dateCal.getTime();
    }
}