package com.bookmyshow.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmyshow.Models.Show;

public interface ShowRepository extends JpaRepository<Show, Integer> {
	
}
