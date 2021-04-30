package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.User;
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

        model.addAttribute("title", "Edit User");
        model.addAttribute(userRepository.findByUsername(userFromSession.getUsername()));
        return "user/edit";
    }

    @PostMapping("edit")
    public String processEditUserForm(@ModelAttribute @Valid User user, Model model, Errors errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit User");
            return "user/edit";
        }

//        animalRepository.findById(animalId).get().setTag(animal.getTag());
//        animalRepository.findById(animalId).get().setCageNumber(animal.getCageNumber());
//        animalRepository.findById(animalId).get().setCageType(animal.getCageType());
//        animalRepository.findById(animalId).get().setSex(animal.getSex());
//        animalRepository.findById(animalId).get().setDateOfBirth(animal.getDateOfBirth());
//        animalRepository.findById(animalId).get().setDateOpened(animal.getDateOpened());
//        animalRepository.findById(animalId).get().setGenotypeOne(animal.getGenotypeOne());
//        animalRepository.findById(animalId).get().setGenotypeTwo(animal.getGenotypeTwo());
//        animalRepository.findById(animalId).get().setLitter(animal.getLitter());
//        animalRepository.findById(animalId).get().setNotes(animal.getNotes());
//
//        animalRepository.save(animalRepository.findById(animalId).get());
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        userRepository.findByUsername(userFromSession.getUsername()).setUsername(user.getUsername());
        userRepository.findByUsername(userFromSession.getUsername()).setFirstName(user.getFirstName());
        userRepository.findByUsername(userFromSession.getUsername()).setLastName(user.getLastName());
        userRepository.findByUsername(userFromSession.getUsername()).setEmail(user.getEmail());
        userRepository.findByUsername(userFromSession.getUsername()).setLab(user.getLab());

        userRepository.findByUsername(userFromSession.getUsername()).setPwHash(userRepository.findByUsername(userFromSession.getUsername()).getPwHash());

        userRepository.save(userRepository.findByUsername(userFromSession.getUsername()));
//        userFromSession.setUsername(user.getUsername());
//        userFromSession.setFirstName(user.getFirstName());
//        userFromSession.setLastName(user.getLastName());
//        userFromSession.setEmail(user.getEmail());
//        userFromSession.setLab(user.getLab());
//
//        userFromSession.setPwHash(userFromSession.getPwHash());

        return "redirect:../";
    }
}
