package com.bookmyshow.Services;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.TheaterDoesNotExists;
import com.bookmyshow.Models.Movie;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.Theatre;
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
		Show show = ShowTransformer.showDtoToShow(showEntryDto);

		if (!movieRepository.existsById(showEntryDto.getMovieId())) {
			throw new MovieDoesNotExists();
		}
		if (!theatreRepository.existsById(showEntryDto.getTheaterId())) {
			throw new TheaterDoesNotExists();
		}

		Theatre theater = theatreRepository.findById(showEntryDto.getTheaterId()).get();
		Movie movie = movieRepository.findById(showEntryDto.getMovieId()).get();

		show.setMovie(movie);
		show.setTheatre(theater);
		show = showRepository.save(show);

		movie.getShows().add(show);
		theater.getShowList().add(show);

		movieRepository.save(movie);
		theatreRepository.save(theater);

		return "Show has been added Successfully";
	}

	@Override
	public String updateShow(int id, ShowEntryDto showEntryDto)
			throws ShowDoesNotExists, MovieDoesNotExists, TheaterDoesNotExists {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteShow(int id) throws ShowDoesNotExists {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) throws ShowDoesNotExists {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Time> showTimingsOnDate(ShowTimingsDto showTimingsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String movieHavingMostShows() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
