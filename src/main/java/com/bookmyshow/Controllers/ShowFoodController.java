package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.ShowFoodEntryDto;
import com.bookmyshow.Models.ShowFood;
import com.bookmyshow.Services.ShowFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/show-food")
public class ShowFoodController {

    @Autowired
    private ShowFoodService showFoodService;

    @PostMapping("/add")
    public ResponseEntity<String> addFoodToShow(@RequestBody ShowFoodEntryDto dto) {
        try {
            String result = showFoodService.addFoodToShow(dto);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{foodId}")
    public ResponseEntity<ShowFood> updateFoodItem(@PathVariable Integer foodId, @RequestBody ShowFoodEntryDto dto) {
        try {
            ShowFood updatedFood = showFoodService.updateFoodItem(foodId, dto);
            return ResponseEntity.ok(updatedFood);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{foodId}")
    public ResponseEntity<String> deleteFoodItem(@PathVariable Integer foodId) {
        try {
            String result = showFoodService.deleteFoodItem(foodId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<ShowFood>> getFoodForShow(@PathVariable Integer showId) {
        List<ShowFood> foodList = showFoodService.getFoodForShow(showId);
        return ResponseEntity.ok(foodList);
    }
}