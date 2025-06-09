package com.ticketplane.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.ticketplane.data.model.ModelPayment;

public interface RepositoryPayment extends CrudRepository<ModelPayment, Integer> {

}
