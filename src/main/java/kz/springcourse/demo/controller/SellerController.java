package kz.springcourse.demo.controller;

import jakarta.validation.Valid;
import kz.springcourse.demo.model.Seller;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.security.UsersDetails;
import kz.springcourse.demo.service.SellerService;
import kz.springcourse.demo.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller")
public class SellerController {
    private Seller seller;
    private Users user;
    private final SellerService sellerService;
    private final UsersDetailsService usersDetailsService;

    @Autowired
    public SellerController(SellerService sellerService, UsersDetailsService usersDetailsService) {
        this.sellerService = sellerService;
        this.usersDetailsService = usersDetailsService;
    }

    @GetMapping()
    public String index(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersDetails users = (UsersDetails) authentication.getPrincipal();
        user = users.getUser();

        seller = sellerService.findByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("seller", seller);

        return "seller/index";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute(name = "seller") Seller sellerToBeUpdated,
                       @RequestParam(name = "password") String pass){

        user.setPassword(pass);

        usersDetailsService.register(user);

        sellerToBeUpdated.setUser(user);

        sellerService.save(sellerToBeUpdated);

        return "redirect:/seller";
    }

    @GetMapping("/book-list")
    public String myBooks(Model model){
        model.addAttribute("bookList", seller.getBooks());

        return "seller/my-books";
    }
}
