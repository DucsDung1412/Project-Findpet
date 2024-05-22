package vn.FinderPet.FinderPetApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/index")
    public String index(){
        return "/index";
    }

    @GetMapping("/sign-in")
    public String signIn(){
        return "/sign-in";
    }

    @GetMapping("/sign-up")
    public String signUp(){
        return "/sign-up";
    }

    @GetMapping("/forgot-password")
    public String res() {
        return "/forgot-password";}

    @GetMapping("/two-factor-auth")
    public String getMethodName() {
        return "/two-factor-auth";
    }


}
