package vn.finder.pet.controller;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.MailService;
import vn.finder.pet.service.OtpService;
import vn.finder.pet.service.UsersService;

import java.util.concurrent.ExecutionException;

@Controller
public class SystemController {
    private OtpService otp;
    private MailService mail;
    private UsersService usersv;

    @Autowired
    public SystemController(OtpService otp, MailService mail, UsersService usersv) {
        this.otp = otp;
        this.mail = mail;
        this.usersv = usersv;
    }

    @GetMapping("/resetotp")
    public String resetotp(HttpSession session) throws ExecutionException {
        String a=(String)session.getAttribute("emailUs");
        mail.MaxacNhan(a);
        return"redirect:/two-factor-auth";
    }

    @PostMapping("/getmail")
    public String getmail(@ModelAttribute(value = "emailUs") String emailus, HttpSession session)  throws ExecutionException {
        if(usersv.findById(emailus).isPresent()) {
            mail.MaxacNhan(emailus);
            session.setAttribute("emailUs", emailus);
            return "redirect:/two-factor-auth";
        }
        return "redirect:/forgot-password";
    }

    @PostMapping("/checkotp")
    public String checkOTP(@ModelAttribute(value = "so1") String so1,
                          @ModelAttribute(value = "so2") String so2,
                          @ModelAttribute(value = "so3") String so3,
                          @ModelAttribute(value = "so4") String so4,
                          @ModelAttribute(value = "so5") String so5,
                          HttpSession session) {
        String a=(String)session.getAttribute("emailUs");
        String ma=so1+so2+so3+so4+so5;
        if(otp.GetOtp(a).equals(ma)) {
            return "redirect:/index";
        }else {
            System.out.println(2);
            return "redirect:/two-factor-auth";
        }
    }

    @PostMapping("/saveUserLoginWithOAuth2")
    public String saveUserLoginWithOAuth2(@ModelAttribute(value = "country") String country, HttpSession session){
        Users userInfo = (Users) session.getAttribute("userLogin");
        userInfo.setCountry(country);
        this.usersv.createdUser(userInfo);
        return "redirect:/index";
    }
}
