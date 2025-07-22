package com.bookmyshow.Services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepository movieRepository;

    public String addMovie(MovieEntryDto movieEntryDto) throws MovieAlreadyPresentWithSameNameAndLanguage {
        logger.info("Attempting to add movie with name: {} and language: {}", movieEntryDto.getMovieName(), movieEntryDto.getLanguage());

        Movie existingMovie = movieRepository.findByMovieName(movieEntryDto.getMovieName());
        
        if (existingMovie != null && existingMovie.getLanguage().equals(movieEntryDto.getLanguage())) {
            logger.warn("Movie with name '{}' and language '{}' already exists. Aborting.", movieEntryDto.getMovieName(), movieEntryDto.getLanguage());
            throw new MovieAlreadyPresentWithSameNameAndLanguage();
        }

        Movie movie = MovieTransformer.movieDtoToMovie(movieEntryDto);
        movieRepository.save(movie);
        logger.info("Movie with name '{}' and language '{}' added successfully.", movie.getMovieName(), movie.getLanguage());
        return "The movie has been added successfully";
    }
    
    public Movie getMovieByName(String name) {
        logger.info("Fetching movie with name: {}", name);
        Movie movie = movieRepository.findByMovieName(name);
        if (movie == null) {
            logger.warn("Movie with name '{}' not found.", name);
            throw new MovieDoesNotExists();
        }
        logger.info("Successfully fetched movie with name: {}", name);
        return movie;
    }
    
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public Long totalCollection(Integer movieId) throws MovieDoesNotExists {
        logger.info("Calculating total collection for movie ID: {}", movieId);
        Optional<Movie> movieOpt = movieRepository.findById(movieId);
        if (movieOpt.isEmpty()) {
            logger.warn("Movie with ID {} does not exist. Cannot calculate collection.", movieId);
            throw new MovieDoesNotExists();
        }
        logger.debug("Found movie with ID: {}", movieId);

        List<Show> showListOfMovie = showRepository.getAllShowsOfMovie(movieId);
        
        long amount = 0;
        for (Show show : showListOfMovie) {
            logger.debug("Processing shows for movie ID: {}", show.getShowId());
            for (Ticket ticket : ticketRepository.findByShowId(show.getShowId())) {
                amount += ticket.getTicketPrice();
            }
        }
        logger.info("Total collection for movie ID {} is: {}", movieId, amount);
        return amount;
    }
    
    public List<Movie> getAllMovies() {
        logger.info("Fetching all movies.");
        List<Movie> movies = movieRepository.findAll();
        logger.debug("Found {} movies in the database.", movies.size());
        return movies;
    }
    
    
    public Movie getMovieById(Integer id) {
        logger.info("Fetching movie with ID: {}", id);
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Movie with ID {} not found.", id);
                return new MovieDoesNotExists();
            });
        logger.info("Successfully fetched movie with ID: {}", id);
        return movie;
    }

    public void deleteMovie(Integer id) {
        logger.info("Attempting to delete movie with ID: {}", id);
        if (!movieRepository.existsById(id)) {
            logger.warn("Movie with ID {} not found for deletion.", id);
            throw new MovieDoesNotExists();
        }
        movieRepository.deleteById(id);
        logger.info("Successfully deleted movie with ID: {}", id);
    }

    public void updateMovie(Integer id, MovieEntryDto dto) {
        logger.info("Attempting to update movie with ID: {}", id);
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Movie with ID {} not found for update.", id);
                return new MovieDoesNotExists();
            });

        logger.debug("Updating movie with old name '{}' and new name '{}'", movie.getMovieName(), dto.getMovieName());
        // Update fields
        movie.setMovieName(dto.getMovieName());
        movie.setDuration(dto.getDuration());
        movie.setRating(dto.getRating());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setGenre(dto.getGenre());
        movie.setLanguage(dto.getLanguage());
        movie.setImageUrl(dto.getImageUrl());

        movieRepository.save(movie);
        logger.info("Successfully updated movie with ID: {}", id);
    }
    
    public List<Movie> searchMoviesByName(String name) {
        logger.info("Searching for movies with name containing: '{}'", name);
        List<Movie> movies = movieRepository.findByMovieNameContainingIgnoreCase(name);
        logger.debug("Found {} movies matching the search term: '{}'", movies.size(), name);
        return movies;
    }

}