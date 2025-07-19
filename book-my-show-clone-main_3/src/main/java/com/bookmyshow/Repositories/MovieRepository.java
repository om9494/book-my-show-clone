package com.bookmyshow.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bookmyshow.Models.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Integer> {
	Movie findByMovieName(String name);
	List<Movie> findByMovieNameContainingIgnoreCase(String movieName);
}
