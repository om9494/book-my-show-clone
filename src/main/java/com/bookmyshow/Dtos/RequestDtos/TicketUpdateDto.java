package com.bookmyshow.Dtos.RequestDtos;

import lombok.Data;

/**
 * DTO for carrying data required to update a ticket.
 */
@Data
public class TicketUpdateDto {

    /**
     * The ID of the ticket to be updated.
     */
    private Integer originalTicketId;

    /**
     * The ID of the new show. Can be the same as the original show
     * if only the seat is changing.
     */
    private Integer newShowId;

    /**
     * The new seat number the user wants to book (e.g., "C4", "D1").
     */
    private String newSeatNo;

    /**
     * The ID of the user requesting the update, used for validation.
     */
    private Integer userId;
}