package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.LabRepository;
import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.Lab;
import org.launchcode.LABrador.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("lab")
public class LabController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabRepository labRepository;

    @GetMapping
    public String displayAddLabForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Add Lab");
        model.addAttribute(new Lab());
        return "lab/add";
    }

    @PostMapping
    public String processAddLabForm(HttpServletRequest request, @ModelAttribute @Valid Lab newLab, Model model, Errors errors) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Lab");
            return "lab/add";
        }
        labRepository.save(newLab);
        return "redirect:";
    }

}
