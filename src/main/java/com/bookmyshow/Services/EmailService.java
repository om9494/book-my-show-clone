package com.bookmyshow.Services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.bookmyshow.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.Dtos.ResponseDtos.MailBody;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    /**
     * Sends a formatted HTML email to the user with their ticket details.
     *
     * @param userEmail     The email address of the user.
     * @param bookedTickets A list of TicketResponseDto objects containing details of each booked seat.
     */
    public void sendTicketConfirmationEmail(String userEmail, List<TicketResponseDto> bookedTickets) {
        if (bookedTickets == null || bookedTickets.isEmpty()) {
            System.err.println("No ticket details provided, email not sent.");
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("your-email@gmail.com"); // Make sure this matches the email in application.properties
            helper.setTo(userEmail);
            helper.setSubject("Your Booking is Confirmed! - BookMyShow Clone");

            String htmlContent = buildHtmlEmailContent(bookedTickets);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            System.out.println("Ticket confirmation email sent successfully to " + userEmail);

        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to construct a beautiful HTML body for the ticket email.
     */
    private String buildHtmlEmailContent(List<TicketResponseDto> tickets) {
        TicketResponseDto firstTicket = tickets.get(0);
        String movieName = firstTicket.getMovie().getMovieName();
        String theaterName = firstTicket.getTheater().getName();
        String showDate = firstTicket.getDate().toString();
        String showTime = firstTicket.getTime().toString();

        StringBuilder seatNumbers = new StringBuilder();
        int totalAmount = 0;
        for (TicketResponseDto ticket : tickets) {
            seatNumbers.append(ticket.getSeatNo()).append(", ");
            totalAmount += ticket.getFare();
        }
        String finalSeatNumbers = seatNumbers.substring(0, seatNumbers.length() - 2);

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                ".container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.1); border: 1px solid #ddd; }" +
                ".header { background-color: #e50914; color: #ffffff; padding: 30px; text-align: center; }" +
                ".header h1 { margin: 0; font-size: 28px; }" +
                ".content { padding: 30px; }" +
                ".content h2 { color: #333; font-size: 22px; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 20px; }" +
                ".details-table { width: 100%; border-collapse: collapse; }" +
                ".details-table td { padding: 12px 0; border-bottom: 1px solid #eee; }" +
                ".details-table .label { font-weight: bold; color: #555; width: 40%; }" +
                ".details-table .value { color: #333; }" +
                ".total { text-align: right; margin-top: 20px; font-size: 24px; font-weight: bold; color: #e50914; }" +
                ".footer { background-color: #f4f4f4; color: #888; text-align: center; padding: 20px; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'><h1>Booking Confirmed!</h1></div>" +
                "<div class='content'>" +
                "<h2>Your E-Ticket</h2>" +
                "<table class='details-table'>" +
                "<tr><td class='label'>Movie:</td><td class='value'>" + movieName + "</td></tr>" +
                "<tr><td class='label'>Theater:</td><td class='value'>" + theaterName + "</td></tr>" +
                "<tr><td class='label'>Date & Time:</td><td class='value'>" + showDate + " at " + showTime + "</td></tr>" +
                "<tr><td class='label'>Seats:</td><td class='value'>" + finalSeatNumbers + "</td></tr>" +
                "</table>" +
                "<div class='total'>Total Amount: ₹" + totalAmount + "</div>" +
                "</div>" +
                "<div class='footer'>Thank you for booking with BookMyShow Clone.</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mailBody.to());
        simpleMailMessage.setFrom("yashbisen24@gmail.com");
        simpleMailMessage.setSubject(mailBody.subject());
        simpleMailMessage.setText(mailBody.text());

        mailSender.send(simpleMailMessage);
    }
}
