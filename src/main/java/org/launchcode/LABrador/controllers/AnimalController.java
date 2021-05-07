package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.data.GenotypeRepository;
import org.launchcode.LABrador.models.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("colony")
public class AnimalController {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private GenotypeRepository genotypeRepository;

    @GetMapping
    public String displayAllAnimals(Model model) {
        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("animals", animalRepository.findAll());
        return "colony/index";
    }

    @GetMapping("add")
    public String displayAddAnimalForm(Model model) {
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
    public String displayEditAnimalForm(Model model, @PathVariable  int animalId) {
        model.addAttribute("title", "Edit Entry");
        model.addAttribute(animalRepository.findById(animalId).get());
        model.addAttribute("genotype", genotypeRepository.findAll());
        return "colony/edit";
    }

    @PostMapping("edit/{animalId}")
    public String processEditAnimalForm(@ModelAttribute @Valid Animal animal, Model model, Errors errors, @PathVariable int animalId) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Entry");
            return "colony/edit";
        }

        animalRepository.findById(animalId).get().setTag(animal.getTag());
        animalRepository.findById(animalId).get().setCageNumber(animal.getCageNumber());
        animalRepository.findById(animalId).get().setCageType(animal.getCageType());
        animalRepository.findById(animalId).get().setSex(animal.getSex());
        animalRepository.findById(animalId).get().setDateOfBirth(animal.getDateOfBirth());
        animalRepository.findById(animalId).get().setGenotype(animal.getGenotype());
        animalRepository.findById(animalId).get().setLitter(animal.getLitter());
        animalRepository.findById(animalId).get().setNotes(animal.getNotes());

        animalRepository.save(animalRepository.findById(animalId).get());

        return "redirect:../";
    }

    @GetMapping("delete")
    public String displayDeleteAnimalForm(Model model) {
        model.addAttribute("title", "Delete Entry");
        model.addAttribute("animals", animalRepository.findAll());
        return "colony/delete";
    }

    @PostMapping("delete")
    public String processDeleteAnimalForm(@RequestParam(required = false) int[] animalIds) {
        if (animalIds != null) {
            for (int id : animalIds) {
                animalRepository.deleteById(id);
            }
        }
        return "redirect:";
    }
}
