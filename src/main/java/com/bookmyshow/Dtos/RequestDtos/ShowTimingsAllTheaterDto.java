package com.bookmyshow.Dtos.RequestDtos;

import java.sql.Date;
import lombok.Data;

@Data
public class ShowTimingsAllTheaterDto {
    private Integer movieId;
    private Date date;
    private String city;
}
