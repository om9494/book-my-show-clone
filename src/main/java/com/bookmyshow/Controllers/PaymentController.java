package com.bookmyshow.Controllers;

import com.bookmyshow.Dtos.RequestDtos.TicketEntryDto;
import com.bookmyshow.Services.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils; // This import is correct
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin // Allows requests from your React frontend
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Autowired
    private TicketService ticketService;

    // This endpoint remains unchanged and is correct.
    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> data) {
        try {
            int amount = (int) data.get("amount");
            RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "booking_receipt_" + System.currentTimeMillis());
            Order order = razorpayClient.orders.create(orderRequest);
            return ResponseEntity.ok(order.toString());
        } catch (Exception e) {
            System.err.println("Error creating Razorpay order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Razorpay order.");
        }
    }

    // This is the endpoint where the fix is applied.
    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPaymentAndBookTicket(@RequestBody Map<String, Object> payload) {
        try {
            String razorpayOrderId = (String) payload.get("razorpay_order_id");
            String razorpayPaymentId = (String) payload.get("razorpay_payment_id");
            String razorpaySignature = (String) payload.get("razorpay_signature");
            Map<String, Object> ticketEntryMap = (Map<String, Object>) payload.get("ticketEntryDto");

            // --- THIS IS THE CORRECTED LOGIC ---
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            boolean isSignatureVerified = Utils.verifyPaymentSignature(options, this.keySecret);
            // --- END OF CORRECTION ---

            if (!isSignatureVerified) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed. Signature mismatch.");
            }

            // If signature is verified, proceed to book the ticket
            System.out.println("Payment verification successful. Booking ticket...");
            ObjectMapper mapper = new ObjectMapper();
            TicketEntryDto ticketEntryDto = mapper.convertValue(ticketEntryMap, TicketEntryDto.class);
            ticketService.ticketBooking(ticketEntryDto);

            return ResponseEntity.ok("Payment verified successfully. Ticket booked!");

        } catch (Exception e) {
            System.err.println("Error in verifyPayment or ticket booking: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error during payment verification or booking.");
        }
    }
}
