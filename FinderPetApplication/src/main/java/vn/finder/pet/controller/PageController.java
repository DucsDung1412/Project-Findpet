package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.finder.pet.entity.Users;

@Controller
public class PageController {
    @GetMapping("/index")
    public String index(){
        return "/index";
    }

    @GetMapping("/sign-in")
    public String signIn(HttpSession session, Model model){
        Users userInfo = (Users) session.getAttribute("userLogin");
        if(userInfo != null){
            model.addAttribute("modal", true);
        }
        return "/sign-in";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model){
        Users users = new Users();
        model.addAttribute("users", users);
        return "/sign-up";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        return "/forgot-password";
    }

    @GetMapping("/two-factor-auth")
    public String getMethodName(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage, @RequestParam(value = "reset", required = false) String reset) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("reset", reset);
        return "/two-factor-auth";
    }

    @GetMapping("/pet-grid")
    public String petGrid(){
        return "/pet-grid";
    }

    @GetMapping("/two-factor-auth-password")
    public String twoFatorAuthPass() {
        return "/two-factor-auth-password";
    }
}
