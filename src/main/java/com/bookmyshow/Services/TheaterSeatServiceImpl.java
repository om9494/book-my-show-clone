package com.bookmyshow.Services;

import com.bookmyshow.Dtos.RequestDtos.TheaterSeatEntryDto;
import com.bookmyshow.Models.Theater;
import com.bookmyshow.Models.TheaterSeat;
import com.bookmyshow.Repositories.TheaterSeatRepository;
import com.bookmyshow.Repositories.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterSeatServiceImpl implements TheaterSeatService {

    @Autowired
    private TheaterSeatRepository theaterSeatRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Override
    public String addTheaterSeat(TheaterSeatEntryDto dto) {
        Optional<Theater> theaterOpt = theatreRepository.findById(dto.getTheaterId());
        if (theaterOpt.isEmpty()) {
            return "Theater not found";
        }
        TheaterSeat seat = TheaterSeat.builder()
                .rowLabel(dto.getRowLabel())
                .seatCount(dto.getSeatCount())
                .seatType(dto.getSeatType())
                .theater(theaterOpt.get())
                .build();
        theaterSeatRepository.save(seat);
        return "Theater seat added successfully";
    }

    @Override
    public List<TheaterSeat> getSeatsByTheater(Integer theaterId) {
        return theaterSeatRepository.findAllByTheaterId(theaterId);
    }

    @Override
    public String updateTheaterSeat(Integer id, TheaterSeatEntryDto dto) {
        Optional<TheaterSeat> seatOpt = theaterSeatRepository.findById(id);
        if (seatOpt.isEmpty()) {
            return "Theater seat not found";
        }
        TheaterSeat seat = seatOpt.get();
        seat.setRowLabel(dto.getRowLabel());
        seat.setSeatCount(dto.getSeatCount());
        seat.setSeatType(dto.getSeatType());
        if (dto.getTheaterId() != null) {
            Optional<Theater> theaterOpt = theatreRepository.findById(dto.getTheaterId());
            theaterOpt.ifPresent(seat::setTheater);
        }
        theaterSeatRepository.save(seat);
        return "Theater seat updated successfully";
    }

    @Override
    public String deleteTheaterSeat(Integer id) {
        if (!theaterSeatRepository.existsById(id)) {
            return "Theater seat not found";
        }
        theaterSeatRepository.deleteById(id);
        return "Theater seat deleted successfully";
    }
}
