package com.bookmyshow.Services;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ShowServiceImpl.class);

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
        logger.info("Attempting to add show with DTO: {}", showEntryDto);

        Show show = new Show();
        show.setDate(showEntryDto.getDate());
        show.setTime(showEntryDto.getTime());

        Movie movie = movieRepository.findById(showEntryDto.getMovieId())
                .orElseThrow(() -> {
                    logger.error("Movie with ID {} not found.", showEntryDto.getMovieId());
                    return new MovieDoesNotExists();
                });

        Theater theater = theatreRepository.findById(showEntryDto.getTheaterId())
                .orElseThrow(() -> {
                    logger.error("Theater with ID {} not found.", showEntryDto.getTheaterId());
                    return new TheaterDoesNotExists();
                });

        show.setMovie(movie);
        show.setTheatre(theater);

        try {
            show = showRepository.save(show);
            logger.info("Show saved successfully with ID: {}", show.getShowId());
            return "Show: " + show.getShowId() + " Added Successfully!";
        } catch (Exception e) {
            logger.error("Error saving show: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add show due to an internal error.", e);
        }
    }

    @Override
    @Transactional
    public String updateShow(int id, ShowEntryDto showEntryDto)
            throws ShowDoesNotExists, MovieDoesNotExists, TheaterDoesNotExists {
        logger.info("Attempting to update show with ID {} using DTO: {}", id, showEntryDto);
        Show show = showRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Show with ID {} not found for update.", id);
                    return new ShowDoesNotExists();
                });

        show.setDate(showEntryDto.getDate());
        show.setTime(showEntryDto.getTime());

        Movie movie = movieRepository.findById(showEntryDto.getMovieId())
                .orElseThrow(() -> {
                    logger.error("Movie with ID {} not found for show update.", showEntryDto.getMovieId());
                    return new MovieDoesNotExists();
                });

        Theater theater = theatreRepository.findById(showEntryDto.getTheaterId())
                .orElseThrow(() -> {
                    logger.error("Theater with ID {} not found for show update.", showEntryDto.getTheaterId());
                    return new TheaterDoesNotExists();
                });

        show.setMovie(movie);
        show.setTheatre(theater);

        try {
            show = showRepository.save(show);
            logger.info("Show with ID {} updated successfully.", show.getShowId());
            return "Show: " + show.getShowId() + " is Updated Successfully!";
        } catch (Exception e) {
            logger.error("Error updating show with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update show due to an internal error.", e);
        }
    }

    @Override
    @Transactional
    public String deleteShow(int id) throws ShowDoesNotExists {
        logger.info("Attempting to delete show with ID: {}", id);
        showRepository.findById(id).orElseThrow(() -> {
            logger.error("Show with ID {} not found for deletion.", id);
            return new ShowDoesNotExists();
        });
        try {
            showRepository.deleteById(id);
            logger.info("Show with ID {} deleted successfully.", id);
            return "Show: " + id + " is Deleted Successfully!";
        } catch (Exception e) {
            logger.error("Error deleting show with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete show due to an internal error.", e);
        }
    }

    @Override
    @Transactional
    public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) throws ShowDoesNotExists {
        logger.info("Attempting to associate seats for show ID: {}", showSeatEntryDto.getShowId());
        Show show = showRepository.findById(showSeatEntryDto.getShowId())
                .orElseThrow(() -> {
                    logger.error("Show with ID {} not found for seat association.", showSeatEntryDto.getShowId());
                    return new ShowDoesNotExists();
                });

        Theater theater = show.getTheatre();
        List<TheaterSeat> theaterSeats = theaterSeatRepository.findAllByTheaterId(theater.getId());

        if (theaterSeats.isEmpty()) {
            logger.warn("No seats available in theater ID {} for show ID {}. Aborting seat association.", theater.getId(), show.getShowId());
            return "No seats available in the theater.";
        }

        List<ShowSeat> showSeatList = showSeatRepository.findByShowId(show.getShowId());
        if (!showSeatList.isEmpty()) {
            logger.warn("Show seats already associated for show ID {}. Aborting association.", show.getShowId());
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
        logger.info("Successfully associated {} seats for show ID {}.", showSeatList.size(), show.getShowId());
        return "Show seats have been associated successfully!";
    }

    @Override
    public List<Time> showTimingsOnDate(ShowTimingsDto showTimingsDto) {
        logger.info("Fetching show timings for movie ID: {}, theater ID: {}, and date: {}", showTimingsDto.getMovieId(), showTimingsDto.getTheaterId(), showTimingsDto.getDate());
        Date date = showTimingsDto.getDate();
        Integer theaterId = showTimingsDto.getTheaterId();
        Integer movieId = showTimingsDto.getMovieId();
        List<Time> timings = showRepository.getShowTimingsOnDate(date, theaterId, movieId);
        logger.debug("Found {} timings for the specified criteria.", timings.size());
        return timings;
    }

    @Override
    public HashMap<Integer, HashMap<Integer, Time>> getTheaterAndShowTimingsByMovie(Integer movieId, Date date, String city)
            throws MovieDoesNotExists, TheaterDoesNotExists {
        logger.info("Fetching theater and show timings for movie ID: {} on date: {} in city: {}", movieId, date, city);
        movieRepository.findById(movieId)
                .orElseThrow(() -> {
                    logger.error("Movie with ID {} not found.", movieId);
                    return new MovieDoesNotExists();
                });
        List<Theater> theaters = theatreRepository.findByCity(city);
        if (theaters.isEmpty()) {
            logger.error("No theaters found in the specified city: {}", city);
            throw new TheaterDoesNotExists();
        }
        logger.debug("Found {} theaters in city {}.", theaters.size(), city);

        HashMap<Integer, HashMap<Integer, Time>> theaterShowTimingsMap = new HashMap<>();
        for (Theater theater : theaters) {
            List<Show> shows = showRepository.getTheaterAndShowTimingsByMovie(movieId, date, theater.getId());
            HashMap<Integer, Time> showTimings = new HashMap<>();
            for (Show show : shows) {
                if (show.getTheatre().getId() == theater.getId()) {
                    showTimings.put(show.getShowId(), show.getTime());
                }
            }
            if (!showTimings.isEmpty()) {
                logger.debug("Found {} shows for theater ID {} on the given date.", showTimings.size(), theater.getId());
                theaterShowTimingsMap.put(theater.getId(), showTimings);
            } else {
                logger.debug("No shows found for theater ID {} on the given date.", theater.getId());
            }
        }

        logger.info("Finished fetching theater and show timings. Total theaters with shows: {}", theaterShowTimingsMap.size());
        return theaterShowTimingsMap;
    }

    @Override
    public String movieHavingMostShows() {
        logger.info("Fetching movie with the most shows.");
        Integer movieId = showRepository.getMostShowsMovieId();
        if (movieId == null) {
            logger.info("No shows are available at the moment.");
            return "No shows are available at the moment.";
        }
        String movieName = movieRepository.findById(movieId)
                .map(Movie::getMovieName)
                .orElseGet(() -> {
                    logger.warn("Movie with ID {} not found, but was associated with the most shows.", movieId);
                    return "Movie not found, but was associated with the most shows.";
                });
        logger.info("Movie with most shows is: {}", movieName);
        return movieName;
    }

    @Override
    public List<Show> getAllShows() {
        logger.info("Fetching all recent shows.");
        List<Show> shows = showRepository.findAll();
        logger.debug("Found {} shows.", shows.size());
        return shows;
    }

    @Override
    public Show getShowById(int id) throws ShowDoesNotExists {
        logger.info("Fetching show with ID: {}", id);
        return showRepository.findById(id).orElseThrow(() -> {
            logger.error("Show with ID {} not found.", id);
            return new ShowDoesNotExists();
        });
    }

    @Override
    public List<Show> getShowByMovieAndTheater(int movieId, int theaterId)
            throws MovieDoesNotExists, TheaterDoesNotExists {
        logger.info("Fetching shows for movie ID: {} and theater ID: {}", movieId, theaterId);
        movieRepository.findById(movieId).orElseThrow(() -> {
            logger.error("Movie with ID {} not found.", movieId);
            return new MovieDoesNotExists();
        });
        theatreRepository.findById(theaterId).orElseThrow(() -> {
            logger.error("Theater with ID {} not found.", theaterId);
            return new TheaterDoesNotExists();
        });
        Time currentTime = new Time(System.currentTimeMillis());
        Date currentDate = new Date(System.currentTimeMillis());
        List<Show> shows = showRepository.getAllShowsOfMovieInTheater(movieId, theaterId);
        for(Show show : shows){
            if (show.getDate().before(currentDate) || (show.getDate().equals(currentDate) && show.getTime().before(currentTime))){
                shows.remove(show);
            }
        }
        logger.debug("Found {} shows for movie ID {} and theater ID {}.", shows.size(), movieId, theaterId);
        return shows;
    }

    @Override
    public List<Show> getAllShowByMovie(int movieId) {
        logger.info("Fetching all shows for movie ID: {}", movieId);
        movieRepository.findById(movieId).orElseThrow(() -> {
            logger.error("Movie with ID {} not found.", movieId);
            return new MovieDoesNotExists();
        });
        List<Show> shows = showRepository.getAllShowsOfMovie(movieId);
        logger.debug("Found {} shows for movie ID {}.", shows.size(), movieId);
        return shows;
    }

    @Override
    public HashMap<String, Integer> getSeatsPrices(Integer showId) throws ShowDoesNotExists {
        logger.info("Fetching seat prices for show ID: {}", showId);
        Show show = showRepository.findById(showId).orElseThrow(() -> {
            logger.error("Show with ID {} not found.", showId);
            return new ShowDoesNotExists();
        });
        List<SeatPrice> seatPricesList = showSeatRepository.getSeatsPrices(show.getShowId());

        HashMap<String, Integer> seatPrices = new HashMap<>();
        for (SeatPrice seatPrice : seatPricesList) {
            seatPrices.put(seatPrice.getSeatType(), seatPrice.getPrice());
        }
        logger.debug("Found prices for {} seat types for show ID {}.", seatPrices.size(), showId);

        return seatPrices;
    }
}