package com.bookmyshow.Services;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Enums.SeatType;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.TheaterDoesNotExists;
import com.bookmyshow.Models.Movie;
import com.bookmyshow.Models.SeatPrice;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.ShowSeat;
import com.bookmyshow.Models.Theater;
import com.bookmyshow.Models.TheaterSeat;
import com.bookmyshow.Repositories.MovieRepository;
import com.bookmyshow.Repositories.ShowRepository;
import com.bookmyshow.Repositories.ShowSeatRepository;
import com.bookmyshow.Repositories.TheaterSeatRepository;
import com.bookmyshow.Repositories.TheatreRepository;

@Service
public class ShowServiceImpl implements ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private TheaterSeatRepository theaterSeatRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Override
    @Transactional
    public String addShow(ShowEntryDto showEntryDto) throws MovieDoesNotExists, TheaterDoesNotExists {
        System.out.println("Attempting to add show with DTO: " + showEntryDto);

        Show show = new Show();
        show.setDate(showEntryDto.getDate());
        show.setTime(showEntryDto.getTime());

        Movie movie = movieRepository.findById(showEntryDto.getMovieId())
                .orElseThrow(() -> {
                    System.err.println("Movie with ID " + showEntryDto.getMovieId() + " not found.");
                    return new MovieDoesNotExists();
                });

        Theater theater = theatreRepository.findById(showEntryDto.getTheaterId())
                .orElseThrow(() -> {
                    System.err.println("Theater with ID " + showEntryDto.getTheaterId() + " not found.");
                    return new TheaterDoesNotExists();
                });

        show.setMovie(movie);
        show.setTheatre(theater);

        try {
            show = showRepository.save(show);
            System.out.println("Show saved successfully with ID: " + show.getShowId());
            return "Show: " + show.getShowId() + " Added Successfully!";
        } catch (Exception e) {
            System.err.println("Error saving show: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add show due to an internal error.", e);
        }
    }

    @Override
    @Transactional
    public String updateShow(int id, ShowEntryDto showEntryDto)
            throws ShowDoesNotExists, MovieDoesNotExists, TheaterDoesNotExists {
        Show show = showRepository.findById(id).orElseThrow(ShowDoesNotExists::new);

        show.setDate(showEntryDto.getDate());
        show.setTime(showEntryDto.getTime());

        Movie movie = movieRepository.findById(showEntryDto.getMovieId())
                .orElseThrow(MovieDoesNotExists::new);

        Theater theater = theatreRepository.findById(showEntryDto.getTheaterId())
                .orElseThrow(TheaterDoesNotExists::new);

        show.setMovie(movie);
        show.setTheatre(theater);

        try {
            show = showRepository.save(show);
            return "Show: " + show.getShowId() + " is Updated Successfully!";
        } catch (Exception e) {
            System.err.println("Error updating show: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update show due to an internal error.", e);
        }
    }

    @Override
    @Transactional
    public String deleteShow(int id) throws ShowDoesNotExists {
        showRepository.findById(id).orElseThrow(ShowDoesNotExists::new);
        try {
            showRepository.deleteById(id);
            return "Show: " + id + " is Deleted Successfully!";
        } catch (Exception e) {
            System.err.println("Error deleting show: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete show due to an internal error.", e);
        }
    }

    @Override
    @Transactional
    public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) throws ShowDoesNotExists {
        Show show = showRepository.findById(showSeatEntryDto.getShowId())
                .orElseThrow(ShowDoesNotExists::new);

        Theater theater = show.getTheatre();
        List<TheaterSeat> theaterSeats = theaterSeatRepository.findAllByTheaterId(theater.getId());

        if (theaterSeats.isEmpty()) {
            return "No seats available in the theater.";
        }

        List<ShowSeat> showSeatList = showSeatRepository.findByShowId(show.getShowId());
        if (!showSeatList.isEmpty()) {
            return "Show seats have already been associated for this show.";
        }

        for (TheaterSeat theaterSeat : theaterSeats) {
            for (int i = 1; i <= theaterSeat.getSeatCount(); i++) {
                String seatNo = theaterSeat.getRowLabel() + i;
                ShowSeat showSeat = ShowSeat.builder()
                        .show(show)
                        .seatNo(seatNo)
                        .seatType(theaterSeat.getSeatType())
                        .isAvailable(true)
                        .isFoodContains(false)
                        .price(
                                (theaterSeat.getSeatType().equals(SeatType.CLASSIC))
                                        ? showSeatEntryDto.getPriceOfClassicSeat()
                                        : (theaterSeat.getSeatType().equals(SeatType.CLASSICPLUS))
                                                ? showSeatEntryDto.getPriceOfClassicPlusSeat()
                                                : showSeatEntryDto.getPriceOfPremiumSeat())
                        .build();
                showSeatList.add(showSeat);
            }
        }

        showSeatRepository.saveAll(showSeatList);
        return "Show seats have been associated successfully!";
    }

    @Override
    public List<Time> showTimingsOnDate(ShowTimingsDto showTimingsDto) {
        Date date = showTimingsDto.getDate();
        Integer theaterId = showTimingsDto.getTheaterId();
        Integer movieId = showTimingsDto.getMovieId();
        return showRepository.getShowTimingsOnDate(date, theaterId, movieId);
    }

    @Override
    public HashMap<Integer, HashMap<Integer, Time>> getTheaterAndShowTimingsByMovie(Integer movieId, Date date, String city)
            throws MovieDoesNotExists, TheaterDoesNotExists {
        movieRepository.findById(movieId).orElseThrow(MovieDoesNotExists::new);
        List<Theater> theaters = theatreRepository.findByCity(city);
        if (theaters.isEmpty()) {
            throw new TheaterDoesNotExists();
        }

        HashMap<Integer, HashMap<Integer, Time>> theaterShowTimingsMap = new HashMap<>();
        for (Theater theater : theaters) {
            List<Show> shows = showRepository.getTheaterAndShowTimingsByMovie(movieId, date, theater.getId());
            HashMap<Integer, Time> showTimings = new HashMap<>();
            for (Show show : shows) {
                if (show.getTheatre().getId() == theater.getId()) {
                    showTimings.put(show.getShowId(), show.getTime());
                }
            }
            theaterShowTimingsMap.put(theater.getId(), showTimings);
        }

        return theaterShowTimingsMap;
    }

    @Override
    public String movieHavingMostShows() {
        Integer movieId = showRepository.getMostShowsMovieId();
        if (movieId == null) {
            return "No shows are available at the moment.";
        }
        return movieRepository.findById(movieId)
                .map(Movie::getMovieName)
                .orElse("Movie not found, but was associated with the most shows.");
    }

    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @Override
    public Show getShowById(int id) throws ShowDoesNotExists {
        return showRepository.findById(id).orElseThrow(ShowDoesNotExists::new);
    }

    @Override
    public List<Show> getShowByMovieAndTheater(int movieId, int theaterId)
            throws MovieDoesNotExists, TheaterDoesNotExists {
        movieRepository.findById(movieId).orElseThrow(MovieDoesNotExists::new);
        theatreRepository.findById(theaterId).orElseThrow(TheaterDoesNotExists::new);
        return showRepository.getAllShowsOfMovieInTheater(movieId, theaterId);
    }

    @Override
    public List<Show> getAllShowByMovie(int movieId) {
        movieRepository.findById(movieId).orElseThrow(MovieDoesNotExists::new);
        return showRepository.getAllShowsOfMovie(movieId);
    }

    @Override
    public HashMap<String, Integer> getSeatsPrices(Integer showId) throws ShowDoesNotExists {
        Show show = showRepository.findById(showId).orElseThrow(ShowDoesNotExists::new);
        List<SeatPrice> seatPricesList = showSeatRepository.getSeatsPrices(show.getShowId());

        HashMap<String, Integer> seatPrices = new HashMap<>();
        for (SeatPrice seatPrice : seatPricesList) {
            seatPrices.put(seatPrice.getSeatType(), seatPrice.getPrice());
        }

        return seatPrices;
    }
}
