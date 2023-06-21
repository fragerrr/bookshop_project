package kz.springcourse.demo.controller;

import kz.springcourse.demo.model.Person;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.security.UsersDetails;
import kz.springcourse.demo.service.BookService;
import kz.springcourse.demo.service.PersonService;
import kz.springcourse.demo.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/person")
public class PersonController {
    private Person person;
    private Users user;
    private final PersonService personService;
    private final BookService bookService;
    private final UsersDetailsService usersDetailsService;

    @Autowired
    public PersonController(PersonService personService, BookService bookService, UsersDetailsService usersDetailsService) {
        this.personService = personService;
        this.bookService = bookService;
        this.usersDetailsService = usersDetailsService;
    }

    @GetMapping()
    public String index(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersDetails users = (UsersDetails) authentication.getPrincipal();
        user = users.getUser();

        person = personService.findByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("person", person);

        return "person/index";
    }


    @PostMapping("/edit")
    public String edit(@ModelAttribute(name = "person") Person personToBeUpdated,
                       @RequestParam(name = "password") String pass){

        user.setPassword(pass);

        usersDetailsService.register(user);

        personToBeUpdated.setUser(user);

        personService.save(personToBeUpdated);

        return "redirect:/person";
    }

    @GetMapping("/book-list")
    public String bookList(Model model){

        model.addAttribute("bookList", bookService.findAll());

        return "person/all-books";
    }



    @GetMapping("/search-book")
    public String search(@RequestParam("name") String name,
                         Model model){

        model.addAttribute("bookList", bookService.getBooksBySearch(name));

        return "person/all-books";
    }
}
