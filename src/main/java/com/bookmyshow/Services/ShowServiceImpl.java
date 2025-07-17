package com.bookmyshow.Services;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for transactional annotation

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Enums.SeatType;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.TheaterDoesNotExists;
import com.bookmyshow.Models.Movie;
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
    @Transactional // Ensures atomicity for database operations
    public String addShow(ShowEntryDto showEntryDto) throws MovieDoesNotExists, TheaterDoesNotExists {
        System.out.println("Attempting to add show with DTO: " + showEntryDto);

        Show show = new Show(); // Create a new Show object
        
        // Manually set the date and time from the DTO
        // These fields are now correctly named 'date' and 'time' in ShowEntryDto
        show.setDate(showEntryDto.getDate());
        show.setTime(showEntryDto.getTime());
        
        System.out.println("Fetching movie with ID: " + showEntryDto.getMovieId());
        Movie movie = movieRepository.findById(showEntryDto.getMovieId())
                .orElseThrow(() -> {
                    System.err.println("Movie with ID " + showEntryDto.getMovieId() + " not found.");
                    return new MovieDoesNotExists();
                });
        
        System.out.println("Fetching theater with ID: " + showEntryDto.getTheaterId());
        Theater theater = theatreRepository.findById(showEntryDto.getTheaterId())
                .orElseThrow(() -> {
                    System.err.println("Theater with ID " + showEntryDto.getTheaterId() + " not found.");
                    return new TheaterDoesNotExists();
                });
        
        show.setMovie(movie);
        show.setTheatre(theater);

        try {
            System.out.println("Saving show to repository...");
            show = showRepository.save(show); // This is the critical line
            System.out.println("Show saved successfully with ID: " + show.getShowId());
            return "Show: " + show.getShowId() + " Added Successfully!";
        } catch (Exception e) {
            // Log the full stack trace of the exception
            System.err.println("Error saving show: " + e.getMessage());
            e.printStackTrace(); // This will print the full stack trace to your console
            throw new RuntimeException("Failed to add show due to an internal error. Please check server logs for details.", e);
        }
    }

	@Override
    @Transactional
    public String updateShow(int id, ShowEntryDto showEntryDto)
            throws ShowDoesNotExists, MovieDoesNotExists, TheaterDoesNotExists {
        Show show = showRepository.findById(id).orElseThrow(ShowDoesNotExists::new);

        // Manually update the date and time from the DTO
        show.setDate(showEntryDto.getDate()); // Now correctly mapped
        show.setTime(showEntryDto.getTime()); // Now correctly mapped

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
            throw new RuntimeException("Failed to update show due to an internal error. Please check server logs for details.", e);
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
            throw new RuntimeException("Failed to delete show due to an internal error. Please check server logs for details.", e);
        }
	}

	@Override
    @Transactional
	public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) throws ShowDoesNotExists {
		Show show = showRepository.findById(showSeatEntryDto.getShowId())
				.orElseThrow(ShowDoesNotExists::new);

		System.out.println("Show " + show);

		Theater theater = show.getTheatre();

		System.out.println("Theater " + theater);

		List<TheaterSeat> theaterSeats = theaterSeatRepository.findAllByTheaterId(theater.getId());
		if (theaterSeats.isEmpty()) {
			return "No seats available in the theater.";
		}

		List<ShowSeat> showSeatList = showSeatRepository.findByShowId(show.getShowId());
		if (!showSeatList.isEmpty()) {
			return "Show seats have already been associated for this show.";
		}
		// It's generally not recommended to clear and re-add if you are checking for emptiness.
		// If the intent is to ensure a fresh list, this is fine, but if it's for updates,
		// you might need a different approach.
		// showSeatList.clear(); // This line is effectively redundant if the list is already empty based on the above check.
		
		System.out.println("Theater Seats: " + theaterSeats);
		for (TheaterSeat theaterSeat : theaterSeats) {
			ShowSeat showSeat = ShowSeat.builder()
					.show(show)
					.seatNo(theaterSeat.getSeatNo())
					.seatType(theaterSeat.getSeatType())
					.isAvailable(Boolean.TRUE)
					.isFoodContains(Boolean.FALSE)
					.price(
							(theaterSeat.getSeatType().equals(SeatType.CLASSIC))
									? showSeatEntryDto.getPriceOfClassicSeat()
									: showSeatEntryDto.getPriceOfPremiumSeat())
					.build();
			showSeatList.add(showSeat);
		}
		// Save all show seats in the repository
		showSeatRepository.saveAll(showSeatList);
		// Associate the show seats with the show (already done by setting show in ShowSeat,
		// and show is already managed by JPA if it was fetched. This save might be redundant
		// unless you are updating fields on the 'show' object itself.)
		// showRepository.save(show); // This line might be redundant if no changes were made to the 'show' object itself.
		
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
		List<Show> shows = showRepository.findAll();
		return shows;
	}

	@Override
	public Show getShowById(int id) throws ShowDoesNotExists {
		Show show = showRepository.findById(id).orElseThrow(ShowDoesNotExists::new);
		return show;
	}

	@Override
	public List<Show> getShowByMovieAndTheater(int movieId, int theaterId)
			throws MovieDoesNotExists, TheaterDoesNotExists {
		movieRepository.findById(movieId).orElseThrow(MovieDoesNotExists::new);
		theatreRepository.findById(theaterId).orElseThrow(TheaterDoesNotExists::new);
		List<Show> shows = showRepository.getAllShowsOfMovieInTheater(movieId, theaterId);
		return shows;
	}

	@Override
	public List<Show> getAllShowByMovie(int movieId) {
		movieRepository.findById(movieId).orElseThrow(MovieDoesNotExists::new);
		List<Show> shows = showRepository.getAllShowsOfMovie(movieId);
		return shows;
	}
}
