package com.bookmyshow.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmyshow.Models.Movie;

public interface MovieRepository extends JpaRepository<Movie,Integer> {
	Movie findByMovieName(String name);
}
