package com.bookmyshow.Dtos.RequestDtos;

import java.sql.Date;
import java.sql.Time;

import lombok.Data;

@Data
public class ShowEntryDto {
    private Time showStartTime;
    private Date showDate;
    private Integer theaterId;
    private Integer movieId;
}
