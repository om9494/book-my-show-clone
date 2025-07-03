package com.bookmyshow.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookmyshow.Dtos.RequestDtos.MovieEntryDto;
import com.bookmyshow.Exceptions.MovieAlreadyPresentWithSameNameAndLanguage;
import com.bookmyshow.Exceptions.MovieDoesNotExists;
import com.bookmyshow.Models.Movie;
import com.bookmyshow.Repositories.MovieRepository;
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

}
