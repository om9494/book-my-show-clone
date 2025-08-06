package com.bookmyshow.Dtos.RequestDtos;

import lombok.Data;

@Data
public class ShowFoodEntryDto {
    private String name;
    private int price;
    private Integer showId;
}