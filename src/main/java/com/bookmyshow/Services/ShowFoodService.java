package com.bookmyshow.Services;

import com.bookmyshow.Dtos.RequestDtos.ShowFoodEntryDto;
import com.bookmyshow.Models.ShowFood;
import java.util.List;

public interface ShowFoodService {
    String addFoodToShow(ShowFoodEntryDto showFoodEntryDto) throws Exception;
    ShowFood updateFoodItem(Integer foodId, ShowFoodEntryDto showFoodEntryDto) throws Exception;
    String deleteFoodItem(Integer foodId) throws Exception;
    List<ShowFood> getFoodForShow(Integer showId);
}