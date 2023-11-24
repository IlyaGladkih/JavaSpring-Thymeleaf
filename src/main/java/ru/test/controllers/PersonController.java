package ru.test.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.test.dao.PersonDao;
import ru.test.models.Person;

import java.util.List;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private PersonDao personDao;

    @Autowired
    public PersonController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping()
    public String getPersons(Model model){
        List<Person> allPersons = personDao.getAllPersons();
        model.addAttribute("persons", allPersons);
        return "persons/all";
    }

    @GetMapping("/new")
    public String addNewPerson(Model model){
        model.addAttribute("person", new Person());
        return "persons/new";
    }

    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model){
        Person person = personDao.get(id);
        model.addAttribute("person",person);
        model.addAttribute("books",personDao.getPersonsBook(id));
        return "persons/ids";
    }

    @PostMapping()
    public String addPerson(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()) return "persons/new";
        personDao.save(person);
        return "redirect:/persons";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person",personDao.get(id));
        return "persons/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editPersonPostMethod(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        personDao.personEditById(id,person);
        return "redirect:/persons";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id){
        personDao.delete(id);
        return "redirect:/persons";
    }
}
