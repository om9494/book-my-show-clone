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
import com.bookmyshow.Repositories.MovieRepository;
import com.bookmyshow.Repositories.ShowRepository;
import com.bookmyshow.Repositories.TheatreRepository;

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
		return null;
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
