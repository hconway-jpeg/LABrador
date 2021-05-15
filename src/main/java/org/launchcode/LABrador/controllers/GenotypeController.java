package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.GenotypeRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.Genotype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

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
    public String processAddGenotypeForm(@ModelAttribute Genotype newGenotype, Model model) {
        //don't allow to repeat genotypes
        Iterable<Genotype> genotypes = genotypeRepository.findAll();
        for (Genotype genotype : genotypes) {
            if (!newGenotype.equals(genotype)) {
                genotypeRepository.save(newGenotype);
            }
        }
        //this doesn't work yet**
        return "redirect:";
    }

    @PostMapping
    public String processDeleteGenotypeForm(Model model, @RequestParam(required = false) int[] genotypeIds) {
        Iterable<Animal> animals = animalRepository.findAll();
        Genotype nullGenotype = new Genotype(null);
        genotypeRepository.save(nullGenotype);

        //check if null exists, if so don't create more.
        if (genotypeIds != null) {
            for (int id : genotypeIds) {
                for (Animal animal : animals) {
                    if (animal.genotype.getId().compareTo(id) == 0) {
                        animal.setGenotype(nullGenotype);
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
