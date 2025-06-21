package com.bookmyshow.Dtos.RequestDtos;
import java.sql.Date;


import com.bookmyshow.Enums.Genre;
import com.bookmyshow.Enums.Language;

import lombok.Data;

@Data
public class MovieEntryDto {
	private String movieName;
    private Integer duration;
    private Double rating;
    private Date releaseDate;
    private Genre genre;
    private Language language;
    private String imageUrl; // 🎞️ Add this to capture image
     
    
}
