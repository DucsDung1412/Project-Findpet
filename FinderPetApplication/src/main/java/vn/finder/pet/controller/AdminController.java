package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.service.AnimalsService;
import vn.finder.pet.service.SheltersService;
import vn.finder.pet.service.UsersService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {
    private SheltersService sheltersService;
    private UsersService usersService;
    private AnimalsService animalsService;

    @Autowired
    public AdminController(SheltersService sheltersService, UsersService usersService, AnimalsService animalsService) {
        this.sheltersService = sheltersService;
        this.usersService = usersService;
        this.animalsService = animalsService;
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("totalPending", this.sheltersService.findByShelterStatus("Awaiting").size());
        model.addAttribute("listTotalPending", this.sheltersService.findByShelterStatus("Awaiting"));

        model.addAttribute("totalShelters", this.sheltersService.findAll(0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalShelters", this.sheltersService.findAll(0, 6).stream());

        model.addAttribute("totalUsers", this.usersService.findAll(0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalUsers", this.usersService.findAll(0, 7).stream());

        return "/admin-dashboard";
    }

    @GetMapping("/admin-shelter-list")
    public String adminShelterList(Model model){
        model.addAttribute("listTotalShelters", this.sheltersService.findAll(0, 10));
        this.sheltersService.findAll(0, 10).stream().forEach(e -> {
            System.out.println(e.getListAnimals().size());
        });
        return "/admin-shelter-list";
    }

    @GetMapping("/admin-shelter-detail")
    public String adminShelterDetail(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        ArrayList<List<String>> listOpen = new ArrayList<>();
        Arrays.stream(this.sheltersService.findById(2L).getShelterInfoOperatingTime().split(", ")).forEach(e -> {
            listOpen.add(Arrays.stream(e.split(": ")).toList());
        });
        model.addAttribute("listOpen", listOpen);
        model.addAttribute("shelter", this.sheltersService.findById(2L));
        model.addAttribute("listAnimal", this.animalsService.findAllPet(this.sheltersService.findById(2L).getUsers().getUserName(), pg == 0 ? 0 : pg - 1, 8));
        return "/admin-shelter-detail";
    }

    @GetMapping("/changePage-adminShelterDetail")
    public String changePageAdminShelterDetail(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/admin-shelter-detail";
    }
}
