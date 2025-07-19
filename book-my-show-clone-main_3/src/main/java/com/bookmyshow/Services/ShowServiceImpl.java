package com.bookmyshow.Services;

import java.sql.Date;

import java.sql.Time;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.bookmyshow.Transformers.ShowTransformer;

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
	public String addShow(ShowEntryDto showEntryDto) throws MovieDoesNotExists, TheaterDoesNotExists {
		// TODO Auto-generated method stub
		Show show = ShowTransformer.showDtoToShow(showEntryDto);		
		Movie movie = movieRepository.findById(showEntryDto.getMovieId())
				.orElseThrow(MovieDoesNotExists::new);
		
		Theater theater = theatreRepository.findById(showEntryDto.getTheaterId())
				.orElseThrow(TheaterDoesNotExists::new);
		
		show.setMovie(movie);
		show.setTheatre(theater);

		show = showRepository.save(show);

		return "Show: " + show.getShowId() + " Added Successfully!";
	}

	@Override
	public String updateShow(int id, ShowEntryDto showEntryDto)
			throws ShowDoesNotExists, MovieDoesNotExists, TheaterDoesNotExists {
		// TODO Auto-generated method stub
		Show show = showRepository.findById(id).orElseThrow(ShowDoesNotExists::new);

		show.setDate(showEntryDto.getShowDate());
		show.setTime(showEntryDto.getShowStartTime());

		Movie movie = movieRepository.findById(showEntryDto.getMovieId())
				.orElseThrow(MovieDoesNotExists::new);
		
		Theater theater = theatreRepository.findById(showEntryDto.getTheaterId())
				.orElseThrow(TheaterDoesNotExists::new);


		show.setMovie(movie);
		show.setTheatre(theater);

		show = showRepository.save(show);

		return "Show: " + show.getShowId() + " is Updated Successfully!";
	}

	@Override
	public String deleteShow(int id) throws ShowDoesNotExists {
		// TODO Auto-generated method stub
		showRepository.findById(id).orElseThrow(ShowDoesNotExists::new);
		showRepository.deleteById(id);
		return "Show: " + id + " is Deleted Successfully!";
	}

	@Override
	public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) throws ShowDoesNotExists {
		// TODO Auto-generated method stub
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
		showSeatList.clear(); // Clear existing seats if any
		System.out.println("Theater Seats: " + theaterSeats);
		for (TheaterSeat theaterSeat : theaterSeats) {
			for(int i=1; i<=theaterSeat.getSeatCount(); i++){
				String seatNo = theaterSeat.getRowLabel() + i;
				ShowSeat showSeat = ShowSeat.builder()
						.show(show)
						.seatNo(seatNo)
						.seatType(theaterSeat.getSeatType())
						.isAvailable(Boolean.TRUE)
						.isFoodContains(Boolean.FALSE)
						.price(
								(theaterSeat.getSeatType().equals(SeatType.CLASSIC))
										? showSeatEntryDto.getPriceOfClassicSeat()
										: (theaterSeat.getSeatType().equals(SeatType.CLASSICPLUS))
												? showSeatEntryDto.getPriceOfClassicPlusSeat()
												: showSeatEntryDto.getPriceOfPremiumSeat())
						.build();
				// System.out.println("Show Seat: " + showSeat);
				showSeatList.add(showSeat);
			}
		}
		// Save all show seats in the repository
		showSeatRepository.saveAll(showSeatList);
		// Associate the show seats with the show
		showRepository.save(show);
		
		return "Show seats have been associated successfully!";
	}

	@Override
	public List<Time> showTimingsOnDate(ShowTimingsDto showTimingsDto) {
		// TODO Auto-generated method stub
		Date date = showTimingsDto.getDate();
		Integer theaterId = showTimingsDto.getTheaterId();
		Integer movieId = showTimingsDto.getMovieId();
		return showRepository.getShowTimingsOnDate(date, theaterId, movieId);
	}

	@Override
	public HashMap<Integer, HashMap<Integer, Time>> getTheaterAndShowTimingsByMovie(Integer movieId, Date date, String city) throws MovieDoesNotExists, TheaterDoesNotExists {
			System.out.println("Internal Fetching theater and show timings for movie ID: " + movieId + " on date: " + date);
			movieRepository.findById(movieId).orElseThrow(MovieDoesNotExists::new);
			List<Theater> theaters = theatreRepository.findByCity(city);
			if (theaters.isEmpty()) {
				throw new TheaterDoesNotExists();
			}
			HashMap<Integer, HashMap<Integer, Time>> theaterShowTimingsMap = new HashMap<>();
			for (Theater theater : theaters) {
				List<Show> show = showRepository.getTheaterAndShowTimingsByMovie(movieId, date, theater.getId());
				HashMap<Integer, Time> showTimings = new HashMap<>();
				for (Show s : show) {
					if(s.getTheatre().getId() == theater.getId()) showTimings.put(s.getShowId(), s.getTime());
				}
				theaterShowTimingsMap.put(theater.getId(), showTimings);
			}
			return theaterShowTimingsMap;
		}

	@Override
	public String movieHavingMostShows() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		List<Show> shows = showRepository.findAll();
		return shows;
	}

	@Override
	public Show getShowById(int id) throws ShowDoesNotExists {
		// TODO Auto-generated method stub
		Show show = showRepository.findById(id).orElseThrow(ShowDoesNotExists::new);
		return show;
	}

	@Override
	public List<Show> getShowByMovieAndTheater(int movieId, int theaterId)
			throws MovieDoesNotExists, TheaterDoesNotExists {
		// TODO Auto-generated method stub
		movieRepository.findById(movieId).orElseThrow(MovieDoesNotExists::new);
		theatreRepository.findById(theaterId).orElseThrow(TheaterDoesNotExists::new);
		List<Show> shows = showRepository.getAllShowsOfMovieInTheater(movieId, theaterId);
		return shows;
	}

	@Override
	public List<Show> getAllShowByMovie(int movieId) {
		// TODO Auto-generated method stub
		movieRepository.findById(movieId).orElseThrow(MovieDoesNotExists::new);
		List<Show> shows = showRepository.getAllShowsOfMovie(movieId);
		return shows;
	}

	@Override
	public HashMap<String, Integer> getSeatsPrices(Integer showId) throws ShowDoesNotExists {
		Show show = showRepository.findById(showId).orElseThrow(ShowDoesNotExists::new);
		List<SeatPrice> showSeats = showSeatRepository.getSeatsPrices(show.getShowId());
		HashMap<String, Integer> seatPrices = new HashMap<>();
		for (SeatPrice seatPrice : showSeats) {
			seatPrices.put(seatPrice.getSeatType(), seatPrice.getPrice());
		}
		return seatPrices;
	}	

}
