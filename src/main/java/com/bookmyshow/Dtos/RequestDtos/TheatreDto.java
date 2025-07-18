package com.bookmyshow.Dtos.RequestDtos;

import lombok.Data;

@Data
public class TheatreDto {
    private String name;
    private String city;
    private String address;
    private int numberOfScreens;
}

