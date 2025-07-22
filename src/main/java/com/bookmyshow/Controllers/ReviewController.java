package com.bookmyshow.Controllers;

import com.bookmyshow.Models.Review;
import com.bookmyshow.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@RequestBody Map<String, String> request, Authentication authentication) {
        // Get the currently authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        Integer movieId = Integer.parseInt(request.get("movieId"));
        Integer rating = Integer.parseInt(request.get("rating"));
        String comment = request.get("comment");

        Review review = reviewService.addReview(movieId, rating, comment, userDetails);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Review>> getReviewsByMovie(@PathVariable Integer movieId) {
        List<Review> reviews = reviewService.getReviewsByMovie(movieId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Integer reviewId, @RequestBody Map<String, String> request, Authentication authentication) {
        // Get the currently authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Integer rating = Integer.parseInt(request.get("rating"));
        String comment = request.get("comment");

        Review updatedReview = reviewService.updateReview(reviewId, rating, comment, userDetails);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer reviewId, Authentication authentication) {
        // Get the currently authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        reviewService.deleteReview(reviewId, userDetails);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
    }
}