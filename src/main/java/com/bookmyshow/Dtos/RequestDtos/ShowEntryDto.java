package com.bookmyshow.Dtos.RequestDtos;

import java.sql.Date;
import java.sql.Time;

import lombok.Data;

@Data
public class ShowEntryDto {
    private Time time; // Changed from showStartTime
    private Date date; // Changed from showDate
    private Integer theaterId;
    private Integer movieId;
}