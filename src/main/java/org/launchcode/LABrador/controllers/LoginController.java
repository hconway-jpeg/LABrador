package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.User;
import org.launchcode.LABrador.models.dto.LoginFormDTO;
import org.launchcode.LABrador.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.launchcode.LABrador.controllers.AuthenticationController.setUserInSession;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage(Model model){
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "LABrador - Log In");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "LABrador - Register");
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute @Valid LoginFormDTO loginFormDTO, Errors errors,
                               HttpServletRequest request, Model model){
        if (errors.hasErrors()){
            model.addAttribute("title", "LABrador - Log In");
            return "login";
        }

        User userFind = userRepository.findByUsername(loginFormDTO.getUsername());

        if(userFind == null){
            errors.rejectValue("username", "user.invalid", "Invalid username.");
            model.addAttribute("title","LABrador - Log In");
            return "login";
        }

        String password = loginFormDTO.getPassword();
        if(!userFind.isMatchingPassword(password)){
            errors.rejectValue("password","password.invalid", "Invalid password.");
            model.addAttribute("title", "LABrador - Log In");
            return "login";
        }

        setUserInSession(request.getSession(), userFind);

        return "redirect:";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute @Valid RegisterFormDTO registerFormDTO, Errors errors,
                                      HttpServletRequest request, Model model){
        if (errors.hasErrors()){
            model.addAttribute("title", "LABrador - Register");
            return "register";
        }

        User userExists = userRepository.findByUsername(registerFormDTO.getUsername());

        if(userExists != null){
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists.");
            model.addAttribute("title", "LABrador - Register");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if(!password.equals(verifyPassword)){
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match.");
            model.addAttribute("title", "LABrador - Register");
            return "register";
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword(),
                registerFormDTO.getEmail(), registerFormDTO.getFirstName(), registerFormDTO.getLastName(),
                registerFormDTO.getLab());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        return "redirect:";

    }
}





