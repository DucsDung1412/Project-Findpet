package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.Adopt;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
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

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("totalPending", this.sheltersService.findByShelterStatus("Awaiting").size());
        model.addAttribute("listTotalPending", this.sheltersService.findByShelterStatus("Awaiting"));

        model.addAttribute("totalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", 0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", 0, 6).stream());

        model.addAttribute("totalUsers", this.usersService.findAll(0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalUsers", this.usersService.findAll(0, 7).stream());

        return "/admin-dashboard";
    }

    @GetMapping("/admin-shelter-list")
    public String adminShelterList(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listTotalShelters", this.sheltersService.findSheltersByStatusContaining("Opening", pg == 0 ? 0 : pg - 1, 10));
        return "/admin-shelter-list";
    }

    @GetMapping("/changePage-adminShelter")
    public String changePageAdminShelter(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin-shelter-list";
    }

    @GetMapping("/admin-shelter-detail")
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
        return "/admin-shelter-detail";
    }

    @GetMapping("/changePage-adminShelterDetail")
    public String changePageAdminShelterDetail(@RequestParam("page") int page, @RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin-shelter-detail";
    }

    @GetMapping("/admin-user-list")
    public String adminUserList(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listTotalUsers", this.usersService.findAll(pg == 0 ? 0 : pg - 1, 12));
        return "/admin-user-list";
    }

    @GetMapping("/changePage-adminUser")
    public String changePageAdminUser(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin-user-list";
    }

    @GetMapping("/admin-regist-list")
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
        return "/admin-regist-list";
    }

    @GetMapping("/changePage-adminRegistList")
    public String changePageAdminRegistList(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin-regist-list";
    }

    @GetMapping("/accept-shelter")
    public String acceptShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Opening");
        redirectAttributes.addAttribute("page", 0);
        return "redirect:/admin-regist-list";
    }

    @GetMapping("/refuse-shelter")
    public String refuseShelter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        this.sheltersService.updateShelter(id, "Canceled");
        redirectAttributes.addAttribute("page", 0);
        return "redirect:/admin-regist-list";
    }

    @GetMapping("/admin-user-detail")
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
        model.addAttribute("listAdopt", this.adoptService.findByUsers(userName, "%", pg == 0 ? 0 : pg - 1, 8));
        if(changePassword != null){
            model.addAttribute("changePassword", changePassword ? "thành công" : "thất bại");
        }
        return "/admin-user-detail";
    }

    @GetMapping("/changePage-adminUserDetail")
    public String changePageAdminUserDetail(@RequestParam("page") int page, @RequestParam("id") String id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin-user-detail";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("id") String id, RedirectAttributes redirectAttributes, @RequestParam("password") String password){
        redirectAttributes.addAttribute("id", id);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String enCode = bCryptPasswordEncoder.encode(password);
        Users users = this.usersService.findById(id).get();
        users.setPassword("{bcrypt}"+enCode);
        redirectAttributes.addAttribute("changePassword", this.usersService.changeInfoUser(users));
        return "redirect:/admin-user-detail";
    }

    @GetMapping("/admin-user-donate")
    public String adminUserDonate(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        model.addAttribute("listSpon", this.sponsService.findAllToPage(pg == 0 ? 0 : pg - 1, 10));
        return "/admin-user-donate";
    }

    @GetMapping("/changePage-adminDonate")
    public String changePageAdminDonate(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin-user-donate";
    }

    @GetMapping("/admin-donate-detail")
    public String adminUserDonateDetail(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "id") Long id){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        Users users = this.sponsService.findById(id).getUsers();
        model.addAttribute("users", users);
        model.addAttribute("listSpon", this.sponsService.findAllByUser(pg == 0 ? 0 : pg - 1, 10, users.getUserName()));
        return "/admin-user-donate-detail";
    }

    @GetMapping("/changePage-adminDonateDetail")
    public String changePageAdminDonateDetail(@RequestParam("page") int page, @RequestParam(value = "id") Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin-donate-detail";
    }
}
