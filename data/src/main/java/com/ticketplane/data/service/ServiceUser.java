package com.ticketplane.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ticketplane.data.dto.UserSummaryDTO;
import com.ticketplane.data.model.ModelUser;
import com.ticketplane.data.repository.RepositoryUser;

@Service
public class ServiceUser {
    @Autowired
    private RepositoryUser rpUser;

    // register user
    public boolean userRegister(@RequestBody ModelUser user) {
        if (rpUser.existsByEmail(user.getEmail()) || rpUser.existsByUsername(user.getUsername())) {
            return false;
        }

        rpUser.save(user);
        return true;
    }

    // login user
    public boolean userLogin(@RequestBody ModelUser user) {
        ModelUser userData = rpUser.findByEmailAndPassword(user.getEmail(), user.getPassword());
        return userData != null;
    }

    // profile user
    public UserSummaryDTO userProfile(@PathVariable Integer userId) {
        ModelUser userProfile = rpUser.findById(userId).orElse(null);
        if (rpUser.existsById(userId)) {
            UserSummaryDTO dto = new UserSummaryDTO();
            dto.setUsername(userProfile.getUsername());
            dto.setEmail(userProfile.getEmail());
            dto.setPhoneNumber(userProfile.getPhoneNumber());
            return dto;
        }
        return null;
    }

    // update profile user
    public boolean updateProfileUser(@PathVariable Integer userId, @RequestBody ModelUser userData) {
        if (rpUser.existsById(userId)) {
            @SuppressWarnings("Null")
            ModelUser user = rpUser.findById(userId).get();
            user.setUsername(userData.getUsername());
            user.setEmail(userData.getEmail());
            user.setPassword(userData.getPassword());
            user.setPhoneNumber(userData.getPhoneNumber());
            rpUser.save(user);
            return true;
        }
        return false;
    }

    // get all user
    public Iterable<ModelUser> getAllUser() {
        return rpUser.findAll();
    }

    // deleteuser
    public boolean deleteUser(@PathVariable Integer userId) {
        if (rpUser.existsById(userId)) {
            rpUser.deleteById(userId);
            return true;
        }

        return false;
    }
}
