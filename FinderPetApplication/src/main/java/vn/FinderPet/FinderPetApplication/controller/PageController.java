package vn.FinderPet.FinderPetApplication.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.FinderPet.FinderPetApplication.entity.UserInfo;

@Controller
public class PageController {
    @GetMapping("/index")
    public String index(){
        return "/index";
    }

    @GetMapping("/sign-in")
    public String signIn(HttpSession session, Model model){
        UserInfo userInfo = (UserInfo) session.getAttribute("userLogin");
        if(userInfo != null){
            model.addAttribute("modal", true);
        }
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
