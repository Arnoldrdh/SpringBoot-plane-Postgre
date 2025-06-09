package com.ticketplane.data.controller;

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
import com.ticketplane.data.dto.RequestBooking;
import com.ticketplane.data.model.ModelBooking;
import com.ticketplane.data.service.ServiceBooking;

@RestController
@RequestMapping("/booking")
public class ControllerBooking {
    @Autowired
    private ServiceBooking srBooking;

    // create booking
    @PostMapping("/create")
    public BookingResponseDTO createBooking(@RequestBody RequestBooking reqBooking) {

        return srBooking.createBooking(reqBooking);
    }

    // update jadi cancel
    @PutMapping("/cancel/{bookingId}")
    public boolean cancelBooking(@PathVariable Integer bookingId) {

        return srBooking.cancelBooking(bookingId);
    }

    // detail tiket
    @GetMapping("/detail/{bookingId}")
    public ModelBooking detailBooking(@PathVariable Integer bookingId) {

        return srBooking.detailBooking(bookingId);
    }

    // history pemesanan tiket
    @GetMapping("/history/user/{userId}")
    public List<ModelBooking> historyBookingUser(@PathVariable Integer userId) {

        return srBooking.historyBookingUser(userId);
    }

    // delete booking (hapus payment nya dulu)
    @DeleteMapping("/delete/{bookingId}")
    public boolean deleteBookingHistory(@PathVariable Integer bookingId) {

        return srBooking.deleteBookingHistory(bookingId);
    }

}
