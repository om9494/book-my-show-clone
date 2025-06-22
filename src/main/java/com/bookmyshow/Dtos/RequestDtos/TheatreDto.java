package com.bookmyshow.Dtos.RequestDtos;

public class TheatreDto {
    private String name;
    private String city;
    private int numberOfScreens;

    /*
    A DTO is a clean, controlled object used to send/receive data, especially
    across the network or between backend layers — keeping your entities private
    and your APIs neat.
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumberOfScreens() {
        return numberOfScreens;
    }

    public void setNumberOfScreens(int numberOfScreens) {
        this.numberOfScreens = numberOfScreens;
    }
}

