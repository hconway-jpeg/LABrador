package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.GenotypeRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.Genotype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("colony/genotype")
public class GenotypeController {

    @Autowired
    private GenotypeRepository genotypeRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @GetMapping
    public String displayAllGenotypes(Model model, @RequestParam(required = false) int[] genotypeIds) {
        model.addAttribute("title", "Lab Genotypes");
        model.addAttribute("genotype", genotypeRepository.findAll());
        return "colony/genotype/index";
    }

    @GetMapping("add")
    public String displayAddGenotypeForm(Model model) {
        model.addAttribute("title", "Lab Genotypes");
        model.addAttribute("genotypes", genotypeRepository.findAll());
        model.addAttribute(new Genotype());
        return "colony/genotype/add";
    }

    @PostMapping("add")
    public String processAddGenotypeForm(@ModelAttribute Genotype newGenotype, Model model, Errors errors) {
        Genotype existingGenotype = genotypeRepository.findByName(newGenotype.getName());

        if (existingGenotype != null) {
            errors.rejectValue("name", "name.alreadyexists", "Already exists!");
            model.addAttribute("title", "Lab Genotypes");
            model.addAttribute("genotypes", genotypeRepository.findAll());
            return "colony/genotype/add";
        }
        genotypeRepository.save(newGenotype);
        return "redirect:";
    }

    @PostMapping
    public String processDeleteGenotypeForm(Model model, @RequestParam(required = false) int[] genotypeIds) {
        Iterable<Animal> animals = animalRepository.findAll();
        String blankGenotype = genotypeRepository.findByName("").getName();

        if (genotypeIds != null) {
            for (int id : genotypeIds) {
                for (Animal animal : animals) {
                    if (animal.genotype.getId().compareTo(id) == 0) {
                        animal.genotype.setName(blankGenotype);
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
