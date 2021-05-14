package org.launchcode.LABrador.controllers;

import org.launchcode.LABrador.data.AnimalRepository;
import org.launchcode.LABrador.models.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    public String displayAllAnimals(Model model) {
        model.addAttribute("title", "LAB_NAME Animal Colony");
        model.addAttribute("animals", animalRepository.findAll());
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
        animalRepository.save(newAnimal);
        return "redirect:";
    }

    @GetMapping("edit/{animalId}")
    public String displayEditAnimalForm(Model model, @PathVariable int animalId) {
        model.addAttribute("title", "Edit Entry");
        model.addAttribute(animalRepository.findById(animalId).get());
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
        animalRepository.findById(animalId).get().setDateOpened(animal.getDateOpened());
        animalRepository.findById(animalId).get().setGenotypeOne(animal.getGenotypeOne());
        animalRepository.findById(animalId).get().setGenotypeTwo(animal.getGenotypeTwo());
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

    @GetMapping("tag")
    public String sortByTag(Model model) {
        model.addAttribute("title", "tag sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("tag")));
        return "colony/index";
    }

    @GetMapping("cagenumber")
    public String sortByCageNumber(Model model) {
        model.addAttribute("title", "cage number sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("cageNumber")));
        return "colony/index";
    }

    @GetMapping("type")
    public String sortByCageType(Model model) {
        model.addAttribute("title", "cage type sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("cageType")));
        return "colony/index";
    }

    @GetMapping("sex")
    public String sortBySex(Model model) {
        model.addAttribute("title", "sex sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("sex")));
        return "colony/index";
    }

    @GetMapping("dateofbirth")
    public String sortByDateOfBirth(Model model) {
        model.addAttribute("title", "date of birth sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("dateOfBirth")));
        return "colony/index";
    }


    @GetMapping("genotype1")
    public String sortBygenotype1(Model model) {
        model.addAttribute("title", "genotype one sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("genotypeOne")));
        return "colony/index";
    }

    @GetMapping("genotype2")
    public String sortBygenotype2(Model model) {
        model.addAttribute("title", "genotype two sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("genotypeTwo")));
        return "colony/index";
    }
    @GetMapping("litter")
    public String sortBLitter(Model model) {
        model.addAttribute("title", "litter sort");
        model.addAttribute("animals", animalRepository.findAll(Sort.by("litter")));
        return "colony/index";
}
}