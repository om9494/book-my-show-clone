package com.bookmyshow.Services;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

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
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.ShowSeat;
import com.bookmyshow.Models.Theater;
import com.bookmyshow.Models.TheaterSeat;
import com.bookmyshow.Repositories.MovieRepository;
import com.bookmyshow.Repositories.ShowRepository;
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

		Theater theater = show.getTheatre();

		List<TheaterSeat> theaterSeats = theater.getTheaterSeatList();

		List<ShowSeat> showSeatList = show.getShowSeatList();

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

}
