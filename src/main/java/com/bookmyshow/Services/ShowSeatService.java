package com.bookmyshow.Services;

import java.util.List;

import com.bookmyshow.Exceptions.SeatAlreadyLockedException;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.ShowSeatDoesNotExists;
import com.bookmyshow.Models.ShowSeat;

public interface ShowSeatService {
    public List<ShowSeat> getAllShowSeats();
    public ShowSeat getShowSeatById(int id) throws ShowSeatDoesNotExists;
    
    public List<ShowSeat> getAvailableSeatsByShowId(int showId) throws ShowDoesNotExists;

    public List<ShowSeat> getBookedSeatsByShowId(int showId) throws ShowDoesNotExists;
    
    public List<ShowSeat> getShowSeatsByShowId(int showId) throws ShowDoesNotExists;
    /**
     * Locks a specific show seat for a user, if it's available.
     * The lock is temporary and has a 5-minute expiration window.
     *
     * @param seatId The ID of the seat to lock.
     * @param userId The ID of the user requesting the lock.
     * @return The updated ShowSeat object.
     * @throws ShowSeatDoesNotExists if the seat ID is not found.
     * @throws SeatAlreadyLockedException if the seat is already locked by another user and the lock is still valid.
     */
    ShowSeat lockSeat(int seatId, String userId) throws ShowSeatDoesNotExists, SeatAlreadyLockedException;

    /**
     * Unlocks a specific show seat, provided the current user is the one who locked it.
     *
     * @param seatId The ID of the seat to unlock.
     * @param userId The ID of the user requesting the unlock.
     * @return The updated ShowSeat object.
     * @throws ShowSeatDoesNotExists if the seat ID is not found.
     */
    ShowSeat unlockSeat(int seatId, String userId) throws ShowSeatDoesNotExists;
}
