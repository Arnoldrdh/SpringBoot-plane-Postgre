package com.ticketplane.data.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

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

@Service
public class ServiceBooking {
    @Autowired
    private RepositoryBooking rpBooking;

    @Autowired
    private RepositoryFlight rpFlight;

    @Autowired
    private RepositoryUser rpUser;

    // create booking
    @PostMapping("/create")
    public BookingResponseDTO createBooking(RequestBooking reqBooking) {
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
        flightData.setAvailableSeats(flightData.getAvailableSeats() - 1);
        rpFlight.save(flightData);

        // response bagian flight dto
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
        flightDTO.setDepartureTime(flightData.getDepartureTime());
        flightDTO.setArrivalTime(flightData.getArrivalTime());
        response.setFlight(flightDTO);

        // Buat DTO untuk user
        UserSummaryDTO userDTO = new UserSummaryDTO();
        userDTO.setUsername(userData.getUsername());
        userDTO.setEmail(userData.getEmail());
        userDTO.setPhoneNumber(userData.getPhoneNumber());
        response.setUser(userDTO);

        return response;
    }

    // cancel booking
    public boolean cancelBooking(Integer bookingId) {
        ModelBooking booking = rpBooking.findById(bookingId).orElse(null);
        if (booking == null) {
            return false;
        }

        // Kembalikan kursi ke ModelFlight
        ModelFlight flight = booking.getFlight();
        if (flight != null) {
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
            rpFlight.save(flight);
        }

        booking.setStatus("Canceled");
        rpBooking.save(booking);
        return true;
    }

    // detail tiket
    public BookingResponseDTO detailBooking(Integer bookingId) {
        ModelBooking booking = rpBooking.findById(bookingId).orElse(null);
        if (booking == null)
            return null;

        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setSeatNumber(booking.getSeatNumber());
        dto.setStatus(booking.getStatus());
        dto.setBookingTime(booking.getBookingTime().toString());
        dto.setPaymentStatus(booking.getPaymentStatus());
        dto.setPrice(booking.getFlight().getPrice());

        FlightSummaryDTO flightDTO = new FlightSummaryDTO();
        flightDTO.setFlightNumber(booking.getFlight().getFlightNumber());
        flightDTO.setDeparture(booking.getFlight().getDeparture());
        flightDTO.setDestination(booking.getFlight().getDestination());
        flightDTO.setDepartureTime(booking.getFlight().getDepartureTime().toString());
        flightDTO.setArrivalTime(booking.getFlight().getArrivalTime().toString());
        dto.setFlight(flightDTO);

        // Map user
        UserSummaryDTO userDTO = new UserSummaryDTO();
        userDTO.setUsername(booking.getUser().getUsername());
        userDTO.setEmail(booking.getUser().getEmail());
        userDTO.setPhoneNumber(booking.getUser().getPhoneNumber());
        dto.setUser(userDTO);

        return dto;
    }

    // history pemesanan tiket
    public List<BookingResponseDTO> historyBookingUser(Integer userId) {
        List<ModelBooking> bookings = rpBooking.findByUserUserId(userId);
        List<BookingResponseDTO> responseList = new ArrayList<>();

        for (ModelBooking booking : bookings) {
            BookingResponseDTO dto = new BookingResponseDTO();
            dto.setBookingId(booking.getBookingId());
            dto.setSeatNumber(booking.getSeatNumber());
            dto.setStatus(booking.getStatus());
            dto.setBookingTime(booking.getBookingTime());
            dto.setPaymentStatus(booking.getPaymentStatus());

            // Flight
            ModelFlight flight = booking.getFlight();
            if (flight != null) {
                FlightSummaryDTO flightDTO = new FlightSummaryDTO();
                flightDTO.setFlightNumber(flight.getFlightNumber());
                flightDTO.setDeparture(flight.getDeparture());
                flightDTO.setDestination(flight.getDestination());
                flightDTO.setDepartureTime(flight.getDepartureTime());
                flightDTO.setArrivalTime(flight.getArrivalTime());
                dto.setFlight(flightDTO);

                dto.setPrice(flight.getPrice()); // Set harga dari flight
            }

            // User
            ModelUser user = booking.getUser();
            if (user != null) {
                UserSummaryDTO userDTO = new UserSummaryDTO();
                userDTO.setUsername(user.getUsername());
                userDTO.setEmail(user.getEmail());
                userDTO.setPhoneNumber(user.getPhoneNumber());
                dto.setUser(userDTO);
            }

            responseList.add(dto);
        }

        return responseList;
    }

    // delete booking (hapus payment nya dulu)
    public boolean deleteBookingHistory(Integer bookingId) {
        if (rpBooking.existsById(bookingId)) {
            rpBooking.deleteById(bookingId);
            return true;
        }

        return false;
    }

}
