package vn.FinderPet.FinderPetApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.FinderPet.FinderPetApplication.service.TwoFactorAuthPasswordsService;

@Controller
@RequestMapping("/two-factor-auth-password")
public class TwoFactorAuthPasswordController {
    @Autowired
    TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;

    @GetMapping("/")
    public String twoFatorAuthPass() {
        return "two-factor-auth-password";
    }

    @PostMapping("/checkPass")
    public String checkTwoFactorAuthPass(@RequestParam String passwordcode,
                                         @RequestParam String password,
                                         @RequestParam String confirmpassword) {
        if (passwordcode != null && password != null && confirmpassword != null) {
            if (twoFactorAuthPasswordsService.validateOneTimePassword(passwordcode)) {
                if (password.equals(confirmpassword)) {
                    twoFactorAuthPasswordsService.checkTwoFactorAuthPassword(password);
                    System.out.println("Đúng rồi");
                    return  "redirect:/two-factor-auth-password/";
                } else {
                    System.out.println("password không giống");
                    return "redirect:/two-factor-auth-password/";
                }
            } else {
                System.out.println("sai code");
                return "redirect:/two-factor-auth-password/";
            }
        }else{
            System.out.println("Trống field");
            return "redirect:/two-factor-auth-password/";
        }
//        return "redirect:/index";
    }

}
