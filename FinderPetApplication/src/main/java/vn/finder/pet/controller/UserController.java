package vn.finder.pet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/profile")
    public String accountProfile() {
        return "/account-profile";
    }
}
