package com.bookmyshow.Transformers;

import com.bookmyshow.Dtos.RequestDtos.MovieEntryDto;
import com.bookmyshow.Models.Movie;

public class MovieTransformer {
	public static Movie movieDtoToMovie(MovieEntryDto dto) {
        return Movie.builder()
            .movieName(dto.getMovieName())
            .duration(dto.getDuration())
            .rating(dto.getRating())
            .releaseDate(dto.getReleaseDate())
            .genre(dto.getGenre())
            .language(dto.getLanguage())
            .imageUrl(dto.getImageUrl()) // ✅ New line
            .build();
    }
}
