package kz.springcourse.demo.controller;

import jakarta.validation.Valid;
import kz.springcourse.demo.model.Person;
import kz.springcourse.demo.model.Seller;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.service.PersonService;
import kz.springcourse.demo.service.SellerService;
import kz.springcourse.demo.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UsersDetailsService usersDetailsService;
    private final PersonService personService;
    private final SellerService sellerService;

    @Autowired
    public AuthController(UsersDetailsService usersDetailsService, PersonService personService, SellerService sellerService) {
        this.usersDetailsService = usersDetailsService;
        this.personService = personService;
        this.sellerService = sellerService;
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registration/person")
    public String registration(@ModelAttribute(name = "person") Person person){
        return "auth/registration";
    }

    @PostMapping("/registration/person")
    public String addPerson(@ModelAttribute(name = "person") @Valid Person person,
                            BindingResult bindingResult,
                            @RequestParam(name = "password") String pass,
                            @RequestParam(name = "email") String email,
                            Model model){

        if(usersDetailsService.findByEmail(email) != null){
            model.addAttribute("bademail", true);
            return "auth/registration";
        }

        if(bindingResult.hasErrors()){
            return "auth/registration";
        }

        person.setUser(usersDetailsService.register(new Users(null, email, pass, "ROLE_CLIENT")));

        personService.save(person);

        return "redirect:/auth/login";
    }

    @GetMapping("/registration/seller")
    public String registration2(@ModelAttribute(name = "seller") Seller seller){
        return "auth/registration2";
    }

    @PostMapping("/registration/seller")
    public String addSeller(@ModelAttribute(name = "seller") @Valid Seller seller,
                            BindingResult bindingResult,
                            @RequestParam(name = "password") String pass,
                            @RequestParam(name = "email") String email,
                            Model model){


        if(usersDetailsService.findByEmail(email) != null){
            model.addAttribute("bademail", true);
            return "auth/registration2";
        }

        if(bindingResult.hasErrors()){
            return "auth/registration2";
        }

        seller.setUser(usersDetailsService.register(new Users(null, email, pass, "ROLE_SELLER")));

        sellerService.save(seller);

        return "redirect:/auth/login";
    }
}
