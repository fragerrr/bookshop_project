package kz.springcourse.demo.controller;

import kz.springcourse.demo.model.Book;
import kz.springcourse.demo.model.Person;
import kz.springcourse.demo.model.Seller;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.security.UsersDetails;
import kz.springcourse.demo.service.BookService;
import kz.springcourse.demo.service.PersonService;
import kz.springcourse.demo.service.SellerService;
import kz.springcourse.demo.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private Users user;
    private final BookService bookService;
    private final PersonService personService;
    private final SellerService sellerService;
    private final UsersDetailsService usersDetailsService;

    @Autowired
    public AdminController(BookService bookService, PersonService personService, SellerService sellerService, UsersDetailsService usersDetailsService) {
        this.bookService = bookService;
        this.personService = personService;
        this.sellerService = sellerService;
        this.usersDetailsService = usersDetailsService;
    }

    @GetMapping
    public String index(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        user = usersDetails.getUser();

        model.addAttribute("admin", user);

        return "admin/index";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam(name = "password") String pass){
        user.setPassword(pass);

        usersDetailsService.register(user);

        return "redirect:/person";
    }

    @GetMapping("/person-list")
    public String personList(Model model){
        model.addAttribute("personList", personService.findAll());

        return "admin/person-list";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id){
        Person person = personService.findByUserId(id);
        Seller seller = sellerService.findByUserId(id);
        if(person!=null){
            personService.delete(person.getId());
            usersDetailsService.delete(usersDetailsService.findById(id));
            return "redirect:/admin/person-list";
        }
        if(seller!=null){
            sellerService.delete(seller.getId());
            usersDetailsService.delete(usersDetailsService.findById(id));
            return "redirect:/admin/seller-list";
        }

        return "redirect:/home";
    }

    @GetMapping("/seller-list")
    public String sellerList(Model model){
        model.addAttribute("sellerList", sellerService.findAll());

        return "admin/seller-list";
    }

    @GetMapping("/{id}/books")
    public String sellerBooks(@PathVariable(name = "id") Integer id,
                              Model model){
        Seller seller = sellerService.findById(id);

        model.addAttribute("bookList", seller.getBooks());

        return "admin/seller-book";
    }


    @GetMapping("/all-books")
    public String getBooks(Model model){
        model.addAttribute("bookList", bookService.findAll());

        return "admin/seller-book";
    }
}
