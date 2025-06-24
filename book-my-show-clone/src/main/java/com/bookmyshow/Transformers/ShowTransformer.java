package com.bookmyshow.Transformers;

import com.bookmyshow.Dtos.RequestDtos.ShowEntryDto;
import com.bookmyshow.Models.Show;

public class ShowTransformer {
	
	public static Show showDtoToShow(ShowEntryDto showEntryDto) {
		Show show = Show.builder()
				.time(showEntryDto.getShowStartTime())
				.date(showEntryDto.getShowDate())
				.build();
		
		return show;
	}
}
