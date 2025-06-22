package com.bookmyshow.Services;

import java.sql.Time;
import java.util.List;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.bookmyshow.Dtos.RequestDtos.ShowTimingsDto;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Exceptions.TheaterDoesNotExists;

public interface ShowService {
	public String addShow(ShowEntryDto showEntryDto) throws MovieDoesNotExists, TheaterDoesNotExists;
	public String updateShow(int id, ShowEntryDto showEntryDto) throws ShowDoesNotExists, MovieDoesNotExists, TheaterDoesNotExists;
	public String deleteShow(int id) throws ShowDoesNotExists;
	public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) throws ShowDoesNotExists;
	public List<Time> showTimingsOnDate(ShowTimingsDto showTimingsDto);
	public String movieHavingMostShows();
}
