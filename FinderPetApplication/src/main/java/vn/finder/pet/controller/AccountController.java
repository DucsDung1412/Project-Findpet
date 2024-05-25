package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.MailService;
import vn.finder.pet.service.OtpService;
import vn.finder.pet.service.TwoFactorAuthPasswordsService;
import vn.finder.pet.service.UsersService;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AccountController {
    private OtpService otpService;
    private MailService mailService;
    private UsersService userService;
    private TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Autowired
    public AccountController(OtpService otpService, MailService mailService, UsersService userService, TwoFactorAuthPasswordsService twoFactorAuthPasswordsService) {
        this.otpService = otpService;
        this.mailService = mailService;
        this.userService = userService;
        this.twoFactorAuthPasswordsService = twoFactorAuthPasswordsService;
    }

    @PostMapping("/saveUserLoginWithOAuth2")
    public String saveUserLoginWithOAuth2(@ModelAttribute(value = "country") String country, HttpSession session){
        Users userInfo = (Users) session.getAttribute("userLogin");
        userInfo.setCountry(country);
        this.userService.createdUser(userInfo);
        return "redirect:/index";
    }

    @PostMapping("/checkMail")
    public String checkMail(@ModelAttribute(value = "emailUs") String emailus, HttpSession session)  throws ExecutionException {
        if (emailus.trim().isEmpty()) {
            System.out.println("Email không được bỏ trống");
        } else {
            Matcher matcher = pattern.matcher(emailus);
            if(matcher.matches()){
                if(userService.findById(emailus).isPresent()) {
                    mailService.MaxacNhan(emailus);
                    session.setAttribute("emailUs", emailus);
                    return "redirect:/two-factor-auth";
                } else {
                    System.out.println("Email chưa được đăng ký vào hệ thống");
                }
            } else {
                System.out.println("Email không đúng định dạng");
            }
        }

        return "redirect:/forgot-password";
    }

    @PostMapping("/checkOtp")
    public String checkOtp(@ModelAttribute(value = "so1") String so1,
                          @ModelAttribute(value = "so2") String so2,
                          @ModelAttribute(value = "so3") String so3,
                          @ModelAttribute(value = "so4") String so4,
                          @ModelAttribute(value = "so5") String so5,
                          HttpSession session) {
        String a=(String)session.getAttribute("emailUs");
        String ma=so1+so2+so3+so4+so5;
        if(!ma.trim().isEmpty() && ma.length() == 5){
            if(otpService.GetOtp(a).equals(ma)) {
                //đúng mã
                twoFactorAuthPasswordsService.sendOneTimePasswords(a);
                return "redirect:/two-factor-auth-password";
            } else {
                System.out.println("Mã xác nhận không đúng");
            }
        } else {
            System.out.println("Vui lòng không để trống field nào");
        }
        return "redirect:/two-factor-auth";
    }

    @GetMapping("/resetOtp")
    public String resetOtp(HttpSession session) throws ExecutionException {
        String a=(String)session.getAttribute("emailUs");
        mailService.MaxacNhan(a);
        return"redirect:/two-factor-auth";
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
