package com.bookmyshow.Controllers;

import com.bookmyshow.Models.Revenue;
import com.bookmyshow.Services.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revenue")
public class RevenueController {
    @Autowired
    private RevenueService revenueService;
    @PostMapping
    public ResponseEntity<Revenue> addRevenue(@RequestBody Revenue revenue) {
        return ResponseEntity.ok(revenueService.addRevenue(revenue));
    }
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Revenue>> getRevenueByMovie(@PathVariable int movieId) {
        return ResponseEntity.ok(revenueService.getRevenueByMovieId(movieId));
    }
    @GetMapping
    public ResponseEntity<List<Revenue>> getAllRevenue() {
        return ResponseEntity.ok(revenueService.getAllRevenue());
    }
}
