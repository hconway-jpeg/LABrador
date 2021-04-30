package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.User;
import org.launchcode.LABrador.models.dto.LoginFormDTO;
import org.launchcode.LABrador.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String displayUserInfo(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        return "user/index";
    }

    @GetMapping("edit")
    public String displayEditUserForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Edit User");
        model.addAttribute(userRepository.findByUsername(userFromSession.getUsername()));
        return "user/edit";
    }

    @PostMapping("edit")
    public String processEditUserForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO, Errors errors, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            return "user/edit";
        }

        User userFind = userRepository.findByUsername(registerFormDTO.getUsername());
        String password = registerFormDTO.getPassword();
        if(!userFind.isMatchingPassword(password)){
            errors.rejectValue("password","password.invalid", "Incorrect password.");
            model.addAttribute("title", "Edit User");
            model.addAttribute("user", userFromSession);
            return "user/edit";
        }









        model.addAttribute("user", userFromSession);

        userRepository.findByUsername(userFromSession.getUsername()).setUsername(registerFormDTO.getUsername());
        userRepository.findByUsername(userFromSession.getUsername()).setFirstName(registerFormDTO.getFirstName());
        userRepository.findByUsername(userFromSession.getUsername()).setLastName(registerFormDTO.getLastName());
        userRepository.findByUsername(userFromSession.getUsername()).setEmail(registerFormDTO.getEmail());
        userRepository.findByUsername(userFromSession.getUsername()).setLab(registerFormDTO.getLab());



        userRepository.save(userRepository.findByUsername(userFromSession.getUsername()));
        return "redirect:";
    }
}
