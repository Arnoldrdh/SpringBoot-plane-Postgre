package com.ticketplane.data.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketplane.data.dto.BookingResponseDTO;
import com.ticketplane.data.dto.FlightSummaryDTO;
import com.ticketplane.data.dto.RequestBooking;
import com.ticketplane.data.dto.UserSummaryDTO;
import com.ticketplane.data.model.ModelBooking;
import com.ticketplane.data.model.ModelFlight;
import com.ticketplane.data.model.ModelUser;
import com.ticketplane.data.repository.RepositoryBooking;
import com.ticketplane.data.repository.RepositoryFlight;
import com.ticketplane.data.repository.RepositoryUser;

@RestController
@RequestMapping("/booking")
public class ControllerBooking {
    @Autowired
    private RepositoryBooking rpBooking;

    @Autowired
    private RepositoryFlight rpFlight;

    @Autowired
    private RepositoryUser rpUser;

    // create booking
    @PostMapping("/create")
    public BookingResponseDTO createBooking(@RequestBody RequestBooking reqBooking) {
        ModelBooking bookingData = new ModelBooking();

        // ambil data penerbangan
        ModelFlight flightData = rpFlight.findById(reqBooking.getFlightId()).orElse(null);

        if (flightData == null) {
            throw new RuntimeException("Penerbangan Tidak Ditemukan");
        }

        // ambil data user
        ModelUser userData = rpUser.findById(reqBooking.getUserId()).orElse(null);

        // set relasi
        bookingData.setFlight(flightData);
        bookingData.setUser(userData);

        // set nilau default
        bookingData.setStatus("Booked");
        bookingData.setPaymentStatus("Unpaid");
        bookingData.setBookingTime(LocalDateTime.now().toString());
        bookingData.setSeatNumber(reqBooking.getSeatNumber());

        ModelBooking savedBooking = rpBooking.save(bookingData);

        //response bagian flight dto
        BookingResponseDTO response = new BookingResponseDTO();
        response.setBookingId(savedBooking.getBookingId());
        response.setSeatNumber(savedBooking.getSeatNumber());
        response.setPrice(flightData.getPrice());
        response.setStatus(savedBooking.getStatus());
        response.setBookingTime(savedBooking.getBookingTime());
        response.setPaymentStatus(savedBooking.getPaymentStatus());

         // Buat DTO untuk flight
        FlightSummaryDTO flightDTO = new FlightSummaryDTO();
        flightDTO.setFlightNumber(flightData.getFlightNumber());
        flightDTO.setDeparture(flightData.getDeparture());
        flightDTO.setDestination(flightData.getDestination());
        response.setFlight(flightDTO);

        //  Buat DTO untuk user
        UserSummaryDTO userDTO = new UserSummaryDTO();
        userDTO.setUsername(userData.getUsername());
        userDTO.setEmail(userData.getEmail());
        response.setUser(userDTO);

        return response;
    }

    // update jadi cancel
    @PutMapping("/cancel/{bookingId}")
    public boolean cancelBooking(@PathVariable Integer bookingId) {
        ModelBooking booking = rpBooking.findById(bookingId).orElse(null);
        if (booking == null) {
            return false;
        }

        booking.setStatus("Canceled");
        rpBooking.save(booking);
        return true;
    }

    // detail tiket
    @GetMapping("/detail/{bookingId}")
    public ModelBooking detailBooking(@PathVariable Integer bookingId) {
        return rpBooking.findById(bookingId).orElse(null);
    }

    // history pemesanan tiket
    @GetMapping("/history/user/{userId}")
    public List<ModelBooking> historyBookingUser(@PathVariable Integer userId) {
        return rpBooking.findByUserUserId(userId);
    }

    // delete booking (hapus payment nya dulu)
    @DeleteMapping("/delete/{bookingId}")
    public boolean deleteBookingHistory(@PathVariable Integer bookingId) {
        if (rpBooking.existsById(bookingId)) {
            rpBooking.deleteById(bookingId);
            return true;
        }

        return false;
    }

}
