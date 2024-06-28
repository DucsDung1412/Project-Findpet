package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AccountController {
    private OtpService otpService;
    private MailService mailService;
    private UsersService userService;
    private AdoptService adoptService;
    private TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Autowired
    public AccountController(OtpService otpService, MailService mailService, UsersService userService, TwoFactorAuthPasswordsService twoFactorAuthPasswordsService, AdoptService adoptService) {
        this.otpService = otpService;
        this.mailService = mailService;
        this.userService = userService;
        this.twoFactorAuthPasswordsService = twoFactorAuthPasswordsService;
        this.adoptService = adoptService;
    }

    public String getEmailLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        if (authentication != null && !authentication.getName().equals("anonymousUser")) {
            if(this.userService.findById(authentication.getName()).isEmpty()){
                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                email = principal.getAttribute("email");
            } else {
                email = authentication.getName();
            }
        } else {
            return null;
        }
        return email;
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
    public String signUp(@ModelAttribute("users") Users users, @RequestParam(value = "cfPassword") String cfPassword, RedirectAttributes redirectAtb, HttpSession session){
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
//            this.userService.createdUser(users);

            mailService.MaxacNhan(users.getUserName());

            session.setAttribute("username",users.getUserName());
            session.setAttribute("userByName",users);

            return "redirect:/two-factor-auth";
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
        String userFogotPw = (String)session.getAttribute("emailUs");
        String userSignUp = (String) session.getAttribute("username");
        Users users= (Users) session.getAttribute("userByName");
        String ma=so1+so2+so3+so4+so5;
        if(!ma.trim().isEmpty() && ma.length() == 5){
            if(userFogotPw!=null) {
                if ((otpService.GetOtp(userFogotPw).equals(ma))) {
                    //đúng mã
                    twoFactorAuthPasswordsService.sendOneTimePasswords(userFogotPw);
                    ra.addAttribute("email", session.getAttribute("emailUs"));
                    return "redirect:/two-factor-auth-password";
                } else {
                    ra.addAttribute("errorMessage", "Mã xác nhận không đúng");
                }
            }if(userSignUp!=null){
                if(otpService.GetOtp(userSignUp).equals(ma)) {
                    //đúng Mã
                    this.userService.createdUser(users);
                    return "redirect:/index";
                }
            }


        } else {
            ra.addAttribute("errorMessage","Vui lòng không để trống field nào");
        }
        return "redirect:/two-factor-auth";
    }

    @GetMapping("/resetOtp")
    public String resetOtp(HttpSession session, RedirectAttributes ra) throws ExecutionException {
        String emailus = (String)session.getAttribute("emailUs");
        String userSignUp = (String) session.getAttribute("username");
        if(emailus!=null){
            mailService.MaxacNhan(emailus);
            ra.addAttribute("reset", true);
            ra.addAttribute("email", emailus);
        }else if(userSignUp!=null){
            mailService.MaxacNhan(userSignUp);
            ra.addAttribute("reset", true);
            ra.addAttribute("email", emailus);
        }
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

    @GetMapping("/account-profile")
    public String accessAccountProfile(Model model
            , @RequestParam(value = "firstNameInValid", required = false) String firstNameInValid
            , @RequestParam(value = "lastNameInValid", required = false) String lastNameInValid
            , @RequestParam(value = "locationInValid", required = false) String locationInValid
            , @RequestParam(value = "errorMessagePasswordOld", required = false) String errorMessagePasswordOld
            , @RequestParam(value = "errorMessagePasswordNew", required = false) String errorMessagePasswordNew
            , @RequestParam(value = "errorMessageConfirmPassword", required = false) String errorMessageConfirmPassword
            , @RequestParam(value = "messageComplete", required = false) String messageComplete){
        Users users = this.userService.findById(getEmailLogin()).get();
        users.setPassword("");
        model.addAttribute("user", users);
        model.addAttribute("firstNameInValid", firstNameInValid);
        model.addAttribute("lastNameInValid", lastNameInValid);
        model.addAttribute("locationInValid", locationInValid);
        model.addAttribute("errorMessagePasswordOld", errorMessagePasswordOld);
        model.addAttribute("errorMessagePasswordNew", errorMessagePasswordNew);
        model.addAttribute("errorMessageConfirmPassword", errorMessageConfirmPassword);
        model.addAttribute("messageComplete", messageComplete);
        return "/account-profile";
    }

    @GetMapping("/change-info-user")
    public String changeInfoUser(RedirectAttributes redirectAttributes, @RequestParam(value = "country") String country, @ModelAttribute(value = "user") Users user){
        user.setUserName(getEmailLogin());
        int i = 0;
        if(user.getFirstName() == null){
            redirectAttributes.addAttribute("firstNameInValid", "Không được để trống");
            i++;
        }
        if(user.getLastName() == null){
            redirectAttributes.addAttribute("lastNameInValid", "Không được để trống");
            i++;
        }
        if(country == null){
            redirectAttributes.addAttribute("locationInValid", "Không được để trống");
            i++;
        }
        if(i == 0){
            if(!this.userService.findById(user.getUserName()).isEmpty()){
                user.setCountry(country);
                user.setCreatedDate(this.userService.findById(user.getUserName()).get().getCreatedDate());
                user.setEnabled(this.userService.findById(user.getUserName()).get().getEnabled());
                user.setPassword(this.userService.findById(user.getUserName()).get().getPassword());
                user.setAvatar(this.userService.findById(user.getUserName()).get().getAvatar());
                this.userService.changeInfoUser(user);
                redirectAttributes.addAttribute("messageComplete", "Đổi thông tin thành công");
            }
        } else {
            redirectAttributes.addAttribute("messageComplete", "Đổi thông tin thất bại");
        }
        return "redirect:/account-profile";
    }

    @PostMapping("/change-password-user")
    public String changePasswordUser(RedirectAttributes redirectAttributes, @RequestParam(value = "passOld") String passOld, @RequestParam(value = "passNew") String passNew, @RequestParam(value = "cfPassNew") String cfPassNew){
        Optional<Users> users = this.userService.findById(getEmailLogin());
        if(!users.isEmpty()){
            int valid = 0;
            //Trống field input
            if (passOld.trim().isEmpty()) {
                redirectAttributes.addAttribute("errorMessagePasswordOld", "Trống field");
            } else {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                bCryptPasswordEncoder.matches(passOld, users.get().getPassword());

                if(bCryptPasswordEncoder.matches(passOld, users.get().getPassword().substring(8))){
                    valid++;
                } else {
                    redirectAttributes.addAttribute("errorMessagePasswordOld", "Sai pass old");
                }
            }

            if (passNew.trim().isEmpty()) {
                redirectAttributes.addAttribute("errorMessagePasswordNew", "Trống field");
            } else {
                // Kiểm tra định dạng và khác nhau với password cũ > Thực hiện đổi password
                if (twoFactorAuthPasswordsService.checkTwoFactorAuthPassword(passNew)) {
                    valid++;
                } else {
                    redirectAttributes.addAttribute("errorMessagePasswordNew", "Sai định dạng password");
                }
            }

            if (cfPassNew.trim().isEmpty()) {
                redirectAttributes.addAttribute("errorMessageConfirmPassword", "Trống field");
            } else {
                if (!passNew.equals(cfPassNew)) {
                    redirectAttributes.addAttribute("errorMessageConfirmPassword", "Mật khẩu không trùng khớp với xác nhận mật khẩu");
                } else {
                    valid++;
                }
            }

            if (valid == 3) {
                redirectAttributes.addAttribute("messageComplete", "Đổi password thành công");
                this.twoFactorAuthPasswordsService.updatePassword(getEmailLogin(), passNew.trim());
            } else {
                redirectAttributes.addAttribute("messageComplete", "Đổi password thất bại");
            }
        }
        return "redirect:/account-profile";
    }

    @GetMapping("/account-wishlist")
    public String accountWishlist(Model model){
        model.addAttribute("listFavorite", this.userService.findById(getEmailLogin()).get().getListFavorites());
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        return "/account-wishlist";
    }

    @GetMapping("/delete-favorite-detail")
    public String deleteFavoriteDetail(@RequestParam(value = "id") Long id){
        this.userService.deleteFavorite(id);
        return "redirect:/account-wishlist";
    }

    @GetMapping("/delete-all-favorites")
    public String deleteAllFavorites(){
        this.userService.deleteAllFavorite(getEmailLogin());
        return "redirect:/account-wishlist";
    }

    @GetMapping("/account-delete")
    public String accountDelete(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        return "/account-delete";
    }

    @GetMapping("/account-notify")
    public String accountNotify(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        model.addAttribute("listNotify", this.adoptService.findByUsers(this.getEmailLogin(), pg == 0 ? 0 : pg - 1, 10));
        model.addAttribute("page", pg == 0 ? 1 : pg);
        return "/account-notify";
    }

    @GetMapping("/changePage-accountNotify")
    public String changePageAccountNotify(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/account-notify";
    }
}
