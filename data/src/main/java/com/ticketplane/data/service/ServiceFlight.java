package com.ticketplane.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketplane.data.model.ModelFlight;
import com.ticketplane.data.repository.RepositoryFlight;

@Service
public class ServiceFlight {
    @Autowired
    private RepositoryFlight rpFlight;

    // add penerbangan
    public boolean addFlights(List<ModelFlight> flights) {
        rpFlight.saveAll(flights);
        return true;
    }

    // list penerbangan

    public Iterable<ModelFlight> listFlight() {
        return rpFlight.findAll();
    }

    // cari penerbangan berdasrkan kota asal dan tujuan

    public Iterable<ModelFlight> flightByCity(ModelFlight flight) {
        return rpFlight.findByDepartureAndDestination(flight.getDeparture(), flight.getDestination());
    }
}
