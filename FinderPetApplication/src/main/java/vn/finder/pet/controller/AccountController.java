package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.DtoAddShelters;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {
    private UsersService userService;
    private AdoptService adoptService;
    private SheltersService sheltersService;
    private SponsService sponsService;
    private AnimalsService animalsService;
    private BreedService breedService;
    private TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;

    @Autowired
    public AccountController(UsersService userService, TwoFactorAuthPasswordsService twoFactorAuthPasswordsService, AdoptService adoptService, SheltersService sheltersService, SponsService sponsService, AnimalsService animalsService, BreedService breedService) {
        this.userService = userService;
        this.twoFactorAuthPasswordsService = twoFactorAuthPasswordsService;
        this.adoptService = adoptService;
        this.sheltersService = sheltersService;
        this.sponsService = sponsService;
        this.animalsService = animalsService;
        this.breedService = breedService;
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

    @GetMapping("/profile")
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
        model.addAttribute("statusActive", "profile");
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
        return "redirect:/account/profile";
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
        return "redirect:/account/profile";
    }

    @GetMapping("/wishlist")
    public String accountWishlist(Model model){
        model.addAttribute("listFavorite", this.userService.findById(getEmailLogin()).get().getListFavorites());
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        model.addAttribute("favorites", true);
        model.addAttribute("statusActive", "wishlist");
        model.addAttribute("animalsService", this.animalsService);
        return "/account-wishlist";
    }

    @GetMapping("/delete-favorite-detail")
    public String deleteFavoriteDetail(@RequestParam(value = "id") Long id){
        System.out.println(id);
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("-----");
        this.userService.deleteFavorite(id);
        return "redirect:/account/wishlist";
    }

    @GetMapping("/delete-all-favorites")
    public String deleteAllFavorites(){
        this.userService.deleteAllFavorite(getEmailLogin());
        return "redirect:/account/wishlist";
    }

    @GetMapping("/delete")
    public String accountDelete(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "delete");
        return "/account-delete";
    }

    @GetMapping("/history")
    public String accountHistory(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        model.addAttribute("listAdopt", this.adoptService.findByUsers(this.getEmailLogin(), "%", pg == 0 ? 0 : pg - 1, 6));
        model.addAttribute("totalAdopt", this.adoptService.findByUsers(this.getEmailLogin(), "%", 0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listDonate", this.sponsService.findAllByUser(pg == 0 ? 0 : pg - 1, 6, this.getEmailLogin()));
        model.addAttribute("totalDonate", this.sponsService.findAllByUser(0, Integer.MAX_VALUE, this.getEmailLogin()).getTotalElements());
        model.addAttribute("adoptService", this.adoptService);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("statusActive", "history");
        model.addAttribute("sponsService", this.sponsService);
        model.addAttribute("breedService", this.breedService);
        return "/account-history";
    }

    @GetMapping("/history/changePage")
    public String changePageAccountHistory(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/account/history";
    }

    @GetMapping("/donate")
    public String accountDonate(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        model.addAttribute("listShelter", this.sheltersService.findAll(0, Integer.MAX_VALUE));
        model.addAttribute("statusActive", "donate");
        return "/account-donate";
    }

    @GetMapping("/donate-confirm")
    public String donateConfirm(Model model, @RequestParam(value = "id") Long id){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        model.addAttribute("status", "thành công");
        model.addAttribute("spon", this.sponsService.findById(id));
        model.addAttribute("transactionId", 123456789);
        model.addAttribute("sponsService", this.sponsService);
        return "/donate-confirm";
    }

    @GetMapping("/adopt-confirm")
    public String adoptConfirm(Model model, @RequestParam(value = "id") Long id){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        model.addAttribute("adopt", this.adoptService.findById(id));
        model.addAttribute("adoptService", this.adoptService);
        model.addAttribute("animalsService", this.animalsService);
        model.addAttribute("breedService", this.breedService);
        return "/booking-confirm";
    }

    @GetMapping("/signUp-shelter")
    public String signUpShelter(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        return "/add-listing";
    }

    @GetMapping("/shelter-added")
    public String shelterAdded(Model model){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.userService.findById(this.getEmailLogin()).get());
        return "/listing-added";
    }
}



















