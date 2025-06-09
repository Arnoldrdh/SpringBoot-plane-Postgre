package com.ticketplane.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketplane.data.model.ModelFlight;
import com.ticketplane.data.service.ServiceFlight;

@RestController
@RequestMapping("/flight")
public class ControllerFlight {
    @Autowired
    private ServiceFlight srFlight;

    // add penerbangan
    @PostMapping("/add")
    public boolean addFlights(@RequestBody List<ModelFlight> flights) {
        // rpFlight.saveAll(flights);
        // return true;

        return srFlight.addFlights(flights);
    }

    // list penerbangan
    @GetMapping("/list")
    public Iterable<ModelFlight> listFlight() {
        // return rpFlight.findAll();
        return srFlight.listFlight();
    }

    // cari penerbangan berdasrkan kota asal dan tujuan
    @PostMapping("/city")
    public Iterable<ModelFlight> flightByCity(@RequestBody ModelFlight flight) {
        // return rpFlight.findByDepartureAndDestination(flight.getDeparture(),
        // flight.getDestination());
        return srFlight.flightByCity(flight);
    }
}
