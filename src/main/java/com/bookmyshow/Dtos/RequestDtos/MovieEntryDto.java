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
     
    public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public MovieEntryDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
