package com.ticketplane.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketplane.data.dto.PaymentResponseDTO;
import com.ticketplane.data.dto.RequestPayment;
import com.ticketplane.data.service.ServicePayment;

@RestController
@RequestMapping("/payment")
public class ControllerPayment {
    @Autowired
    private ServicePayment srPayment;

    @PostMapping("/pay")
    public PaymentResponseDTO payBooking(@RequestBody RequestPayment reqPayment) {

        return srPayment.payBooking(reqPayment);

    }

    @DeleteMapping("/delete/{paymentId}")
    public boolean deletePayment(@PathVariable Integer paymentId) {

        return srPayment.deletePayment(paymentId);
    }
}
