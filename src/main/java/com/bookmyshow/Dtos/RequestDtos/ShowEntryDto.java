package com.bookmyshow.Dtos.RequestDtos;

import java.sql.Date;
import java.sql.Time;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat; // Import this


@Data
public class ShowEntryDto {
    private Time time; // Changed from showStartTime
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")

    private Date date; // Changed from showDate
    private Integer theaterId;
    private Integer movieId;
}