package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.Adopt;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private SheltersService sheltersService;
    private UsersService usersService;
    private AnimalsService animalsService;
    private AdoptService adoptService;
    private SponsService sponsService;

    @Autowired
    public AdminController(SheltersService sheltersService, UsersService usersService, AnimalsService animalsService, AdoptService adoptService, SponsService sponsService) {
        this.sheltersService = sheltersService;
        this.usersService = usersService;
        this.animalsService = animalsService;
        this.adoptService = adoptService;
        this.sponsService = sponsService;
    }

    public String getEmailLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        if (authentication != null && !authentication.getName().equals("anonymousUser")) {
            if(this.usersService.findById(authentication.getName()).isEmpty()){
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

    @GetMapping("/dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("totalPending", this.sheltersService.findByShelterStatus("Awaiting").size());
        model.addAttribute("listTotalPending", this.sheltersService.findByShelterStatus("Awaiting"));

        model.addAttribute("totalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", 0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", 0, 6).stream());

        model.addAttribute("totalUsers", this.usersService.findAll(0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalUsers", this.usersService.findAll(0, 7).stream());

        model.addAttribute("listSpon", this.sponsService.findAllToPage(0, 6));

        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "dashboard");
        return "/admin-dashboard";
    }

    @GetMapping("/dashboard/accept-shelter")
    public String dashboardAcceptShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Opening");
        redirectAttributes.addAttribute("page", 0);
        adoptService.sendMailToAgree(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard/remove-shelter")
    public String dashboardRefuseShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Canceled");
        redirectAttributes.addAttribute("page", 0);
        adoptService.sendMailToDisable(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/shelter-list")
    public String adminShelterList(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", pg == 0 ? 0 : pg - 1, 10));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "shelter");
        return "/admin-shelter-list";
    }

    @GetMapping("/shelter-list/changePage")
    public String changePageAdminShelter(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin/shelter-list";
    }

    @GetMapping("/shelter-detail")
    public String adminShelterDetail(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "id") Long id){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        ArrayList<List<String>> listOpen = new ArrayList<>();
        Arrays.stream(this.sheltersService.findById(id).getShelterInfoOperatingTime().split(", ")).forEach(e -> {
            listOpen.add(Arrays.stream(e.split(": ")).toList());
        });
        model.addAttribute("listOpen", listOpen);
        model.addAttribute("shelter", this.sheltersService.findById(id));
        model.addAttribute("listAnimal", this.animalsService.findAllPet(this.sheltersService.findById(id).getUsers().getUserName(), pg == 0 ? 0 : pg - 1, 8));
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "shelter");
        return "/admin-shelter-detail";
    }

    @GetMapping("/shelter-detail/changePage")
    public String changePageAdminShelterDetail(@RequestParam("page") int page, @RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin/shelter-detail";
    }

    @GetMapping("/regist-list")
    public String adminRegistList(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "status", required = false) String status){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        if(status == null){
            model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining("%",  pg == 0 ? 0 : pg - 1, 6));
        } else {
            model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining(status,  pg == 0 ? 0 : pg - 1, 6));
        }
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("status", status);
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "regist");
        return "/admin-regist-list";
    }

    @GetMapping("/regist-list/changePage")
    public String changePageAdminRegistList(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin/regist-list";
    }

    @GetMapping("/regist-list/accept-shelter")
    public String acceptShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Opening");
        redirectAttributes.addAttribute("page", 0);
        adoptService.sendMailToAgree(id);
        return "redirect:/admin/regist-list";
    }

    @GetMapping("/regist-list/remove-shelter")
    public String refuseShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Canceled");
        redirectAttributes.addAttribute("page", 0);
        adoptService.sendMailToDisable(id);
        return "redirect:/admin/regist-list";
    }

    @GetMapping("/user-list")
    public String adminUserList(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listTotalUsers", this.usersService.findAll(pg == 0 ? 0 : pg - 1, 12));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "user");
        return "/admin-user-list";
    }

    @GetMapping("/user-list/changePage")
    public String changePageAdminUser(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin/user-list";
    }

    @GetMapping("/user-detail")
    public String adminUserDetail(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "changePassword", required = false) Boolean changePassword, @RequestParam(value = "id") String userName){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("userDetail", this.usersService.findById(userName).get());
        model.addAttribute("page", pg == 0 ? 1 : pg);
        int i = 0;
        for(Adopt a : this.usersService.findById(userName).get().getListAdopt()){
            if(a.getAdopt_status().equals("Cancel")){
                i++;
            }
        }
        model.addAttribute("totalCanceled", i);
        model.addAttribute("listAdopt", this.adoptService.findByUsers(userName, "%", pg == 0 ? 0 : pg - 1, 1));
        if(changePassword != null){
            model.addAttribute("changePassword", changePassword ? "thành công" : "thất bại");
        }
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "user");
        return "/admin-user-detail";
    }

    @GetMapping("/user-detail/changePage")
    public String changePageAdminUserDetail(@RequestParam("page") int page, @RequestParam("id") String id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin/user-detail";
    }

    @PostMapping("/user-detail/resetPassword")
    public String resetPassword(@RequestParam("id") String id, RedirectAttributes redirectAttributes, @RequestParam("password") String password){
        redirectAttributes.addAttribute("id", id);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String enCode = bCryptPasswordEncoder.encode(password);
        Users users = this.usersService.findById(id).get();
        users.setPassword("{bcrypt}"+enCode);
        redirectAttributes.addAttribute("changePassword", this.usersService.changeInfoUser(users));
        return "redirect:/admin/user-detail";
    }

    @GetMapping("/user-donate")
    public String adminUserDonate(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listSpon", this.sponsService.findAllToPage(pg == 0 ? 0 : pg - 1, 10));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "donate");
        return "/admin-user-donate";
    }

    @GetMapping("/user-donate/changePage")
    public String changePageAdminDonate(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin/user-donate";
    }

    @GetMapping("/donate-detail")
    public String adminUserDonateDetail(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "id") Long id){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        Users users = this.sponsService.findById(id).getUsers();
        model.addAttribute("users", users);
        model.addAttribute("id", id);
        model.addAttribute("listSpon", this.sponsService.findAllByUser(pg == 0 ? 0 : pg - 1, 10, users.getUserName()));
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("statusActive", "donate");
        return "/admin-user-donate-detail";
    }

    @GetMapping("/donate-detail/changePage")
    public String changePageAdminDonateDetail(@RequestParam("page") int page, @RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin/donate-detail";
    }
}
