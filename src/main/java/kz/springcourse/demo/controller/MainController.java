package kz.springcourse.demo.controller;

import jakarta.validation.Valid;
import kz.springcourse.demo.model.Book;
import kz.springcourse.demo.model.Person;

import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.security.UsersDetails;
import kz.springcourse.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
    private Users user;


    @GetMapping("/home")
    public String index(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersDetails users = (UsersDetails) authentication.getPrincipal();
        user = users.getUser();

        if(user.getRole().equalsIgnoreCase("ROLE_ADMIN")){
            return "redirect:/admin";
        } if(user.getRole().equalsIgnoreCase("ROLE_SELLER")){
            return "redirect:/seller";
        }else{
            return "redirect:/person";
        }
    }

}
