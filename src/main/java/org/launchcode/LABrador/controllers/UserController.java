package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.User;
import org.launchcode.LABrador.models.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        model.addAttribute(new EditFormDTO());
        model.addAttribute("title", "Edit User");
        model.addAttribute(userRepository.findByUsername(userFromSession.getUsername()));
        return "user/edit";
    }

    @PostMapping("edit")
    public String processEditUserForm(@ModelAttribute @Valid EditFormDTO editFormDTO, Errors errors, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            return "user/edit";
        }

        String password = editFormDTO.getPassword();
        if(!userFromSession.isMatchingPassword(password)){
            errors.rejectValue("password","password.invalid", "Incorrect password.");
            model.addAttribute("title", "Edit User");
            model.addAttribute("user", userFromSession);
            return "user/edit";
        }

        User userTmp = userRepository.findByUsername(userFromSession.getUsername());
        userTmp.setFirstName(editFormDTO.getFirstName());
        userTmp.setLastName(editFormDTO.getLastName());
        userTmp.setEmail(editFormDTO.getEmail());
        userTmp.setLab(editFormDTO.getLab());
        userRepository.save(userTmp);

        model.addAttribute("user", userTmp);
        return "redirect:";
    }

    @GetMapping("username")
    public String displayEditUsernameForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Edit Username");
        model.addAttribute(userRepository.findByUsername(userFromSession.getUsername()));
        return "user/username";
    }

    @PostMapping("username")
    public String processEditUsernameForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO, Errors errors, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            return "user/username";
        }

        User userExists = userRepository.findByUsername(loginFormDTO.getUsername());
        if(userExists != null){
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists.");
            model.addAttribute("title", "Edit Username");
            model.addAttribute("user", userFromSession);
            return "user/username";
        }

        String password = loginFormDTO.getPassword();
        if(!userFromSession.isMatchingPassword(password)){
            errors.rejectValue("password","password.invalid", "Incorrect password.");
            model.addAttribute("title", "Edit Username");
            model.addAttribute("user", userFromSession);
            return "user/username";
        }

        User userTmp = userRepository.findByUsername(userFromSession.getUsername());
        userTmp.setUsername(loginFormDTO.getUsername());
        userRepository.save(userTmp);

        model.addAttribute("user", userTmp);
        return "redirect:";
    }

    @GetMapping("password")
    public String displayNewPasswordForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute(new PwFormDTO());
        model.addAttribute("title", "Edit Password");
        model.addAttribute(userRepository.findByUsername(userFromSession.getUsername()));
        return "user/password";
    }

    @PostMapping("password")
    public String processNewPasswordForm(@ModelAttribute @Valid PwFormDTO pwFormDTO, Errors errors, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            return "user/password";
        }

        String password = pwFormDTO.getPassword();
        if(!userFromSession.isMatchingPassword(password)){
            errors.rejectValue("password","password.invalid", "Incorrect password.");
            model.addAttribute("title", "Edit Password");
            model.addAttribute("user", userFromSession);
            return "user/password";
        }

        String newPassword = pwFormDTO.getNewPassword();
        String verifyNewPassword = pwFormDTO.getVerifyNewPassword();
        if(!newPassword.equals(verifyNewPassword)){
            errors.rejectValue("newPassword", "passwords.mismatch", "Passwords do not match.");
            model.addAttribute("title", "Edit Password");
            model.addAttribute("user", userFromSession);
            return "user/password";
        }

        User userTmp = userRepository.findByUsername(userFromSession.getUsername());
        userTmp.setPwHash(pwFormDTO.getNewPassword());
        userRepository.save(userTmp);

        model.addAttribute("user", userTmp);
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteUserForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute(new DeleteFormDTO());
        model.addAttribute("title", "Delete User");
        model.addAttribute(userRepository.findByUsername(userFromSession.getUsername()));
        return "user/delete";
    }

    @PostMapping("delete")
    public String processDeleteUserForm(@ModelAttribute @Valid DeleteFormDTO deleteFormDTO, Errors errors, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            return "user/delete";
        }

        String password = deleteFormDTO.getPassword();
        if(!userFromSession.isMatchingPassword(password)){
            errors.rejectValue("password","password.invalid", "Incorrect password.");
            model.addAttribute("title", "Delete User");
            model.addAttribute("user", userFromSession);
            return "user/delete";
        }

        User userDel = userRepository.findByUsername(userFromSession.getUsername());
        userRepository.deleteById(userDel.getId());
        return "redirect:";
    }
}
