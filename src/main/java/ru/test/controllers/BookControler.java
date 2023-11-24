package ru.test.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.test.dao.BookDao;
import ru.test.dao.PersonDao;
import ru.test.models.Book;
import ru.test.models.Person;


@Controller
@RequestMapping("/books")
public class BookControler {

    private BookDao bookDao;

    private PersonDao personDao;

    @Autowired
    public BookControler(BookDao bookDao, PersonDao personDao) {
        this.bookDao = bookDao;
        this.personDao = personDao;
    }

    @GetMapping
    public String getAllBooks(Model model){
        model.addAttribute("books",bookDao.all());
        return "books/all";
    }

    @GetMapping("/new")
    public String addNewBook(Model model){
        model.addAttribute("book", new Book());
        return  "books/new";
    }

    @PostMapping("/new")
    public String addNewBookPost(@ModelAttribute("book") @Valid Book book,
                                 BindingResult bindingResult){
        if(bindingResult.hasErrors()) return "books/new";

        bookDao.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id, Model model, @ModelAttribute("per") Person person){
        model.addAttribute("book",bookDao.getWithOwner(id));
        model.addAttribute("persons",personDao.getAllPersons());
        return "books/ids";
    }

    @PostMapping("/{id}")
    public String pushBook(@PathVariable("id") int id, @ModelAttribute("per") Person person){;
        bookDao.setPerson(id,person.getPerson_id());
        System.out.println(person.getPerson_id());
        return "redirect:/books/"+id;
    }

    @GetMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id){
        bookDao.releaseBook(id);
        System.out.println(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/{id}/edit")
    public String editBookGet(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookDao.get(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editBookById(@PathVariable("id") int id, @ModelAttribute("book") Book book){
        bookDao.edit(id,book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id){
        bookDao.delete(id);
        return "redirect:/books";
    }
}
