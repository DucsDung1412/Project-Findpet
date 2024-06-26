package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.AnimalsService;
import vn.finder.pet.service.FavoritesService;
import vn.finder.pet.service.UsersService;

import java.util.Optional;

@Controller
public class PageController {
    private UsersService usersService;
    private FavoritesService favoritesService;
    private AnimalsService animalsService;

    @Autowired
    public PageController(UsersService userService, FavoritesService favoritesService, AnimalsService animalsService) {
        this.usersService = userService;
        this.favoritesService = favoritesService;
        this.animalsService = animalsService;
    }

    public String getEmailLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        if (authentication != null && !authentication.getName().equals("anonymousUser")) {
            if(this.usersService.findById(authentication.getName()).isEmpty()){
                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                email = principal.getAttribute("email");
            } else {
                email = authentication.getName();
            }
        } else {
            return null;
        }
        return email;
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model){
        session.removeAttribute("emailUs");
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("listFavorite", this.favoritesService.findAll(0,4).stream().toList());
        if(this.getEmailLogin() == null){
            model.addAttribute("listExplore", this.animalsService.findRandom(0, 12).stream().toList());
        } else {
            Users users = this.usersService.findById(this.getEmailLogin()).get();
            model.addAttribute("listExplore", this.animalsService.findByShelterAddressOrderByCustom(0, 12, users.getCountry()).stream().toList());
        }
        return "/index";
    }

    @GetMapping("/sign-in")
    public String signIn(HttpSession session, Model model, @RequestParam(value = "messageComplete", required = false) String messageComplete){
        session.removeAttribute("emailUs");
        Users userInfo = (Users) session.getAttribute("userLogin");
        if(userInfo != null){
            model.addAttribute("modal", true);
        }
        model.addAttribute("messageComplete", messageComplete);
        return "/sign-in";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model, HttpSession session,
                         @RequestParam(value = "errorMessageFirstName", required = false) String errorMessageFirstName,
                         @RequestParam(value = "errorMessageLastName", required = false) String errorMessageLastName,
                         @RequestParam(value = "errorMessageUserName", required = false) String errorMessageUserName,
                         @RequestParam(value = "errorMessagePassword", required = false) String errorMessagePassword,
                         @RequestParam(value = "errorMessageConfirmPassword", required = false) String errorMessageConfirmPassword,
                         @RequestParam(value = "errorMessageCountry", required = false) String errorMessageCountry){
        session.removeAttribute("emailUs");
        Users users = new Users();
        model.addAttribute("users", users);
        model.addAttribute("errorMessageFirstName", errorMessageFirstName);
        model.addAttribute("errorMessageLastName", errorMessageLastName);
        model.addAttribute("errorMessageUserName", errorMessageUserName);
        model.addAttribute("errorMessagePassword", errorMessagePassword);
        model.addAttribute("errorMessageConfirmPassword", errorMessageConfirmPassword);
        model.addAttribute("errorMessageCountry", errorMessageCountry);
        return "/sign-up";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        return "/forgot-password";
    }

    @GetMapping("/two-factor-auth")
    public String getMethodName(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage, @RequestParam(value = "reset", required = false) String reset, @RequestParam(value = "pagePrev", required = false) String pagePrev, @RequestParam(value = "email", required = false) String email) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("reset", reset);
        model.addAttribute("email", email);
        System.out.println(pagePrev);
        return "/two-factor-auth";
    }

    @GetMapping("/two-factor-auth-password")
    public String twoFatorAuthPass(Model model, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "errorMessagePasswordCode", required = false) String errorMessagePasswordCode, @RequestParam(value = "errorMessagePassword", required = false) String errorMessagePassword, @RequestParam(value = "errorMessageConfirmPassword", required = false) String errorMessageConfirmPassword) {
        model.addAttribute("email", email);
        model.addAttribute("errorMessagePasswordCode", errorMessagePasswordCode);
        model.addAttribute("errorMessagePassword", errorMessagePassword);
        model.addAttribute("errorMessageConfirmPassword", errorMessageConfirmPassword);
        return "/two-factor-auth-password";
    }
}
