package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
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
@RequestMapping("colony")
public class AnimalController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private AnimalRepository animalRepository;

    @GetMapping
    public String displayAllAnimals(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("animals", animalRepository.findAll());
        return "colony/index";
    }

    @GetMapping("add")
    public String displayAddAnimalForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Add Entry");
        model.addAttribute(new Animal());
        return "colony/add";
    }

    @PostMapping("add")
    public String processAddAnimalForm(@ModelAttribute @Valid Animal newAnimal, Errors errors, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Entry");
            return "colony/add";
        }

        if (!newAnimal.getNotesDescription().isEmpty() && newAnimal.getNotesKeyword().isEmpty() ) {
            errors.rejectValue("notesKeyword", "notesKeyword.isblank", "Must enter a keyword.");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Add Entry");
            return "colony/add";
        }

        animalRepository.save(newAnimal);
        return "redirect:";
    }

    @GetMapping("edit/{animalId}")
    public String displayEditAnimalForm(Model model, @PathVariable  int animalId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Edit Entry");
        model.addAttribute(animalRepository.findById(animalId));
        return "colony/edit";
    }

    @PostMapping("edit/{animalId}")
    public String processEditAnimalForm(@ModelAttribute @Valid Animal animal, Errors errors, HttpServletRequest request, Model model, @PathVariable int animalId) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);

        if (errors.hasErrors()) {
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Edit Entry");
            return "colony/edit";
        }

        if (!animal.getNotesDescription().isEmpty() && animal.getNotesKeyword().isEmpty() ) {
            errors.rejectValue("notesKeyword", "notesKeyword.isblank", "Must enter a keyword.");
            model.addAttribute("user", userFromSession);
            model.addAttribute("title", "Edit Entry");
            return "colony/edit";
        }

        Animal animalTmp = animalRepository.findById(animalId);

        animalTmp.setTag(animal.getTag());
        animalTmp.setCageNumber(animal.getCageNumber());
        animalTmp.setCageType(animal.getCageType());
        animalTmp.setSex(animal.getSex());
        animalTmp.setDateOfBirth(animal.getDateOfBirth());
        animalTmp.setGenotypeOne(animal.getGenotypeOne());
        animalTmp.setGenotypeTwo(animal.getGenotypeTwo());
        animalTmp.setLitter(animal.getLitter());
        animalTmp.setNotesKeyword(animal.getNotesKeyword());
        animalTmp.setNotesDescription(animal.getNotesDescription());

        animalRepository.save(animalTmp);

        return "redirect:../";
    }

    @PostMapping
    public String processDeleteAnimalForm(Model model, HttpServletRequest request, @RequestParam(required = false) int[] animalIds) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        if (animalIds != null) {
            for (int id : animalIds) {
                animalRepository.deleteById(id);
            }
        }
        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("animals", animalRepository.findAll());
        return "colony/index";
    }
}
