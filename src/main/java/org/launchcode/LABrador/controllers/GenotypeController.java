package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.GenotypeRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.Genotype;
import org.launchcode.LABrador.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("colony/genotype")
public class GenotypeController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private GenotypeRepository genotypeRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @GetMapping
    public String displayAllGenotypes(HttpServletRequest request, Model model, @RequestParam(required = false) int[] genotypeIds) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Lab Genotypes");
        model.addAttribute("genotype", genotypeRepository.findAll());
        return "colony/genotype/index";
    }

    @GetMapping("add")
    public String displayAddGenotypeForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Lab Genotypes");
        model.addAttribute("genotypes", genotypeRepository.findAll());
        model.addAttribute(new Genotype());
        return "colony/genotype/add";
    }

    @PostMapping("add")
    public String processAddGenotypeForm(HttpServletRequest request, @ModelAttribute Genotype newGenotype, Model model, Errors errors) {
        Genotype existingGenotype = genotypeRepository.findByName(newGenotype.getName());

        if (existingGenotype != null) {
            HttpSession session = request.getSession();
            User userFromSession = authenticationController.getUserFromSession(session);
            model.addAttribute("user", userFromSession);

            errors.rejectValue("name", "name.alreadyexists", "Already exists!");
            model.addAttribute("title", "Lab Genotypes");
            model.addAttribute("genotypes", genotypeRepository.findAll());
            return "colony/genotype/add";
        }
        genotypeRepository.save(newGenotype);
        return "redirect:";
    }

    @PostMapping
    public String processDeleteGenotypeForm(HttpServletRequest request, Model model, @RequestParam(required = false) int[] genotypeIds) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        Iterable<Animal> animals = animalRepository.findAll();
        Genotype blankGenotype = genotypeRepository.findByName("");

        if (genotypeIds != null) {
            for (int id : genotypeIds) {
                for (Animal animal : animals) {
                    if (animal.getGenotype().getId().compareTo(id) == 0) {
                        animal.setGenotype(blankGenotype);
                        animalRepository.save(animal);
                    }
                }
                genotypeRepository.deleteById(id);
            }
        }
        model.addAttribute("title", "Lab Genotypes");
        model.addAttribute("genotype", genotypeRepository.findAll());
        return "colony/genotype/index";
    }


}
