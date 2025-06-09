package com.ticketplane.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.ticketplane.data.model.ModelUser;

public interface RepositoryUser extends CrudRepository<ModelUser, Integer> {

    ModelUser findByEmailAndPassword(String email, String password);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
