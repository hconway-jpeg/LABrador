package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.LabRepository;
import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.Lab;
import org.launchcode.LABrador.models.User;
import org.launchcode.LABrador.models.dto.LabFormDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("lab")
public class LabController {

    Logger logger = LoggerFactory.getLogger(LabController.class);

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
        logger.info("AddLabForm display pending.");
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "Add Lab");
        model.addAttribute(new Lab());
        logger.info("AddLabForm display successful.");
        return "lab/add";
    }

    @PostMapping("add")
    public String processAddLabForm(@ModelAttribute @Valid Lab newLab, Errors errors, HttpServletRequest request, Model model) {
        logger.info("AddLabForm processing in progress.");
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            logger.info("processAddLabForm has errors.");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Lab");
            return "lab/add";
        }

        Lab labExists = labRepository.findLabByLabName(newLab.getLabName());
        if (labExists != null) {
            logger.info("A lab with this name already exists.");
            errors.rejectValue("labName","labName.alreadyexists", "A lab with this name already exists.");

            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Lab");
            return "lab/add";
        }

        newLab.getUsers().add(userFromSession);
        labRepository.save(newLab);
        User userTmp = userRepository.findByUsername(userFromSession.getUsername());
        userTmp.addLab(newLab);
        userRepository.save(userTmp);
        logger.info("AddLabForm processing successful.");
        return "redirect:";
    }

    @GetMapping("passcode")
    public String displayPasscodeForm(@ModelAttribute @Valid Lab lab, HttpServletRequest request,  Model model, RedirectAttributes redirectAttributes) {
        logger.info("PasscodeForm display pending.");
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        LabFormDTO labFormDTO = new LabFormDTO();
        labFormDTO.setLabName(lab.getLabName());

        model.addAttribute("user", userFromSession);
        model.addAttribute("title", "Join Lab");
        model.addAttribute("lab", labRepository.findLabByLabName(lab.getLabName()));
        model.addAttribute("labFormDTO", labFormDTO);
        logger.info("PasscodeForm display successful.");
        return "lab/passcode";
    }

    @PostMapping("passcode")
    public String processPasscodeForm(@ModelAttribute @Valid LabFormDTO labFormDTO, Errors errors, HttpServletRequest request, Model model) {
        logger.info("PasscodeForm processing in progress.");
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            logger.info("PasscodeForm has errors.");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Join Lab");
            model.addAttribute("lab", labRepository.findLabByLabName(labFormDTO.getLabName()));
            model.addAttribute("labFormDTO", labFormDTO);
            return "lab/passcode";
        }

        Lab tmp = labRepository.findLabByLabName(labFormDTO.getLabName());
        String passcode = tmp.getPasscode();
        String pcCheck = labFormDTO.getPcCheck();
        if(!passcode.equals(pcCheck)){
            logger.info("PasscodeForm passcode does not match.");
            errors.rejectValue("pcCheck","pcCheck.mismatch", "Incorrect Passcode");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Join Lab");
            model.addAttribute("lab", labRepository.findLabByLabName(labFormDTO.getLabName()));
            model.addAttribute("labFormDTO", labFormDTO);
            return "lab/passcode";
        }

        User userTmp = userRepository.findByUsername(userFromSession.getUsername());
        Lab labExists = null;
        for (Lab lab : userTmp.getLab()) {
            if (lab.getLabName().equals(labFormDTO.getLabName())){
                labExists = lab;
            }
        }

        if(labExists != null) {
            logger.info("User is already a member of this lab.");
            errors.rejectValue("pcCheck","pcCheck.alreadymember", "You are already a member of this lab.");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Join Lab");
            model.addAttribute("lab", labRepository.findLabByLabName(labFormDTO.getLabName()));
            model.addAttribute("labFormDTO", labFormDTO);
            return "lab/passcode";
        }

        tmp.addUser(userFromSession);
        labRepository.save(tmp);
        userTmp.addLab(tmp);
        userRepository.save(userTmp);
        logger.info("PasscodeForm processing successful.");
        return "redirect:";
    }
}
