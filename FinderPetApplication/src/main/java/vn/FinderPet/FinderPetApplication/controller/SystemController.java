package vn.FinderPet.FinderPetApplication.controller;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.FinderPet.FinderPetApplication.service.MailService;
import vn.FinderPet.FinderPetApplication.service.OtpService;
import vn.FinderPet.FinderPetApplication.service.TwoFactorAuthPasswordsService;
import vn.FinderPet.FinderPetApplication.service.UsersService;

import java.util.concurrent.ExecutionException;

@Controller
public class SystemController {
    private OtpService otp;
    private MailService mail;
    private UsersService usersv;
    @Autowired
    private TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;
    @Autowired
    public SystemController(OtpService otp, MailService mail, UsersService usersv) {
        this.otp = otp;
        this.mail = mail;
        this.usersv = usersv;
    }

    @GetMapping("/resetotp")
    public String SendMail(HttpSession session) throws ExecutionException {
        String a=(String)session.getAttribute("emailUs");
        mail.MaxacNhan(a);
        return"redirect:/two-factor-auth";
    }

   @PostMapping("/getmail")
    public String getmail(@ModelAttribute(value = "emailUs") String emailus, HttpSession session, Model md)  throws ExecutionException {
        if(usersv.findById(emailus).isPresent()) {
            mail.MaxacNhan(emailus);
            session.setAttribute("emailUs", emailus);
            return "redirect:/two-factor-auth";
        }
        return "redirect:/forgot-password";
    }
    @PostMapping("/checkotp")
    public String getmail(@ModelAttribute(value = "so1") String so1,
                          @ModelAttribute(value = "so2") String so2,
                          @ModelAttribute(value = "so3") String so3,
                          @ModelAttribute(value = "so4") String so4,
                          @ModelAttribute(value = "so5") String so5,
                          HttpSession session) {
        String a=(String)session.getAttribute("emailUs");
        String ma=so1+so2+so3+so4+so5;
        if(otp.GetOtp(a).equals(ma)) {
            //đúng mã
            twoFactorAuthPasswordsService.sendOneTimePasswords(a);
            return "redirect:/two-factor-auth-password/";
        }else {
            System.out.println("Sai code");
            return "redirect:/two-factor-auth";
        }
    }
}
