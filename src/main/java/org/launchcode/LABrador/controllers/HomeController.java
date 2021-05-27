package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.LabRepository;
import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.Lab;
import org.launchcode.LABrador.models.User;
import org.launchcode.LABrador.models.dto.LabFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabRepository labRepository;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {

        //grabs the session from the browser and uses that to find which user's information to display.
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute("labs", labRepository.findAll());

        return "index";
    }

    @PostMapping
    public String processIndex(@ModelAttribute @Valid Lab newLab, Errors errors, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Lab");
            return "index";
        }

//        model.addAttribute("user", userFromSession);
//        model.addAttribute("user.lab", labRepository.findLabByLabName(newLab.getLabName()));
//        model.addAttribute("labFormDTO", new LabFormDTO());
        redirectAttributes.addFlashAttribute("lab", newLab);
        return "redirect:lab/passcode";
    }
}