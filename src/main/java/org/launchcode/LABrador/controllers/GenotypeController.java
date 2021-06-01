package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.GenotypeRepository;
import org.launchcode.LABrador.data.LabRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.Genotype;
import org.launchcode.LABrador.models.Lab;
import org.launchcode.LABrador.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("colony/genotype")
public class GenotypeController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private GenotypeRepository genotypeRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private LabRepository labRepository;

    @GetMapping("{labId}")
    public String displayAllGenotypes(HttpServletRequest request, Model model, @PathVariable int labId, @RequestParam(required = false) int[] genotypeIds) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Lab Genotypes");
        model.addAttribute("genotype", genotypeRepository.findAll());
        model.addAttribute("lab", labRepository.findLabById(labId));

        List<Genotype> genotypes = new ArrayList<>();
        for (Genotype genotype : genotypeRepository.findAll()) {
            if (genotype.getLab() != null && genotype.getLab().getId() == labId){
                genotypes.add(genotype);
            }
        }

        labRepository.findLabById(labId).setGenotype(genotypes);

        return "colony/genotype/index";
    }

    @GetMapping("add/{labId}")
    public String displayAddGenotypeForm(HttpServletRequest request, Model model, @PathVariable int labId) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        model.addAttribute("title", "Lab Genotypes");
        model.addAttribute("genotypes", genotypeRepository.findAll());
        model.addAttribute("lab", labRepository.findLabById(labId));
        model.addAttribute(new Genotype());
        return "colony/genotype/add";
    }

    @PostMapping("add/{labId}")
    public String processAddGenotypeForm(HttpServletRequest request, @ModelAttribute Genotype newGenotype, Model model, @PathVariable int labId, Errors errors) {
        HttpSession session = request.getSession();
        User userFromSession = authenticationController.getUserFromSession(session);
        model.addAttribute("user", userFromSession);

        Genotype existingGenotype = genotypeRepository.findByName(newGenotype.getName());

        if (existingGenotype != null) {
            model.addAttribute("user", userFromSession);
            errors.rejectValue("name", "name.alreadyexists", "Already exists!");
            model.addAttribute("title", "Lab Genotypes");
            model.addAttribute("genotypes", genotypeRepository.findAll());
            return "colony/genotype/add";
        }

        List<Genotype> genotypes = new ArrayList<>();
        for (Genotype genotype : genotypeRepository.findAll()) {
            if (genotype.getLab() != null && genotype.getLab().getId() == labId){
                genotypes.add(genotype);
            }
        }

        Lab userLab = labRepository.findLabById(labId);
        newGenotype.setLab(userLab);
        genotypes.add(newGenotype);
        userLab.setGenotype(genotypes);
        genotypeRepository.save(newGenotype);

        model.addAttribute("lab", labRepository.findLabById(labId));
        String labName = labRepository.findLabById(labId).getLabName();
        model.addAttribute("genotypes", genotypes);
        return "redirect:/colony/genotype/{labId}";
    }

    @PostMapping("{labId}")
    public String processDeleteGenotypeForm(HttpServletRequest request, Model model, @PathVariable int labId, @RequestParam(required = false) int[] genotypeIds) {
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
        return "redirect:/colony/genotype/{labId}";
    }


}
