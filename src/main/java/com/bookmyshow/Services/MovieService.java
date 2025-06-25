package com.bookmyshow.Services;

import java.util.List;
import java.util.ArrayList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookmyshow.Dtos.RequestDtos.MovieEntryDto;
import com.bookmyshow.Exceptions.MovieAlreadyPresentWithSameNameAndLanguage;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Models.Movie;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.Ticket;
import com.bookmyshow.Repositories.MovieRepository;
import com.bookmyshow.Repositories.ShowRepository;
import com.bookmyshow.Repositories.TicketRepository;
import com.bookmyshow.Transformers.MovieTransformer;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public String addMovie(MovieEntryDto movieEntryDto) throws MovieAlreadyPresentWithSameNameAndLanguage {
        Movie existingMovie = movieRepository.findByMovieName(movieEntryDto.getMovieName());
        
        if (existingMovie != null && existingMovie.getLanguage().equals(movieEntryDto.getLanguage())) {
            throw new MovieAlreadyPresentWithSameNameAndLanguage();
        }

        Movie movie = MovieTransformer.movieDtoToMovie(movieEntryDto);
        movieRepository.save(movie);
        return "The movie has been added successfully";
    }
    
    public Movie getMovieByName(String name) {
        Movie movie = movieRepository.findByMovieName(name);
        if (movie == null) throw new MovieDoesNotExists();
        return movie;
    }
    
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public Long totalCollection(Integer movieId) throws MovieDoesNotExists {
        Optional<Movie> movieOpt = movieRepository.findById(movieId);
        if (movieOpt.isEmpty()) {
            throw new MovieDoesNotExists();
        }

        List<Show> showListOfMovie = showRepository.getAllShowsOfMovie(movieId);
        
        long amount = 0;
        for (Show show : showListOfMovie) {
            for (Ticket ticket : ticketRepository.findByShowId(show.getShowId())) {
                amount += ticket.getTicketPrice(); // Assuming this returns int or long
            }
        }

        return amount;
    }
    
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    
    
    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(MovieDoesNotExists::new);
    }

    public void deleteMovie(Integer id) {
        if (!movieRepository.existsById(id)) {
            throw new MovieDoesNotExists();
        }
        movieRepository.deleteById(id);
    }

    public void updateMovie(Integer id, MovieEntryDto dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(MovieDoesNotExists::new);

        // Update fields
        movie.setMovieName(dto.getMovieName());
        movie.setDuration(dto.getDuration());
        movie.setRating(dto.getRating());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setGenre(dto.getGenre());
        movie.setLanguage(dto.getLanguage());
        movie.setImageUrl(dto.getImageUrl());

        movieRepository.save(movie);
    }



}
