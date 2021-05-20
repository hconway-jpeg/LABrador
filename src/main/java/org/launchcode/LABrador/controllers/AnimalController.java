package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.GenotypeRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.User;
import org.launchcode.LABrador.models.Genotype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("colony")
public class AnimalController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private GenotypeRepository genotypeRepository;

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

        if (genotypeRepository.findByName("") == null) {
            Genotype blankGenotype = new Genotype("");
            genotypeRepository.save(blankGenotype);
        }

        model.addAttribute("title", "Add Entry");
        model.addAttribute("genotype", genotypeRepository.findAll());
        model.addAttribute(new Animal());
        return "colony/add";
    }

    @PostMapping("add")
    public String processAddAnimalForm(@ModelAttribute Animal newAnimal, Model model) {
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
        model.addAttribute("genotype", genotypeRepository.findAll());
        return "colony/edit";
    }

    @PostMapping("edit/{animalId}")
    public String processEditAnimalForm(@ModelAttribute @Valid Animal animal, Model model, Errors errors, @PathVariable int animalId) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Entry");
            return "colony/edit";
        }

        Animal animalTmp = animalRepository.findById(animalId);

        animalTmp.setTag(animal.getTag());
        animalTmp.setCageNumber(animal.getCageNumber());
        animalTmp.setCageType(animal.getCageType());
        animalTmp.setSex(animal.getSex());
        animalTmp.setDateOfBirth(animal.getDateOfBirth());
        animalTmp.setGenotype(animal.getGenotype());
        animalTmp.setLitter(animal.getLitter());
        animalTmp.setNotes(animal.getNotes());

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
