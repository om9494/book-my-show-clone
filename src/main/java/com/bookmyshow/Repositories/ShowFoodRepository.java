package com.bookmyshow.Repositories;

import com.bookmyshow.Models.ShowFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowFoodRepository extends JpaRepository<ShowFood, Integer> {
    // Find all food items for a given showId
    List<ShowFood> findByShow_ShowId(Integer showId);
}