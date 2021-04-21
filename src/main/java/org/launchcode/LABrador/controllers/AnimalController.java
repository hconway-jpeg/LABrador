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
    public String processAddAnimalForm(Model model) {
        return "redirect:";
    }

    @GetMapping("edit")
    public String displayEditAnimalForm(Model model) {
        model.addAttribute("title", "Edit Entry");
        return "colony/edit";
    }

    @PostMapping("edit")
    public String processEditAnimalForm(@RequestParam Integer animalId, Model model) {
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteAnimalForm(Model model) {
        model.addAttribute("title", "Delete Entry");
        return "colony/delete";
    }

    @PostMapping("delete")
    public String processDeleteAnimalForm() {
        return "redirect:";
    }

    @GetMapping("details/{animalId}")
    public String displayAnimalDetails(Model model, @PathVariable int animalId) {
        return "colony/details";
    }

}
