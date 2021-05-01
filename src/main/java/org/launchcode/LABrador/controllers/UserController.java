package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.User;
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

        String password = registerFormDTO.getPassword();

        if(!userFromSession.isMatchingPassword(password)){
            errors.rejectValue("password","password.invalid", "Incorrect password.");
            model.addAttribute("title", "Edit User");
            model.addAttribute("user", userFromSession);
            return "user/edit";
        }

        User userTmp = userRepository.findByUsername(userFromSession.getUsername());

        userTmp.setUsername(registerFormDTO.getUsername());
        userTmp.setFirstName(registerFormDTO.getFirstName());
        userTmp.setLastName(registerFormDTO.getLastName());
        userTmp.setEmail(registerFormDTO.getEmail());
        userTmp.setLab(registerFormDTO.getLab());

        userRepository.save(userTmp);

        model.addAttribute("user", userTmp);

        return "redirect:";
    }
}
