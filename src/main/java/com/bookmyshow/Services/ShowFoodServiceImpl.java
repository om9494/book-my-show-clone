package com.bookmyshow.Services;

import com.bookmyshow.Dtos.RequestDtos.ShowFoodEntryDto;
import com.bookmyshow.Exceptions.ShowDoesNotExists;
import com.bookmyshow.Models.Show;
import com.bookmyshow.Models.ShowFood;
import com.bookmyshow.Repositories.ShowFoodRepository;
import com.bookmyshow.Repositories.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShowFoodServiceImpl implements ShowFoodService {

    @Autowired
    private ShowFoodRepository showFoodRepository;

    @Autowired
    private ShowRepository showRepository;

    @Override
    public String addFoodToShow(ShowFoodEntryDto dto) throws Exception {
        Show show = showRepository.findById(dto.getShowId())
            .orElseThrow(() -> new ShowDoesNotExists());
        
        ShowFood showFood = ShowFood.builder()
            .name(dto.getName())
            .price(dto.getPrice())
            .show(show)
            .build();
            
        showFoodRepository.save(showFood);
        return "Food item added to the show successfully.";
    }

    @Override
    public ShowFood updateFoodItem(Integer foodId, ShowFoodEntryDto dto) throws Exception {
        ShowFood foodItem = showFoodRepository.findById(foodId)
            .orElseThrow(() -> new Exception("Food item not found"));

        foodItem.setName(dto.getName());
        foodItem.setPrice(dto.getPrice());
        
        return showFoodRepository.save(foodItem);
    }

    @Override
    public String deleteFoodItem(Integer foodId) throws Exception {
        if (!showFoodRepository.existsById(foodId)) {
            throw new Exception("Food item not found");
        }
        showFoodRepository.deleteById(foodId);
        return "Food item deleted successfully.";
    }

    @Override
    public List<ShowFood> getFoodForShow(Integer showId) {
        return showFoodRepository.findByShow_ShowId(showId);
    }
}