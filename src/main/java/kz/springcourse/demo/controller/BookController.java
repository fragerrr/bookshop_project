package kz.springcourse.demo.controller;

import jakarta.validation.Valid;
import kz.springcourse.demo.model.Book;


import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.security.UsersDetails;
import kz.springcourse.demo.service.BookService;
import kz.springcourse.demo.service.PersonService;

import kz.springcourse.demo.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/book")
public class BookController {

    private Users user;

    private final SellerService sellerService;

    private final BookService bookService;


    @Autowired
    public BookController(SellerService sellerService, BookService bookService, PersonService personService) {
        this.sellerService = sellerService;
        this.bookService = bookService;
    }


    @GetMapping("/new")
    public String create(@ModelAttribute("book") Book book){
        return "book/new";
    }


    @PostMapping("/new")
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "book/new";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails users = (UsersDetails) authentication.getPrincipal();
        user = users.getUser();

        book.setOwner(sellerService.findByUserId(user.getId()));

        bookService.save(book);

        return "redirect:/home";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model){
        model.addAttribute("book", bookService.findById(id));

        return "book/edit";
    }


    @PatchMapping("/{id}/edit")
    public String update(@PathVariable(name = "id")Integer id,
                         @ModelAttribute @Valid Book book,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "book/edit";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails users = (UsersDetails) authentication.getPrincipal();
        user = users.getUser();

        book.setOwner(sellerService.findByUserId(user.getId()));
        book.setId(id);

        bookService.update(book);

        return "redirect:/home";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id){

        bookService.delete(id);
        System.out.println(id);
        return "redirect:/home";

    }

}
