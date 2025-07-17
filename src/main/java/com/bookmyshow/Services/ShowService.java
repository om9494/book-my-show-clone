package com.bookmyshow.Services;

import java.sql.Time;
import java.sql.Date;
import java.util.List;
import java.util.HashMap;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.TheaterDoesNotExists;
import com.bookmyshow.Models.Show;

public interface ShowService {
	public List<Show> getAllShows();
	public Show getShowById(int id) throws ShowDoesNotExists;
	public String addShow(ShowEntryDto showEntryDto) throws MovieDoesNotExists, TheaterDoesNotExists;
	public String updateShow(int id, ShowEntryDto showEntryDto) throws ShowDoesNotExists, MovieDoesNotExists, TheaterDoesNotExists;
	public String deleteShow(int id) throws ShowDoesNotExists;

	public List<Show> getShowByMovieAndTheater(int movieId, int theaterId)
			throws MovieDoesNotExists, TheaterDoesNotExists;
	public List<Show> getAllShowByMovie(int movieId);
	public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) throws ShowDoesNotExists;
	public List<Time> showTimingsOnDate(ShowTimingsDto showTimingsDto);
	public HashMap<Integer, List<Time>> getTheaterAndShowTimingsByMovie(Integer movieId, Date date, String city) throws MovieDoesNotExists, TheaterDoesNotExists;
	public String movieHavingMostShows();
}
