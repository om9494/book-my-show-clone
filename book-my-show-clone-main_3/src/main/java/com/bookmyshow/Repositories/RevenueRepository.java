package com.bookmyshow.Repositories;

import com.bookmyshow.Models.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Integer> {
    List<Revenue> findByMovieId(int movieId);
}
