package com.bookmyshow.Dtos.RequestDtos;


import java.sql.Date;
import lombok.Data;

@Data
public class ShowTimingsDto {
    private Date date;
    private Integer theaterId;
    private Integer movieId;
}
