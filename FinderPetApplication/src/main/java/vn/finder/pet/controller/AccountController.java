package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.MailService;
import vn.finder.pet.service.OtpService;
import vn.finder.pet.service.TwoFactorAuthPasswordsService;
import vn.finder.pet.service.UsersService;

import java.sql.Date;
import java.time.LocalDate;
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
        session.removeAttribute("userLogin");
        return "redirect:/index";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("users") Users users, @RequestParam(value = "cfPassword") String cfPassword, RedirectAttributes redirectAtb){
        int valid = 0;
        if (users.getFirstName().isEmpty()) {
            redirectAtb.addAttribute("errorMessageFirstName", "Thiếu thông tin");
            valid++;
        }

        if (users.getLastName().isEmpty()) {
            redirectAtb.addAttribute("errorMessageLastName", "Thiếu thông tin");
            valid++;
        }

        if (users.getUserName().isEmpty()) {
            redirectAtb.addAttribute("errorMessageUserName", "Thiếu thông tin");
            valid++;
        } else {
            if (!userService.isValidEmailAddress(users.getUserName())) {
                redirectAtb.addAttribute("errorMessageUserName", "Email không hợp lệ");
                valid++;
            } else {
                if (userService.isUserExists(users.getUserName())) {
                    redirectAtb.addAttribute("errorMessageUserName", "Username đã tồn tại");
                    valid++;
                }
            }
        }

        if (users.getPassword().isEmpty()) {
            redirectAtb.addAttribute("errorMessagePassword", "Thiếu thông tin");
            valid++;
        } else {
            if (!userService.isValidPassword(users.getPassword())) {
                redirectAtb.addAttribute("errorMessagePassword", "Mật khẩu không hợp lệ");
                valid++;
            }
        }

        if (cfPassword.isEmpty()) {
            redirectAtb.addAttribute("errorMessageConfirmPassword", "Thiếu thông tin");
            valid++;
        } else {
            if (!users.getPassword().equals(cfPassword)) {
                redirectAtb.addAttribute("errorMessageConfirmPassword", "Mật khẩu không khớp");
                valid++;
            }
        }

        if (users.getCountry().isEmpty()) {
            redirectAtb.addAttribute("errorMessageCountry", "Thiếu thông tin");
            if (users.getCountry().equals("Select Country")) {
                redirectAtb.addAttribute("errorMessageCountry", "Thiếu thông tin");
            }
            valid++;
        }

        if(valid == 0){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String enCode = bCryptPasswordEncoder.encode(users.getPassword());
            users.setPassword("{bcrypt}"+enCode);
            users.setEnabled(true);
            users.setCreatedDate(Date.valueOf(LocalDate.now()));
            this.userService.createdUser(users);

            mailService.MaxacNhan(users.getUserName());

            return "redirect:/index";
        }
        return "redirect:/sign-up";
    }

    @PostMapping("/checkMail")
    public String checkMail(@ModelAttribute(value = "emailUs") String emailus, HttpSession session, RedirectAttributes ra)  throws ExecutionException {
        if (emailus.trim().isEmpty()) {
            ra.addAttribute("errorMessage","Email không được bỏ trống");
        } else {
            Matcher matcher = pattern.matcher(emailus);
            if(matcher.matches()){
                if(userService.findById(emailus).isPresent()) {
                    mailService.MaxacNhan(emailus);
                    session.setAttribute("emailUs", emailus);
                    ra.addAttribute("email", emailus);
                    return "redirect:/two-factor-auth";
                } else {
                    ra.addAttribute("errorMessage","Email chưa được đăng ký vào hệ thống");
                }
            } else {
                ra.addAttribute("errorMessage", "Email không đúng định dạng");
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
                          HttpSession session, RedirectAttributes ra) {
        String a=(String)session.getAttribute("emailUs");
        String ma=so1+so2+so3+so4+so5;
        if(!ma.trim().isEmpty() && ma.length() == 5){
            if(otpService.GetOtp(a).equals(ma)) {
                //đúng mã
                twoFactorAuthPasswordsService.sendOneTimePasswords(a);
                ra.addAttribute("email", session.getAttribute("emailUs"));
                return "redirect:/two-factor-auth-password";
            } else {
                ra.addAttribute("errorMessage","Mã xác nhận không đúng");
            }
        } else {
            ra.addAttribute("errorMessage","Vui lòng không để trống field nào");
        }
        return "redirect:/two-factor-auth";
    }

    @GetMapping("/resetOtp")
    public String resetOtp(HttpSession session, RedirectAttributes ra) throws ExecutionException {
        String emailus = (String)session.getAttribute("emailUs");
        mailService.MaxacNhan(emailus);
        ra.addAttribute("reset", true);
        ra.addAttribute("email", emailus);
        return"redirect:/two-factor-auth";
    }

    @PostMapping("/checkPass")
    public String checkTwoFactorAuthPass(@RequestParam String passwordCode,
                                         @RequestParam String password,
                                         @RequestParam String confirmPassword,
                                         RedirectAttributes ra, HttpSession session) {
        int valid = 0;
        //Trống field input
        if (passwordCode.trim().isEmpty()) {
            ra.addAttribute("errorMessagePasswordCode", "Trống field");
        } else {
            //Kiểm tra password code
            if (!twoFactorAuthPasswordsService.validateOneTimePassword(passwordCode)) {
                ra.addAttribute("errorMessagePasswordCode", "Sai code");
            } else {
                valid++;
            }
        }

        if (password.trim().isEmpty()) {
            ra.addAttribute("errorMessagePassword", "Trống field");
        } else {
            // Kiểm tra định dạng và khác nhau với password cũ > Thực hiện đổi password
            if (twoFactorAuthPasswordsService.checkTwoFactorAuthPassword(password)) {
                valid++;
            } else {
                ra.addAttribute("errorMessagePassword", "Sai định dạng password");
            }
        }

        if (confirmPassword.trim().isEmpty()) {
            ra.addAttribute("errorMessageConfirmPassword", "Trống field");
        } else {
            //Kiểm tra password với password confirm
            if (!password.equals(confirmPassword)) {
                ra.addAttribute("errorMessageConfirmPassword", "Mật khẩu không trùng khớp với xác nhận mật khẩu");
            } else {
                valid++;
            }
        }

        if (valid == 3) {
            ra.addAttribute("messageComplete", "Đổi password thành công");
            String emailus = (String)session.getAttribute("emailUs");
            this.twoFactorAuthPasswordsService.updatePassword(emailus, password.trim());
            return "redirect:/sign-in";
        }
        return "redirect:/two-factor-auth-password";
    }
}
