package com.ticketplane.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketplane.data.dto.UserSummaryDTO;
import com.ticketplane.data.model.ModelUser;
import com.ticketplane.data.repository.RepositoryUser;
import com.ticketplane.data.service.ServiceUser;

@RestController
@RequestMapping("/user")
public class ControllerUser {
    @Autowired
    private ServiceUser srUser;

    @Autowired
    private RepositoryUser rpUser;

    // register
    @PostMapping("/register")
    public boolean addUser(@RequestBody ModelUser user) {
        // if (rpUser.existsByEmail(user.getEmail()) ||
        // rpUser.existsByUsername(user.getUsername())) {
        // return false;
        // }

        // rpUser.save(user);
        // return true;
        return srUser.userRegister(user);

    }

    // login
    @PostMapping("/login")
    public boolean loginUser(@RequestBody ModelUser user) {
        // ModelUser userData = rpUser.findByEmailAndPassword(user.getEmail(),
        // user.getPassword());
        // return userData != null;
    }

    // profile user
    @GetMapping("/profile/{userId}")
    public UserSummaryDTO userProfile(@PathVariable Integer userId) {
        // ModelUser userProfile = rpUser.findById(userId).orElse(null);
        // if (rpUser.existsById(userId)) {
        // UserSummaryDTO dto = new UserSummaryDTO();
        // dto.setUsername(userProfile.getUsername());
        // dto.setEmail(userProfile.getEmail());
        // dto.setPhoneNumber(userProfile.getPhoneNumber());
        // return dto;
        // }
        // return null;
    }

    // update profile
    @PutMapping("/update/{userId}")
    public boolean updateProfileUser(@PathVariable Integer userId, @RequestBody ModelUser userData) {
        // if (rpUser.existsById(userId)) {
        // @SuppressWarnings("Null")
        // ModelUser user = rpUser.findById(userId).get();
        // user.setUsername(userData.getUsername());
        // user.setEmail(userData.getEmail());
        // user.setPassword(userData.getPassword());
        // user.setPhoneNumber(userData.getPhoneNumber());
        // rpUser.save(user);
        // return true;
        // }
        // return false;
    }

    // deleteuser
    @DeleteMapping("/delete/{userId}")
    public boolean deleteUser(@PathVariable Integer userId) {
        // if (rpUser.existsById(userId)) {
        // rpUser.deleteById(userId);
        // return true;
        // }

        // return false;
    }
}
