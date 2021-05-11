package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.GenotypeRepository;
import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.Genotype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute(new Genotype());
        return "colony/genotype/add";
    }

    @PostMapping("add")
    public String processAddGenotypeForm(@ModelAttribute Genotype newGenotype, Model model) {
        genotypeRepository.save(newGenotype);
        return "redirect:";
    }

//    @GetMapping("edit")
//    @PostMapping("edit")

    @PostMapping
    public String processDeleteGenotypeForm(Model model, @RequestParam(required = false) int[] genotypeIds) {
        if (genotypeIds != null) {
            for (int id : genotypeIds) {
                //need to delete from animal first! will keep throwing error until then.

                genotypeRepository.deleteById(id);
            }
        }
        model.addAttribute("title", "Lab Genotypes");
        model.addAttribute("genotype", genotypeRepository.findAll());
        return "colony/genotype/index";
    }



}
