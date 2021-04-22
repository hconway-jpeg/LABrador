package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.models.Animal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("colony")
public class AnimalController {

    @GetMapping
    public String displayAllAnimals(Model model) {
        model.addAttribute("title", "LAB_NAME Animal Colony");
        //model.addAttribute("animals", animalRepository.findAll());
        return "colony/index";
    }

    @GetMapping("add")
    public String displayAddAnimalForm(Model model) {
        model.addAttribute("title", "Add Entry");
        model.addAttribute(new Animal());
        return "colony/add";
    }

    @PostMapping("add")
    public String processAddAnimalForm(@ModelAttribute Animal newAnimal, Model model) {
        //animalRepository.save(newAnimal);
        return "redirect:";
    }

    @GetMapping("edit/${animalId}")
    public String displayEditAnimalForm(Model model, @PathVariable  int animalId) {
        //Animal animal = animalRepository.findById(animalId);
        model.addAttribute("title", "Edit Entry");
        //model.addAttribute("animal", animal);
        return "colony/edit";
    }

    @PostMapping("edit")
    public String processEditAnimalForm(@RequestParam Integer animalId, String tag) {
        //Animal editedAnimal = animalRepository.findById(animalId);
        //editedAnimal.setTag(tag);
        //etc
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteAnimalForm(Model model) {
        model.addAttribute("title", "Delete Entry");
        //model.addAttribute("animals", animalRepository.findAll());
        return "colony/delete";
    }

    @PostMapping("delete")
    public String processDeleteAnimalForm(@RequestParam(required = false) int[] animalIds) {
        if (animalIds != null) {
            for (int id : animalIds) {
                //animalRepository.deleteById(id);
            }
        }
        return "redirect:";
    }

    @GetMapping("details/{animalId}")
    public String displayAnimalDetails(Model model, @PathVariable int animalId) {
        return "colony/details";
    }

}
