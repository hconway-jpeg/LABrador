package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.Status;
import org.launchcode.LABrador.data.userRepository;
import org.launchcode.LABrador.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class logincontroller{
    @Autowired
    userRepository UserRepository;
    @PostMapping("/register")
    public Status registerUser(@RequestBody User newUser) {
        List<User> users = UserRepository.findAll();
        System.out.println("New user: " + newUser.toString());
        for (User user : users) {
            System.out.println("Registered user: " + newUser.toString());
            if (user.equals(newUser)) {
                System.out.println("User Already exists!");
                return Status.USER_ALREADY_EXISTS;
            }
        }
        UserRepository.save(newUser);
        return Status.SUCCESS;
        //redirect to login
    }
    @PostMapping("/login")
    public Status loginUser(@RequestBody User user) {
        List<User> users = UserRepository.findAll();
        for (User other : users) {
            if (other.equals(user)) {
                user.setLoggedIn(true);
                UserRepository.save(user);
                return Status.SUCCESS;
                //redirect
            }
        }
        return Status.FAILURE;
        //redirect back to login
    }
}





