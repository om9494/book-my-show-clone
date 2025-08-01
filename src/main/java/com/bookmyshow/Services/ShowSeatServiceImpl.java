package com.bookmyshow.Services;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit; // Import ChronoUnit
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
// It's generally better to use Spring's Transactional annotation
import org.springframework.transaction.annotation.Transactional; 

import com.bookmyshow.Exceptions.SeatAlreadyLockedException;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.ShowSeatDoesNotExists;
import com.bookmyshow.Models.ShowSeat;
import com.bookmyshow.Repositories.ShowRepository;
import com.bookmyshow.Repositories.ShowSeatRepository;

@Service
public class ShowSeatServiceImpl implements ShowSeatService {

    @Autowired
    // Corrected the typo from "Repositiory" to "Repository"
    private ShowSeatRepository showSeatRepository; 

    @Autowired
    private ShowRepository showRepository;
    
    // A constant for lock timeout is good practice
    private static final long LOCK_TIMEOUT_MINUTES = 5;

    @Override
    public List<ShowSeat> getAllShowSeats() {
        return showSeatRepository.findAll();
    }

    @Override
    public ShowSeat getShowSeatById(int id) throws ShowSeatDoesNotExists {
        // Using orElseThrow is a more concise way to handle Optionals
        return showSeatRepository.findById(id)
                .orElseThrow(ShowSeatDoesNotExists::new);
    }

    @Override
    public List<ShowSeat> getAvailableSeatsByShowId(int showId) throws ShowDoesNotExists {
        showRepository.findById(showId).orElseThrow(ShowDoesNotExists::new);
        return showSeatRepository.findAvailableSeatsByShowId(showId);
    }

    @Override
    public List<ShowSeat> getBookedSeatsByShowId(int showId) throws ShowDoesNotExists {
        showRepository.findById(showId).orElseThrow(ShowDoesNotExists::new);
        return showSeatRepository.findBookedSeatsByShowId(showId);
    }

    @Override
    public List<ShowSeat> getShowSeatsByShowId(int showId) throws ShowDoesNotExists {
        showRepository.findById(showId).orElseThrow(ShowDoesNotExists::new);
        return showSeatRepository.findByShowId(showId);
    }
    
    @Override
    @Transactional
    public ShowSeat lockSeat(int seatId, String userId) throws ShowSeatDoesNotExists, SeatAlreadyLockedException {
        ShowSeat showSeat = showSeatRepository.findById(seatId)
                .orElseThrow(ShowSeatDoesNotExists::new);

        // 1. CRITICAL FIX: First, check if the seat is already permanently booked.
        if (!showSeat.getIsAvailable()) {
            throw new SeatAlreadyLockedException("This seat has already been booked.");
        }

        // 2. CRITICAL FIX: Check if the seat is locked by SOMEONE ELSE.
        if (showSeat.getLockedByUserId() != null && !showSeat.getLockedByUserId().equals(userId)) {
            LocalDateTime lockTime = showSeat.getLockedAt();
            long minutesSinceLock = ChronoUnit.MINUTES.between(lockTime, LocalDateTime.now());

            // If the lock by another user is still valid, throw an exception.
            if (minutesSinceLock < LOCK_TIMEOUT_MINUTES) {
                throw new SeatAlreadyLockedException("This seat is currently held by another user. Please try again in a few moments.");
            }
        }

        // If we pass the checks, lock the seat for the current user.
        // This will also correctly "re-lock" or refresh the lock for the same user.
        showSeat.setLockedByUserId(userId);
        showSeat.setLockedAt(LocalDateTime.now());
        
        return showSeatRepository.save(showSeat);
    }

    @Override
    @Transactional
    public ShowSeat unlockSeat(int seatId, String userId) throws ShowSeatDoesNotExists {
        ShowSeat showSeat = showSeatRepository.findById(seatId)
                .orElseThrow(ShowSeatDoesNotExists::new);
        
        // Your unlock logic is good. We only unlock if the user ID matches.
        if (userId.equals(showSeat.getLockedByUserId())) {
            showSeat.setLockedByUserId(null);
            showSeat.setLockedAt(null);
            return showSeatRepository.save(showSeat);
        }
        
        // If the lock belongs to someone else, do nothing and return the seat as is.
        return showSeat;
    }
}