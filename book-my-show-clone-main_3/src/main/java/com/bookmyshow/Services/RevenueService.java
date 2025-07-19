package com.bookmyshow.Services;

import com.bookmyshow.Models.Revenue;
import com.bookmyshow.Repositories.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    public Revenue addRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }
    public List<Revenue> getRevenueByMovieId(int movieId) {
        return revenueRepository.findByMovieId(movieId);
    }
    public List<Revenue> getAllRevenue() {
        return revenueRepository.findAll();
    }
}
