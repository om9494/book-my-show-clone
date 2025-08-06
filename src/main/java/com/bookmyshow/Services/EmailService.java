package com.bookmyshow.Services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.bookmyshow.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.Dtos.ResponseDtos.MailBody;
import com.bookmyshow.Models.ShowFood;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param userName      The name of the user.
     * @param bookedTickets A list of TicketResponseDto objects containing details of each booked seat.
     */
    public void sendTicketConfirmationEmail(String userEmail, String userName, List<TicketResponseDto> bookedTickets) {
        if (bookedTickets == null || bookedTickets.isEmpty()) {
            System.err.println("No ticket details provided, email not sent.");
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("your-email@gmail.com");
            helper.setTo(userEmail);
            helper.setSubject("Your Booking is Confirmed! - BookMyShow Clone");

            String htmlContent = buildHtmlEmailContent(bookedTickets, userName, userEmail);
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
    private String buildHtmlEmailContent(List<TicketResponseDto> tickets, String userName, String userEmail) {
        TicketResponseDto firstTicket = tickets.get(0);
        String movieName = firstTicket.getMovie().getMovieName();
        String theaterName = firstTicket.getTheater().getName();
        String theaterAddress = firstTicket.getTheater().getAddress();
        String moviePosterUrl = firstTicket.getMovie().getImageUrl();
        String showDate = firstTicket.getDate().toString();
        String showTime = firstTicket.getTime().toString().substring(0, 5);

        String finalSeatNumbers = tickets.stream()
                                         .map(TicketResponseDto::getSeatNo)
                                         .collect(Collectors.joining(", "));
        
        int totalAmount = tickets.stream()
                                 .mapToInt(TicketResponseDto::getFare)
                                 .sum();

        // --- Build Food Items HTML ---
        StringBuilder foodItemsHtml = new StringBuilder();
        if (firstTicket.getPurchasedFoods() != null && !firstTicket.getPurchasedFoods().isEmpty()) {
            foodItemsHtml.append("<div style='margin-top: 20px; padding-top: 20px; border-top: 1px solid #eaeaea;'>");
            foodItemsHtml.append("<h3 style='font-size: 16px; color: #333; margin: 0 0 10px;'>Snacks & Beverages</h3>");
            for (ShowFood food : firstTicket.getPurchasedFoods()) {
                foodItemsHtml.append("<p style='margin: 4px 0; color: #666;'>- ").append(food.getName()).append("</p>");
            }
            foodItemsHtml.append("</div>");
        }

        // --- Build QR Code Data and URL ---
        String qrData = String.format("Ticket ID: %d, Movie: %s, User: %s, Seats: %s",
                firstTicket.getTicketId(), movieName, userName, finalSeatNumbers);
        String qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + URLEncoder.encode(qrData, StandardCharsets.UTF_8);


        // --- Main HTML Template ---
        return "<!DOCTYPE html>" +
            "<html lang='en'>" +
            "<head>" +
            "<meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<style>" +
            "  @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');" +
            "  body { font-family: 'Roboto', Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f5f9; color: #333; }" +
            "  .wrapper { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 6px 24px rgba(0,0,0,0.08); }" +
            "  .header { padding: 30px; text-align: center; background: linear-gradient(to right, #f84464, #d81b60); border-top-left-radius: 12px; border-top-right-radius: 12px; }" +
            "  .header .logo { font-size: 28px; font-weight: 700; color: #fff; } .header .logo span { font-weight: 500; }" +
            "  .content { padding: 30px; }" +
            "  .intro { text-align: center; margin-bottom: 30px; }" +
            "  .intro h1 { font-size: 26px; color: #333; margin-bottom: 10px; }" +
            "  .intro p { color: #666; font-size: 16px; }" +
            "  .ticket { border: 1px solid #e0e0e0; border-radius: 12px; overflow: hidden; }" +
            "  .ticket-main { padding: 20px; }" +
            "  .movie-info { display: flex; align-items: center; gap: 20px; }" +
            "  .movie-info img { width: 100px; border-radius: 8px; }" +
            "  .movie-details h2 { font-size: 22px; margin: 0 0 5px; color: #1a1a1a; }" +
            "  .movie-details p { margin: 4px 0; color: #666; }" +
            "  .booking-details { padding-top: 20px; margin-top: 20px; border-top: 1px solid #e0e0e0; }" +
            "  .detail-item { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f5f5f5; }" +
            "  .detail-item:last-child { border-bottom: none; }" +
            "  .detail-item .label { font-weight: 500; color: #555; }" +
            "  .detail-item .value { font-weight: 500; text-align: right; }" +
            "  .detail-item .seats { color: #f84464; font-weight: 700; font-size: 18px; }" +
            "  .ticket-stub { background-color: #fafafa; padding: 20px; border-top: 2px dashed #d81b60; display: flex; align-items: center; justify-content: space-between; }" +
            "  .qr-code img { width: 120px; height: 120px; }" +
            "  .stub-details { text-align: right; }" +
            "  .stub-details .total-label { font-size: 16px; color: #666; }" +
            "  .stub-details .total-amount { font-size: 28px; font-weight: 700; color: #333; }" +
            "  .footer { text-align: center; padding: 20px; font-size: 12px; color: #999; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='wrapper'>" +
            "  <div class='header'><div class='logo'>book<span>my</span>show</div></div>" +
            "  <div class='content'>" +
            "    <div class='intro'>" +
            "      <h1>Your Booking is Confirmed!</h1>" +
            "      <p>Hi " + userName + ", thank you for booking with us. Here is your e-ticket.</p>" +
            "    </div>" +
            "    <div class='ticket'>" +
            "      <div class='ticket-main'>" +
            "        <div class='movie-info'>" +
            "          <img src='" + moviePosterUrl + "' alt='Movie Poster'>" +
            "          <div class='movie-details'>" +
            "            <h2>"   + movieName + "</h2>" +
            "            <p>"    + theaterName + "</p>" +
            "            <p style='font-size: 14px;'>   "    + theaterAddress + "</p>" +
            "          </div>" +
            "        </div>" +
            "        <div class='booking-details'>" +
            "          <div class='detail-item'><span class='label'>Booked By</span><span class='value'>" + userName + "<br><span style='font-size:12px; color:#888;'>" + userEmail + "</span></span></div>" +
            "          <div class='detail-item'><span class='label'>Date</span><span class='value'>" + showDate + "</span></div>" +
            "          <div class='detail-item'><span class='label'>Time</span><span class='value'>" + showTime + "</span></div>" +
            "          <div class='detail-item'><span class='label'>Seats</span><span class='value seats'>" + finalSeatNumbers + "</span></div>" +
            "        </div>" +
            foodItemsHtml.toString() +
            "      </div>" +
            "      <div class='ticket-stub'>" +
            "        <div class='qr-code'>" +
            "          <img src='" + qrCodeUrl + "' alt='QR Code'>" +
            "        </div>" +
            "        <div class='stub-details'>" +
            "          <p class='total-label'>Total Amount Paid</p>" +
            "          <p class='total-amount'>₹" + totalAmount + "</p>" +
            "        </div>" +
            "      </div>" +
            "    </div>" +
            "  </div>" +
            "  <div class='footer'>© 2025 BookMyShow Clone. All Rights Reserved.</div>" +
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
