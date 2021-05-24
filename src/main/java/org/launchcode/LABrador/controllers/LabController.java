package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.LabRepository;
import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.Lab;
import org.launchcode.LABrador.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("lab")
public class LabController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabRepository labRepository;

    @GetMapping
    public String displayAddLabForm(Model model) {
        model.addAttribute("title", "Add Lab");
        model.addAttribute(new Lab());
        return "lab/add";
    }

}
