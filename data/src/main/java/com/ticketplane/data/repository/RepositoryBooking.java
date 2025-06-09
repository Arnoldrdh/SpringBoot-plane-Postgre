package com.ticketplane.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ticketplane.data.model.ModelBooking;

public interface RepositoryBooking extends CrudRepository<ModelBooking, Integer> {

    List<ModelBooking> findByUserUserId(Integer userId);

}
