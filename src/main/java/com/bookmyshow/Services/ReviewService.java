package com.bookmyshow.Services;

import com.bookmyshow.Exceptions.ReviewNotFoundException;
import com.bookmyshow.Models.Movie;
import com.bookmyshow.Models.Review;
import com.bookmyshow.Models.User;
import com.bookmyshow.Repositories.MovieRepository;
import com.bookmyshow.Repositories.ReviewRepository;
import com.bookmyshow.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Review addReview(Integer movieId, Integer rating, String comment, UserDetails currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername());
        if (user == null) {
            // This should ideally not happen if the JWT is valid
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));

        Review review = Review.builder()
                .user(user)
                .movie(movie)
                .rating(rating)
                .comment(comment)
                .build();
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByMovie(Integer movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    public Review updateReview(Integer reviewId, Integer rating, String comment, UserDetails currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        // Check if the user is an ADMIN
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // AUTHORIZATION CHECK: User must be the owner of the review or an ADMIN
        if (!review.getUser().getUsername().equals(currentUser.getUsername()) && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this review.");
        }

        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    public void deleteReview(Integer reviewId, UserDetails currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
        
        // Check if the user is an ADMIN
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // AUTHORIZATION CHECK: User must be the owner of the review or an ADMIN
        if (!review.getUser().getUsername().equals(currentUser.getUsername()) && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this review.");
        }

        reviewRepository.deleteById(reviewId);
    }
}