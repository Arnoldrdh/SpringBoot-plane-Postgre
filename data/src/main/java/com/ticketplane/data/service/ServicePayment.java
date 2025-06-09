package com.ticketplane.data.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketplane.data.dto.BookingSummaryDTO;
import com.ticketplane.data.dto.FlightSummaryDTO;
import com.ticketplane.data.dto.PaymentResponseDTO;
import com.ticketplane.data.dto.RequestPayment;
import com.ticketplane.data.dto.UserSummaryDTO;
import com.ticketplane.data.model.ModelBooking;
import com.ticketplane.data.model.ModelFlight;
import com.ticketplane.data.model.ModelPayment;
import com.ticketplane.data.model.ModelUser;
import com.ticketplane.data.repository.RepositoryBooking;
import com.ticketplane.data.repository.RepositoryPayment;

@Service
public class ServicePayment {
    @Autowired
    private RepositoryPayment rpPayment;

    @Autowired
    private RepositoryBooking rpBooking;

    public PaymentResponseDTO payBooking(RequestPayment reqPayment) {
        ModelBooking booking = rpBooking.findById(reqPayment.getBookingId()).orElse(null);

        if (booking == null) {
            throw new RuntimeException("Data tidak ditemukan");
        }

        Double ticketPrice = booking.getFlight().getPrice();

        if ("Paid".equalsIgnoreCase(booking.getPaymentStatus())) {
            throw new RuntimeException("sudah dibayar");
        }

        if (!reqPayment.getAmount().equals(ticketPrice)) {
            throw new RuntimeException("Harga Tidak Sesuai");
        }

        ModelPayment payment = new ModelPayment();
        payment.setPaymentMethod(reqPayment.getPaymentMethod());
        payment.setAmount(reqPayment.getAmount());
        payment.setPaymentStatus("Paid");
        payment.setPaymentTime(LocalDateTime.now().toString());

        // set relasi ke booking
        payment.setBooking(booking);
        ModelPayment savedPayment = rpPayment.save(payment);

        // update booking
        booking.setPaymentStatus("Paid");
        rpBooking.save(booking);

        // Build DTO response
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setPaymentId(savedPayment.getPaymentId());
        response.setPaymentMethod(savedPayment.getPaymentMethod());
        response.setPaymentStatus(savedPayment.getPaymentStatus());
        response.setPaymentTime(savedPayment.getPaymentTime());
        response.setAmount(savedPayment.getAmount());

        // Build Booking DTO
        BookingSummaryDTO bookingDTO = new BookingSummaryDTO();
        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setSeatNumber(booking.getSeatNumber());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setBookingTime(booking.getBookingTime());
        bookingDTO.setPaymentStatus(booking.getPaymentStatus());

        // Flight DTO
        ModelFlight flight = booking.getFlight();
        FlightSummaryDTO flightDTO = new FlightSummaryDTO();
        flightDTO.setFlightNumber(flight.getFlightNumber());
        flightDTO.setDeparture(flight.getDeparture());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setDepartureTime(flight.getDepartureTime());
        flightDTO.setArrivalTime(flight.getArrivalTime());
        bookingDTO.setFlight(flightDTO);

        // User DTO
        ModelUser user = booking.getUser();
        UserSummaryDTO userDTO = new UserSummaryDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        bookingDTO.setUser(userDTO);

        response.setBooking(bookingDTO);
        return response;

    }

    public boolean deletePayment(Integer paymentId) {
        if (rpPayment.existsById(paymentId)) {
            rpPayment.deleteById(paymentId);
            return true;
        }

        return false;
    }
}
