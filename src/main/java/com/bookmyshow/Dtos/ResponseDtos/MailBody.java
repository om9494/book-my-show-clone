package com.bookmyshow.Dtos.ResponseDtos;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
