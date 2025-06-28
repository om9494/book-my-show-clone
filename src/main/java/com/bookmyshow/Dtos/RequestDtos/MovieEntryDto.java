package com.bookmyshow.Dtos.RequestDtos;
import java.sql.Date;



import com.bookmyshow.Enums.Genre;
import com.bookmyshow.Enums.Language;

import lombok.Data;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
public class MovieEntryDto {
	 @NotBlank(message = "Movie name is required")
	    private String movieName;

	    @NotNull(message = "Duration is required")
	    @Min(value = 30, message = "Duration should be at least 30 minutes")
	    private Integer duration;

	    @NotNull(message = "Rating is required")
	    @DecimalMin(value = "0.0", message = "Rating must be between 0 and 10")
	    @DecimalMax(value = "10.0", message = "Rating must be between 0 and 10")
	    private Double rating;

	    @NotNull(message = "Release date is required")
	    private Date releaseDate;

	    @NotNull(message = "Genre is required")
	    private Genre genre;

	    @NotNull(message = "Language is required")
	    private Language language;

	    @NotBlank(message = "Image URL is required")
	    private String imageUrl;
     
    
}
