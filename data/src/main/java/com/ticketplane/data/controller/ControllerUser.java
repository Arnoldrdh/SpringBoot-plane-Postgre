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
import com.ticketplane.data.service.ServiceUser;

@RestController
@RequestMapping("/user")
public class ControllerUser {
    @Autowired
    private ServiceUser srUser;

    // register
    @PostMapping("/register")
    public boolean addUser(@RequestBody ModelUser user) {
        return srUser.userRegister(user);

    }

    // login
    @PostMapping("/login")
    public boolean loginUser(@RequestBody ModelUser user) {
        return srUser.userLogin(user);
    }

    // profile user
    @GetMapping("/profile/{userId}")
    public UserSummaryDTO userProfile(@PathVariable Integer userId) {
        return srUser.userProfile(userId);
    }

    // update profile
    @PutMapping("/update/{userId}")
    public boolean updateProfileUser(@PathVariable Integer userId, @RequestBody ModelUser userData) {
        return srUser.updateProfileUser(userId, userData);
    }

    // get all user
    @GetMapping("/getAll")
    public Iterable<ModelUser> getAllUser() {
        return srUser.getAllUser();
    }

    // deleteuser
    @DeleteMapping("/delete/{userId}")
    public boolean deleteUser(@PathVariable Integer userId) {

        return srUser.deleteUser(userId);
    }
}
