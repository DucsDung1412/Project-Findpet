package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.finder.pet.entity.Users;

@Controller
public class PageController {
    @GetMapping("/index")
    public String index(HttpSession session){
        session.removeAttribute("emailUs");
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
