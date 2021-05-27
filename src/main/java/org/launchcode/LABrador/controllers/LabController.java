package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.LabRepository;
import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.Lab;
import org.launchcode.LABrador.models.User;
import org.launchcode.LABrador.models.dto.LabFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
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
    public String displayLabs(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "User's Labs");
        model.addAttribute("labs", labRepository.findAll());
        return "lab/index";
    }

    @GetMapping("add")
    public String displayAddLabForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Add Lab");
        model.addAttribute(new Lab());
        return "lab/add";
    }

    @PostMapping("add")
    public String processAddLabForm(@ModelAttribute @Valid Lab newLab, Errors errors, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Lab");
            return "lab/add";
        }
        newLab.getUsers().add(userFromSession);
        labRepository.save(newLab);

        User userTmp = userRepository.findByUsername(userFromSession.getUsername());
        userTmp.addLab(newLab);
        userRepository.save(userTmp);
        return "redirect:";
    }

    @GetMapping("passcode")
    public String displayPasscodeForm(@ModelAttribute @Valid Lab lab, HttpServletRequest request,  Model model) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "Join Lab");
        model.addAttribute("lab", labRepository.findLabByLabName(lab.getLabName()));
        model.addAttribute("labFormDTO", new LabFormDTO());
        return "lab/passcode";
    }

    @PostMapping("passcode")
    public String processPasscodeForm(@ModelAttribute @Valid Lab lab, @ModelAttribute @Valid String passcode, Errors errors, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            return "lab/passcode";
        }

        Lab tmp = labRepository.findLabByLabName(lab.getLabName());
        String labPasscode = tmp.getPasscode();

        if(!labPasscode.equals(passcode)){
            errors.rejectValue("passcode","passcode.mismatch", "Incorrect Passcode");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Join Lab");
            return "lab/passcode";
        }

        tmp.addUser(userFromSession);
//        tmp.setLabName(lab.getLabName());
//        tmp.setContactEmail(lab.getContactEmail());
//        tmp.setDepartment(lab.getDepartment());
//        tmp.setOrganization(lab.getOrganization());
//        tmp.setPrincipalInvestigator(lab.getPrincipalInvestigator());
        labRepository.save(tmp);

        User userTmp = userRepository.findByUsername(userFromSession.getUsername());
        userTmp.addLab(tmp);
        userRepository.save(userTmp);
        return "redirect:../";
    }
}
